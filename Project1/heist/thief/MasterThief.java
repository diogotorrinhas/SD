package heist.thief;

import heist.room.RoomStatus;
import heist.Configuration;
import heist.interfaces.AssaultParty;
import heist.interfaces.ConcentrationSite;
import heist.interfaces.ControlCollectionSite;
import heist.interfaces.Logger;
import heist.interfaces.Museum;

/**
 * MasterThief Thread
 * Is responsible from planning and prepare the Heist.
 */
public class MasterThief extends Thread
{
    /**
     * Planning the heist state.
     */
    public static final int PLANNING_THE_HEIST = 1000;

    /**
     * Waiting for group arrival state.
     */
    public static final int WAITING_FOR_GROUP_ARRIVAL = 4000;
    
    /**
     * Deciding what to do state.
     */
    public static final int DECIDING_WHAT_TO_DO = 2000;
    
    /**
     * Assembling a party state.
     */
    public static final int ASSEMBLING_A_GROUP = 3000;

    
    /**
     * Presenting report state.
     */
    public static final int PRESENTING_THE_REPORT = 5000;

    /**
     * Control and Collection Site
     */
    private final ControlCollectionSite controlCollection;
   
    /**
     * Concentration Site
     */
    private final ConcentrationSite concentration;

    /**
     * AssaultParties
     */
    private final AssaultParty[] parties;

    /**
     * Simulation configuration.
     */
    private final Configuration configuration;

    /**
     * Logger
     */
    private final Logger logger;

    /**
     * MasterThief state.
     */
    private int state;

    /**
     * Museum
     */
    private final Museum museum;

    /**
     * Master Thief constructor
     * @param controlCollection ControlCollectionSite
     * @param concentration ConcentrationSite
     * @param museum Museum
     * @param parties AssaultParties
     * @param logger Logger
     * @param configuration Simulation configuration
     */
    public MasterThief(ControlCollectionSite controlCollection, ConcentrationSite concentration, Museum museum, AssaultParty[] parties, Logger logger, Configuration configuration)
    {
        this.state = MasterThief.PLANNING_THE_HEIST;
        
        this.controlCollection = controlCollection;
        this.concentration = concentration;
        this.museum = museum;
        this.parties = parties;
        this.logger = logger;
        this.configuration = configuration;
    }
    
    /**
     * Change MasterThief state
     * @param state 
     */
    private void setState(int state)
    {
        this.state = state;
    }
    
    /**
     * @return Thief state.
     */
    public int state()
    {
        return this.state;
    }
    

    /**
     * After this state, it changes the master thief state do deciding what to do.
     * @throws Exception Exception
     */
    private void startOperations() throws Exception
    {
        this.concentration.startOperations();
        this.setState(MasterThief.DECIDING_WHAT_TO_DO);
        this.logger.log(this);
    }
    
    /**
     * Take the decisions about the ordinaryThief state
     * @throws java.lang.InterruptedException Exception
     */
    private void appraiseSit() throws Exception
    {
        this.setState(this.concentration.appraiseSit());
    }

    /**
     * The MasterThief waits in the CollectionSite until is awaken by an OrdinaryThief.
     * @throws java.lang.InterruptedException Exception
     */
    private void takeARest() throws Exception
    {
        this.controlCollection.takeARest();
        this.logger.log(this);
    }
    
    /**
     * Prepare an assaultParty
     * @return AssaultParty ID.
     * @throws java.lang.InterruptedException Exception
     */
    private int prepareAssaultParty() throws Exception
    {
        RoomStatus room = this.concentration.getRoomToAttack();
        this.logger.log(this);
        int partyID = this.concentration.createAssaultParty(room);
        this.logger.log(this);
        this.concentration.addThievesToParty(partyID);
        this.logger.log(this);
        return partyID;
    }
    
    /**
     * Send assault party with thieves from the party created.
     * Wakes up the first thief in the party. That thief will wake the other thieves.
     * @param partyID Party to send.
     * @throws java.lang.InterruptedException Exception
     */
    private void sendAssaultParty(int partyID) throws Exception
    {
        this.parties[partyID].sendAssaultParty();
        this.setState(MasterThief.DECIDING_WHAT_TO_DO);
        this.logger.log(this);
    }

    /**
     * Collect canvas from thieve waiting in the collection site.
     * @throws java.lang.InterruptedException Exception
     */
    private void collectCanvas() throws Exception
    {
        this.controlCollection.collectCanvas();
        this.setState(MasterThief.DECIDING_WHAT_TO_DO);
        this.logger.log(this);
    }
    
    /**
     * Sum up the heist results, prepare a log of the heist and end the hole simulation.
     * Stop all thieves.
     */
    private void sumUpResults() throws Exception
    {
        //this.setState(MasterThief.PRESENTING_THE_REPORT);
        this.concentration.sumUpResults();
        this.logger.end();
        this.controlCollection.end();
        for(int i = 0; i < this.parties.length; i++)
        {
            this.parties[i].end();
        }
        this.concentration.end();
        this.museum.end();
        //this.setState(MasterThief.PRESENTING_THE_REPORT);
    }

    /**
     * Master Thief life cycle.
     */
    @Override
    public void run()
    {
        try
        {
            this.startOperations();
            while(this.state != MasterThief.PRESENTING_THE_REPORT)
            {
                this.appraiseSit();
                switch (this.state){
                    case ASSEMBLING_A_GROUP:
                        this.sendAssaultParty(this.prepareAssaultParty());
                        break;
                    case WAITING_FOR_GROUP_ARRIVAL:
                        this.takeARest();
                        this.collectCanvas();
                        break;
                }
            }
            this.sumUpResults();
        }
        catch(Exception e)
        {
            System.out.println("Error in MasterThief");
            e.printStackTrace(); //shows the sequence of method calls that led to the error
            System.exit(1);
        }
        System.out.println("MasterThief ended");
    }
}
