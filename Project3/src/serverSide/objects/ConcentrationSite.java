package serverSide.objects;

import java.rmi.registry.*;
import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;


/**
 *  ConcentrationSite
 *
 *    All public methods are executed in mutual exclusion.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */
public class ConcentrationSite implements ConcentrationSiteInterface {
    
    /**
     * Ordinary thieves Queue.
     */
    private MemFIFO<Integer> thievesQueue;

    /**
     * Number of ordinary thieves waiting
     */
    private int waitingThieves;

    /**
     *  Number of recruited thieves
     */
    private int recruited;

    /**
     * Flag to indicates if master is going to sum up the results from the museum heist
     */
    private boolean results;

    /**
     * Summon thieve to an assault party
     */
    private int summon;

    /**
     * Flag that indicates if the thief summoned responds to the call
     */
    private boolean summoned;

    /**
     * Preparing assault Party
     */
    private int preparingAP;

    /**
     * Number of members on Assault parties
     */
    private int[] party;

    /**
     * Number of thieves currently heisting the museum
     */
    private int heisting;

    /**
     * Indicate which the Assault party is heisting
     */
    private int rooms[];

    /**
     *  Ordinary threads.
     */
    private final Thread [] ord;

    /**
     * State of the ordinary thief
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
     * Reference to the general reposStub.itory.
     */
    private final GeneralReposInterface reposStub;

    /**
     * ConcentrationSite instantiation
     *
     * @param reposStub reference to the general reposStub.itory
     */
    public ConcentrationSite(GeneralReposInterface reposStub) {

        this.reposStub = reposStub;
        try { this.thievesQueue = new MemFIFO<>(new Integer[SimulConsts.M-1]);
        } catch (MemException e) {
            GenericIO.writelnString ("Instantiation of waiting FIFO failed: " + e.getMessage ());
            System.exit (1);
        }

        this.waitingThieves = 0;
        this.results = false;
        this.recruited = 0;
        this.summoned = true;
        this.summon = -1;
        this.preparingAP = -1;
        this.heisting = 0;
        this.party = new int[2];
        this.rooms = new int[2];
        for(int i=0; i<2; i++){
            party[i] = 0;
            rooms[i] = -1; 
        } 

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
     * Get room assign to the assault party
     *
     * @param ap assault party
     * @return room address to heist
     */
    public synchronized int getRoom(int ap){
        return rooms[ap];
    }

    /**
     * Return one assault party available
     *
     * @return assault party
     */
    public synchronized int getAssautlParty(){
        return rooms[0]<0? 0:1;
    }


    /**
     * Master thief appraise the situation
     * 
     * @param roomState indicate if all rooms are empty
     * @return the master decision
     */
    public synchronized int appraiseSit(boolean roomState) throws RemoteException {
        if(roomState){
            if(heisting>0){
                heisting--;
                return 2;
            } 
            return 3;
        }
        if(waitingThieves>=SimulConsts.E && (rooms[0]<0 || rooms[1]<0)) return 1;
        if(heisting>0){
            heisting--;
            return 2;
        } 

        while (waitingThieves<SimulConsts.E) {
            try {  wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return 1;
    }


    /**
     * The master thief prepares an assault party
     * 
     * @param ap assault party to prepare
     * @param room to assault
     * @return state
     */
    public synchronized int prepareAssaultParty(int ap, int room) throws RemoteException{
        reposStub.setApRoom(ap, room);
        preparingAP = ap;

        // Update Master state
        int masterState;
        master = Thread.currentThread();
        masterState = MasterStates.ASSEMBLING_A_GROUP;
        try{
            reposStub.setMasterState(masterState);
        }catch (RemoteException e) {
            GenericIO.writelnString ("Master remote exception on sendAssaultParty - setMasterState: " + e.getMessage ());
            System.exit (1);
        }

        while (recruited < SimulConsts.E) {
            if(summoned){
                summoned = false;
                try {
                    summon = thievesQueue.read();
                    waitingThieves--;
                } catch (Exception e) {}
                notifyAll();
            } 

            

            try { wait();
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            }
        }

        recruited = 0;
        summon = -1;
        preparingAP = -1;
        rooms[ap] = room;
        heisting += SimulConsts.E;

        return masterState;
    }


    /**
     * Ordinary thief prepare excursion
     * @param ordinaryId ordinary id
     * @return joined assault party
     */
    public synchronized ReturnInt prepareExcursion(int ordinaryId) throws RemoteException{
        // Update Ordinary state
        ord[ordinaryId] =  Thread.currentThread();
        ordinaryState[ordinaryId] = (OrdinaryStates.CRAWLING_INWARDS);
        try{
            reposStub.setOrdinaryState(ordinaryId, ordinaryState[ordinaryId]);
        }catch (RemoteException e) { 
            GenericIO.writelnString ("Ordinary " + ordinaryId + " remote exception on reverseDirection - setOrdinaryState: " + e.getMessage ());
            System.exit (1);
        }

        recruited++;
        summoned = true;
        notifyAll();

        party[preparingAP]++;
        reposStub.setOrdinarySituation(ordinaryId, 'P');
        return new ReturnInt(preparingAP, ordinaryState[ordinaryId]);
    }




    /**
     * Ordinary thief indicates to the master that he is free
     * 
     * @param ap assault party from which the ordinary thieve work before
     * @return master service decision
     */
    public synchronized ReturnBoolean amINeeded(int ap, int ordinaryId) throws RemoteException{
        //Update Ordinary state
        ord[ordinaryId] = Thread.currentThread();
        ordinaryState[ordinaryId] = (OrdinaryStates.CONCENTRATION_SITE);
        try{
            reposStub.setOrdinaryState(ordinaryId, ordinaryState[ordinaryId]);
        }catch (RemoteException e) { 
            GenericIO.writelnString ("Ordinary " + ordinaryId + " remote exception on reverseDirection - setOrdinaryState: " + e.getMessage ());
            System.exit (1);
        }

        if(ap>=0 && --party[ap]==0){
            reposStub.setApRoom(ap, -1);
            rooms[ap]=-1;
        } 

        
        reposStub.setOrdinarySituation(ordinaryId, 'W');
        try {
            thievesQueue.write(ordinaryId);
            waitingThieves++;
        } catch (Exception e) {}
        notifyAll();

        while(summon!=ordinaryId && !results){
			try { wait();
			} catch (InterruptedException e) {
				GenericIO.writelnString(" "+e.getMessage());
                System.exit(0);
			}
		}

        return new ReturnBoolean(summon==ordinaryId, ordinaryState[ordinaryId]);
    }



    /**
     * Sum up the results from the heist
     * @return state
     */
    public synchronized int sumUpResults() throws RemoteException{
        results = true;
        notifyAll();
        
        // Update Master state
        int masterState;
        master = Thread.currentThread();
        masterState = MasterStates.PRESENTING_THE_REPORT;
        try{
            reposStub.setMasterState(masterState);
        }catch (RemoteException e) { 
            GenericIO.writelnString ("Master remote exception on sendAssaultParty - setMasterState: " + e.getMessage ());
            System.exit (1);
        }

        return masterState;
    }


    /**
   *  End operation.
   *
   *   New operation.
   *   @param ordId ordinary id
   */
   public synchronized void endOperation (int ordId) throws RemoteException{
      while (nEntities == 0)
      { /* the master waits for the termination of the ordinaries */
        try
        { wait ();
        }
        catch (InterruptedException e) {}
      }
      if (ord[ordId] != null)
         ord[ordId].interrupt ();
   }


   /**
   *   Operation server shutdown.
   *
   *   New operation.
   */
   public synchronized void shutdown () throws RemoteException{
       nEntities += 1;
       if (nEntities >= SimulConsts.SHT)
          ServerConcentrationSite.shutdown();
       notifyAll ();                                        // the master may now terminate
   }

}
