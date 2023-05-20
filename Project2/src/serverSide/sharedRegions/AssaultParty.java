package serverSide.sharedRegions;

import java.util.Arrays;
import serverSide.main.*;
import serverSide.entities.*;
import clientSide.entities.*;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;

public class AssaultParty {
    /**
     * Number of Assault Party Members
     */
    private int members;

    /**
     * Assign an Assault Party id to the thief
     * @param ap assault party
     * @return member id assigned to the party
     */

    /**
     * Room to be heisted
     */
    private int room;


    /**
     * Flag sended assault party
     */
    private boolean sended;

    /**
     * Initial step for the first member of the assault party
     */
    private boolean init;

    /**
     * Flag for crawl in movement
     */
    private boolean crawlin;

    /**
     * Number of party members at the room in the museum
     */
    private int atRoom;

    /**
     * Flag for crawl out movement
     */
    private boolean crawlout;



    /**
     * Tha last member signals to reverse march
     */

    private boolean reversed;

    /**
     * Crawl line positions of each member
     */
    private int[] pos;

    /**
     * General Repository.
     */
    private final GeneralReposStub reposStub;

    /**
     * Array with distance from the site to each museum room
     */
    private final int[] rooms;

    /**
     *  ordinary thief threads.
     */
    private final AssaultPartyClientProxy [] ord;

    /**
     *  master thread.
     */
    private AssaultPartyClientProxy master;

    /**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     * Assault Party instantiation
     *
     * @param reposStub general repository
     */

    public AssaultParty(GeneralReposStub reposStub) {
        this.reposStub = reposStub;
        this.rooms = reposStub.getRoomDistances();
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
        ord = new AssaultPartyClientProxy [SimulConsts.M-1];
        for (int i = 0; i < SimulConsts.M-1; i++)
            ord[i] = null;
        nEntities = 0;
    }

    /**
     * Get room id
     * @return room id
     */
    public synchronized int getRoom() {
        return room;
    }

    public synchronized int assignMember(int ap) {
        members = (members + 1) % SimulConsts.E;
        reposStub.setAssaultPartyElement(ap * SimulConsts.E + members, ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryId());
        return members;
    }

    /**
     * Reverse direction
     * @param member member ID
     */
    public synchronized void reverseDirection(int member) {
        int ordinaryId = ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryId();
        if (member == SimulConsts.E - 1) {
            init = true;
            reversed = true;
            atRoom = 0;
            crawlin = false;
            sended = false;
            notifyAll();
        }

        // Update Ordinary state
        ord[ordinaryId] = (AssaultPartyClientProxy) Thread.currentThread();
        ord[ordinaryId].setOrdinaryState(OrdinaryStates.CRAWLING_OUTWARDS);
        reposStub.setOrdinaryState(ordinaryId, ord[ordinaryId].getOrdinaryState());

    }

    /**
     * Master sends the assault party to the museum
     * 
     * @param room to heist
     */
    public synchronized void sendAssaultParty(int room) {
        crawlin = false;
        sended = true;
        init = true;
        crawlout = false;
        reversed = false;
        this.room = room;
        notifyAll();
        // Update Master state
        master = (AssaultPartyClientProxy) Thread.currentThread();
        master.setMasterState(MasterStates.DECIDING_WHAT_TO_DO);
        reposStub.setMasterState(0, master.getMasterState());
    }

    /**
     * Crawl in movement
     * 
     * @param ap     assault party number
     * @param member id of the thief in the crawl line
     * @param md     maximum distance capable by the thief
     * @return true if the thief reached the room in the museum
     */
    public synchronized boolean crawlIn(int ap, int member, int md) {
        int move = 0;
        move = calculateMoveIn(member, md);

        int ordinaryId = ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryId();
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
        pos[member] = (pos[member] > rooms[room]) ? rooms[room] : pos[member];

        reposStub.setPosition(ap * SimulConsts.E + member, pos[member]);
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
            ord[ordinaryId] = (AssaultPartyClientProxy) Thread.currentThread();
            ord[ordinaryId].setOrdinaryState(OrdinaryStates.AT_A_ROOM);
            reposStub.setOrdinaryState(ordinaryId, ord[ordinaryId].getOrdinaryState());
        }

        return pos[member] < rooms[room];
    }

    /**
     * Crawl out movement
     *
     * @param ap     assault party number
     * @param member id of the thief in the crawl line
     * @param md     maximum distance capable by the thief
     * @return true if the thief reached the site
     */
    public synchronized boolean crawlOut(int ap, int member, int md) {
        int move = 0;
        move = calculateMoveOut(member,md);

        int ordinaryId = ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryId();
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
        pos[member] = (pos[member] < 0) ? 0 : pos[member];

        reposStub.setPosition(ap * SimulConsts.E + member, pos[member]);
        notifyAll();

        if (pos[member] == 0) {
            // Update Ordinary state
            ord[ordinaryId] = (AssaultPartyClientProxy) Thread.currentThread();
            ord[ordinaryId].setOrdinaryState(OrdinaryStates.COLLECTION_SITE);
            reposStub.setOrdinaryState(ordinaryId, ord[ordinaryId].getOrdinaryState());
        }

        return pos[member] > 0;
    }

    /**
     * Auxiliar function to update move in crawlIn
     * @param member id of the thief in the crawl line
     * @param md maximum distance capable by the thief
     * @return
     */

    private synchronized int calculateMoveIn(int member, int md) {
        if (sended) {
            for (int i = md; i > 0; i--) {
                if (valid(member, i)) {
                    return i;
                }
            }
        } else {
            return member == 0 ? 3 : 0;
        }
        return 0;
    }

    /**
     * Auxiliar function to update move in crawlOut
     * @param member id of the thief in the crawl line
     * @param md maximum distance capable by the thief
     * @return
     */
    private synchronized int calculateMoveOut(int member, int md) {
        if (reversed) {
            for (int i = md; i > 0; i--) {
                if (valid(member, -i)) {
                    return i;
                }
            }
        } else {
            return member == 0 ? 3 : 0;
        }
        return 0;
    }


    /**
     * Aux function to validate a state given a movement of the thief
     *
     * @param member id in the party
     * @param p      movement to test
     * @return true if the given state is valid
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
   *   Server shutdown.
   */

   public synchronized void shutdown ()
   {
       nEntities = nEntities + 1;
       if (nEntities >= SimulConsts.SHT)
            ServerAssaultParty.waitConnection = false;
          
       notifyAll ();
   }
}