package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import clientSide.entities.*;
import clientSide.stubs.*;


public class ControlCollectionSite {
    
    /**
     * Flag that indicates if an ordinary thieve handed a canvas
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
     *  ordinary threads.
     */
    private final ControlCollectionSiteClientProxy [] ord;

    /**
     *  master thread.
     */
    private ControlCollectionSiteClientProxy master;

    /**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     *   general repository.
     */
    private final GeneralReposStub reposStub;

    /**
     * ControlCollectionSite instantiation
     *
     * @param reposStub reference to the general repository
     */
    public ControlCollectionSite(GeneralReposStub reposStub){
        this.reposStub = reposStub;
        this.canvas = -1;
        this.room = -1;
        this.idx = 0;
        this.rooms = new boolean[SimulConsts.N];
        for(int i=0; i<SimulConsts.N; i++) rooms[i]=true;
        this.handed = false;
        this.collected = false;

        master = null;
        ord = new ControlCollectionSiteClientProxy [SimulConsts.M-1];
        for (int i = 0; i < SimulConsts.M-1; i++)
            ord[i] = null;
        nEntities = 0;
    }

    


    /**
     * Master start operations
     */
    public synchronized void startOperation(){
        //Update Master state
        master = (ControlCollectionSiteClientProxy) Thread.currentThread();
        master.setMasterState(MasterStates.DECIDING_WHAT_TO_DO);
        reposStub.setMasterState(0, master.getMasterState());
    }


    /**
     * Master take a rest until return of the ordinary thieves
     */
    public synchronized void takeARest(){
        while(handed == false){
			try { wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
        if(canvas==0){
            rooms[room] = false;
        }
        else{
            reposStub.setRobbedPaintings();
        }
        canvas = -1; 
        room = -1;

        //Update Master state
        master = (ControlCollectionSiteClientProxy) Thread.currentThread();
        master.setMasterState(MasterStates.WAITING_FOR_GROUP_ARRIVAL);
        reposStub.setMasterState(0, master.getMasterState());

    }


    /**
     * Ordinary thieves hands the canvas to the master thief (if it has one)
     * 
     * @param canvas or nothing
     * @param ap assault party
     * @param members member id
     * @param room robbed by the thief
     */
    public synchronized void handACanvas(int canvas, int room, int ap, int members){
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
     */
    public synchronized void collectACanvas(){
        collected = true;
        handed = false;
        notifyAll();
        
        //Update Master state
		master = (ControlCollectionSiteClientProxy) Thread.currentThread();
        master.setMasterState(MasterStates.DECIDING_WHAT_TO_DO);
        reposStub.setMasterState(0, master.getMasterState());
    }

    /**
   *  Server shutdown.
   *
   */

   public synchronized void shutdown ()
   {
       nEntities = nEntities + 1;
       if (nEntities >= SimulConsts.SHT)
          ServerControlCollectionSite.waitConnection = false;
       notifyAll ();
   }
}
