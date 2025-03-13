package serverSide.objects;

import java.util.Arrays;
import java.rmi.registry.*;
import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;


/**
 *  Assault Party.
 *
 *    All public methods are executed in mutual exclusion.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */

public class AssaultParty implements AssaultPartyInterface {

    /**
     * Number of members in the party
     */
    private int members;

    /**
     * Room to heist
     */
    private int room;

    /**
     * Sended assault party
     */
    private boolean sended;

    /**
     * Inital step for the first member of the assault party
     */
    private boolean init;

    /**
     * The movement of Crawl in has been initializated
     */
    private boolean crawlin;

    /**
     * Number of party members that are at the door of the museum room
     */
    private int atRoom;

    /**
     * The movement of Crawl out has been initializated
     */
    private boolean crawlout;

    /**
     * Positions of each member during crawl line
     */
    private int[] pos;

    /**
     * Tha last member signals to reverse march
     */

    private boolean reversed;

    /**
     * Reference to the general repository.
     */
    private final GeneralReposInterface reposStub;

    /**
     * Distaces in units from the site to the each museum room
     */
    private int[] rooms = new int[SimulConsts.N];

    /**
     *  Reference to ordinary threads.
     */
    private final Thread [] ord;

    /**
     * State of the ordinary
     */
    private final int[] ordinaryState;

    /**
     *  Reference to master thread.
     */
    private Thread master;

    /**
     * State of the master
     */
    private int masterState;

    /**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     * AP instantiation
     *
     * @param reposStub reference to the general repository
     */

    public AssaultParty(GeneralReposInterface reposStub) {
        this.reposStub = reposStub;
        try{
            this.rooms = reposStub.getRoomDistances();
        }catch (RemoteException e) { 
            GenericIO.writelnString ("assault party remote exception on getRoomDistances: " + e.getMessage ());
            System.exit (1);
        }
        this.room = -1;
        this.members = -1;
        this.reversed = false;
        this.sended = false;
        this.init = false;
        this.crawlin = false;
        this.atRoom = 0;
        this.crawlout = false;
        this.pos = new int[SimulConsts.E];
        for (int i = 0; i < SimulConsts.E; i++)
            pos[i] = 0;

        master = null;
        masterState = -1;
        ord = new Thread [SimulConsts.M-1];
        ordinaryState = new int [SimulConsts.M-1];
        for (int i = 0; i < SimulConsts.M-1; i++){
            ord[i] = null;
            ordinaryState[i] = -1;
        }
        nEntities = 0;
    }

    /**
     * Get room id
     * @return room id
     */
    public synchronized int getRoom() throws RemoteException {
        return room;
    }

    /**
     * Assign a member
     * @param ap assault party
     * @return member id
     */
    public synchronized int assignMember(int ap, int ordinaryId) throws RemoteException {
        members = (members + 1) % SimulConsts.E;
        reposStub.setApElement(ap * SimulConsts.E + members, ordinaryId);
        return members;
    }

    /**
     * Reverse direction
     * @param member id
     */
    public synchronized int reverseDirection(int member, int ordinaryId) throws RemoteException{

        if (member == SimulConsts.E - 1) {
            init = true;
            reversed = true;
            atRoom = 0;
            crawlin = false;
            sended = false;
            notifyAll();
        }

        // Update Ordinary state
        ord[ordinaryId] = Thread.currentThread();
        ordinaryState[ordinaryId] = (OrdinaryStates.CRAWLING_OUTWARDS);
        try{
            reposStub.setOrdinaryState(ordinaryId, ordinaryState[ordinaryId]);
        }catch (RemoteException e) { 
            GenericIO.writelnString ("Ordinary " + ordinaryId + " remote exception on reverseDirection - setOrdinaryState: " + e.getMessage ());
            System.exit (1);
        }

        return ordinaryState[ordinaryId];
    }

    /**
     * Master sends the assault party to the museum
     * 
     * @param room to heist
     */
    public synchronized int sendAssaultParty(int room) throws RemoteException{

        crawlin = false;
        sended = true;
        init = true;
        crawlout = false;
        reversed = false;
        this.room = room;
        notifyAll();

        // Update Master state
        master = Thread.currentThread();
        masterState = MasterStates.DECIDING_WHAT_TO_DO;
        try{
            reposStub.setMasterState(masterState);
        }catch (RemoteException e) { 
            GenericIO.writelnString ("Master remote exception on sendAssaultParty - setMasterState: " + e.getMessage ());
            System.exit (1);
        }
        
        return masterState;
    }

    /**
     * Crawl in movement
     * 
     * @param ap     number
     * @param member id of the thieve in the crawl line
     * @param md     maximum distance capable by the thieve
     * @param ordinaryId ordinary id
     * @return true if the thieve get to the room
     */
    public synchronized ReturnBoolean crawlIn(int ap, int member, int md, int ordinaryId) {
        int move = 0;

        if (sended) {
            for (int i = md; i > 0; i--) {
                if (valid(member, i)) {
                    move = i;
                    break;
                }
            }
        } else {
            move = member == 0 ? 3 : 0;
        }

        
        while ((crawlin && move < 1) || (member != 0 && init) || !sended) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = md; i > 0; i--) {
                if (valid(member, i)) {
                    move = i;
                    break;
                }
            }
        }

        if (member == 0 && init) {
            crawlin = true;
            init = false;
        }

        pos[member] += move;
        if (pos[member] > rooms[room])
            pos[member] = rooms[room];

        try{
            reposStub.setPosition(ap * SimulConsts.E + member, pos[member]);
        }catch (RemoteException e) { 
            GenericIO.writelnString ("Ordinary " + ordinaryId + " remote exception on setPosition: " + e.getMessage ());
            System.exit (1);
        }
        notifyAll();

        if (pos[member] == rooms[room]) {
            atRoom++;
            while (atRoom < SimulConsts.E) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            notifyAll();

            // Update Ordinary state
            ord[ordinaryId] = Thread.currentThread();
            ordinaryState[ordinaryId] = (OrdinaryStates.AT_A_ROOM);
            try{
                reposStub.setOrdinaryState(ordinaryId, ordinaryState[ordinaryId]);
            }catch (RemoteException e) { 
                GenericIO.writelnString ("Ordinary " + ordinaryId + " remote exception on crawlIn - setOrdinaryState: " + e.getMessage ());
                System.exit (1);
            }
        }

        return new ReturnBoolean( pos[member] < rooms[room], ordinaryState[ordinaryId]);
    }

    /**
     * Crawl out movement
     * 
     * @param ap     number
     * @param member id of the thieve in the crawl line
     * @param md     maximum distance capable by the thive
     * @param ordinaryId ordinary id
     * @return true if the thieve get to the site
     */
    public synchronized ReturnBoolean crawlOut(int ap, int member, int md, int ordinaryId) throws RemoteException{
        int move = 0;

        if (reversed) {
            for (int i = md; i > 0; i--) {
                if (valid(member, -i)) {
                    move = i;
                    break;
                }
            }
        } else {
            move = member == 0 ? 3 : 0;
        }

        
        while ((crawlout && move < 1) || (member != 0 && init) || !reversed) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = md; i > 0; i--) {
                if (valid(member, -i)) {
                    move = i;
                    break;
                }
            }

        }

        if (member == 0 && init) {
            crawlout = true;
            init = false;
        }

        pos[member] -= move;
        if (pos[member] < 0)
            pos[member] = 0;

        reposStub.setPosition(ap * SimulConsts.E + member, pos[member]);
        notifyAll();

        if (pos[member] == 0) {
            // Update Ordinary state
            ord[ordinaryId] = Thread.currentThread();
            ordinaryState[ordinaryId] = (OrdinaryStates.COLLECTION_SITE);
            try{
                reposStub.setOrdinaryState(ordinaryId, ordinaryState[ordinaryId]);
            }catch (RemoteException e) { 
                GenericIO.writelnString ("Ordinary " + ordinaryId + " remote exception on crawlOut - setOrdinaryState: " + e.getMessage ());
                System.exit (1);
            }
        }

        return new ReturnBoolean(pos[member] > 0, ordinaryState[ordinaryId]);
    }

    /**
     * Aux function to validate a state given a movement of the thief
     *
     * @param member id in the party
     * @param p      movement to test
     */
    private synchronized boolean valid(int member, int p) {
        int[] test = pos.clone();
        test[member] += p;
        if (test[member] < 0)
            test[member] = 0;
        if (test[member] > rooms[room])
            test[member] = rooms[room];

        Arrays.sort(test);

        for (int i = SimulConsts.E - 1; i > 0; i--) {
            if (test[i - 1] - test[i] == 0 && (test[i] == 0 || test[i] == rooms[room]))
                continue;
            if (test[i] - test[i - 1] > SimulConsts.S || test[i - 1] - test[i] == 0)
                return false;
        }

        return true;
    }


    /**
   *   Operation server shutdown.
   *
   *   New operation.
   */
    public synchronized void shutdown () throws RemoteException{
        nEntities += 1;
        if (nEntities >= SimulConsts.SHT)
                ServerAssaultParty.shutdown();
            
        notifyAll ();
    }
}