package serverSide.objects;

import java.rmi.registry.*;
import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;


/**
 *  ControlCollectionSite.
 *
 *    All public methods are executed in mutual exclusion.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */

public class ControlCollectionSite implements ControlCollectionSiteInterface {
    
    /**
     * Indicate ordinary thieve handed a canvas
     */
    private boolean handed;

    /**
     * Indicate if the thief gives a canvas or is empty
     */
    private int canvas;

    /**
     * Flag that indicates if master thief as collected a canvas
     */
    private boolean collected;

    /**
     * Indicate the room that has been assaulted
     */
    private int room;

    /**
     * State of each room
     */
    private boolean[] rooms;

    /**
     * Idx of the room with still painting in the wall
     */
    private int idx;

    /**
     *  Ordinary threads.
     */
    private final Thread [] ord;

    /**
     * State of the ordinary
     */
    private final int[] ordinaryState;

    /**
     *  Master thread.
     */
    private Thread master;

    /**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     *   General repository.
     */
    private final GeneralReposInterface reposStub;

    /**
     * ControlCollectionSite instantiation
     *
     * @param reposStub reference to the general repository
     */
    public ControlCollectionSite(GeneralReposInterface reposStub){
        this.reposStub = reposStub;
        this.canvas = -1;
        this.room = -1;
        this.idx = 0;
        this.rooms = new boolean[SimulConsts.N];
        for(int i=0; i<SimulConsts.N; i++) rooms[i]=true;
        this.handed = false;
        this.collected = false;

        master = null;
        ord = new Thread [SimulConsts.M-1];
        ordinaryState = new int[SimulConsts.M-1];
        for (int i = 0; i < SimulConsts.M-1; i++){
            ord[i] = null;
            ordinaryState[i] = -1;
        }
        nEntities = 0;
    }

    /**
     * Get room state
     *
     * @return room state
     */
    public synchronized int getRoomIdx(){
        while(idx<SimulConsts.N){
            if(rooms[idx]) break;
            idx++;
        }
        return idx;
    }

    /**
     * Master start operations
     * @return thief state
     */
    public synchronized int startOperation() throws RemoteException{
        //Update Master state
        int masterState;
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
     * Master take a rest until return of the ordinary thieves
     * @return thief state
     */
    public synchronized int takeARest() throws RemoteException{

        while(!handed){
			try { wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

        if(canvas==0) rooms[room] = false;
        else reposStub.setRobbedPaintings();
        
        canvas = -1; 
        room = -1;

        //Update Master state
        int masterState;
        master = Thread.currentThread();
        masterState = MasterStates.WAITING_FOR_GROUP_ARRIVAL;
        try{
            reposStub.setMasterState(masterState);
        }catch (RemoteException e) { 
            GenericIO.writelnString ("Master remote exception on sendAssaultParty - setMasterState: " + e.getMessage ());
            System.exit (1);
        }

        return masterState;
    }



    /**
     * Ordinary thieves hands the canvas to the master thief (if it has one)
     * 
     * @param canvas or empty handed
     * @param ap assault party
     * @param members member id
     * @param room heisted by the thief
     */
    public synchronized void handACanvas(int canvas, int room, int ap, int members) throws RemoteException{
        
        while(handed){
			try { wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


        handed = true;
        this.canvas = canvas;
        this.room = room;
        notifyAll();
        reposStub.setCanvas(ap * SimulConsts.E + members, 0);

        while(!collected){
			try { wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

        collected = false;
    }


    /**
     * The master thief collects a canvas from ordinary thief
     * @return state
     */
    public synchronized int collectACanvas() throws RemoteException{
        collected = true;
        handed = false;
        notifyAll();
        
        //Update Master state
        int masterState;
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
   *   Operation server shutdown.
   *
   *   New operation.
   */
   public synchronized void shutdown () throws RemoteException{
       nEntities += 1;
       if (nEntities >= SimulConsts.SHT)
          ServerControlCollectionSite.shutdown();
       notifyAll ();                                        // the master may now terminate
   }
}
