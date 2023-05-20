package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import clientSide.entities.*;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;


public class ConcentrationSite {
    
    /**
     * Ordinary thieves Queue.
     */
    private MemFIFO<Integer> thievesQueue;

    /**
     * Number of ordinary thieves waiting
     */
    private int waitingThieves;

    /**
     * Number of recruited thieves
     */
    private int recruited;

    /**
     * Flag to indicates if master is going to sum up the results from the museum heist
     */
    private boolean results;

    /**
     * Summon thief to an assault party
     */
    private int summon;

    /**
     * Flag that indicates if the thief summoned responds to the call
     */
    private boolean summoned;

    /**
     * Preparing assault party
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
     *  ordinary threads.
     */
    private final ConcentrationSiteClientProxy [] ord;

    /**
     *  master thread.
     */
    private ConcentrationSiteClientProxy master;

    /**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     * general repository.
     */
    private final GeneralReposStub reposStub;

    /**
     * ConcentrationSite instantiation
     *
     * @param reposStub reference to the general repository
     */
    public ConcentrationSite(GeneralReposStub reposStub) {

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
        ord = new ConcentrationSiteClientProxy [SimulConsts.M-1];
        for (int i = 0; i < SimulConsts.M-1; i++)
            ord[i] = null;
        nEntities = 0;
    }

    /**
     * Get room assign to the assault party
     *
     * @param ap assault party
     * @return room id
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
     * @param roomState flag to indicate if all rooms are empty
     * @return the master decision
     */
    public synchronized int appraiseSit(boolean roomState) {
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
     */
    public synchronized void prepareAssaultParty(int ap, int room) {
        reposStub.setAssaultPartyRoom(ap, room);
        preparingAP = ap;

        // Update Master stat
        master = (ConcentrationSiteClientProxy) Thread.currentThread();
        master.setMasterState(MasterStates.ASSEMBLING_A_GROUP);
        reposStub.setMasterState(0, master.getMasterState());

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

    }


    /**
     * Ordinary thief prepare excursion
     * 
     * @return joined assault party
     */
    public synchronized int prepareExcursion() {
        // Update Ordinary state
        int ordinaryId = ((ConcentrationSiteClientProxy) Thread.currentThread()).getOrdinaryId();
        ord[ordinaryId] = (ConcentrationSiteClientProxy) Thread.currentThread();
        ord[ordinaryId].setOrdinaryState(OrdinaryStates.CRAWLING_INWARDS);
        reposStub.setOrdinaryState(ordinaryId, ord[ordinaryId].getOrdinaryState());
        recruited++;
        summoned = true;
        notifyAll();
        party[preparingAP]++;
        reposStub.setOrdinarySituation(ordinaryId, 'P');
        return preparingAP;
    }

    /**
     * ordinary thief indicates to the master that he is free
     * 
     * @param ap assault party
     * @return master service decision
     */
    public synchronized boolean amINeeded(int ap){
        //Update Ordinary state
        int ordinaryId = ((ConcentrationSiteClientProxy) Thread.currentThread()).getOrdinaryId();
        ord[ordinaryId] = (ConcentrationSiteClientProxy) Thread.currentThread();
        ord[ordinaryId].setOrdinaryState(OrdinaryStates.CONCENTRATION_SITE);
        reposStub.setOrdinaryState(ordinaryId, ord[ordinaryId].getOrdinaryState());

        if(ap>=0 && --party[ap]==0){
            reposStub.setAssaultPartyRoom(ap, -1);
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

        return summon==ordinaryId;
    }

    /**
     * Sum up the results from the heist
     */
    public synchronized void sumUpResults() {
        results = true;
        notifyAll();
        
        // Update Master state
        master = (ConcentrationSiteClientProxy) Thread.currentThread();
        master.setMasterState(MasterStates.PRESENTING_THE_REPORT);
        reposStub.setMasterState(0, master.getMasterState());
    }


    /**
   *  End operation.
   *
   *
   *   @param ordId ordinary id
   */

   public synchronized void endOperation (int ordId)
   {
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
   * server shutdown.
   *
   */

   public synchronized void shutdown () {
       nEntities = nEntities + 1;
       if (nEntities >= SimulConsts.SHT)
          ServerConcentrationSite.waitConnection = false;
       notifyAll ();                                        // the master may now terminate
   }

}
