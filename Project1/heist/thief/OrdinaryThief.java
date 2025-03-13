package heist.thief;

import heist.Configuration;
import heist.interfaces.AssaultParty;
import heist.interfaces.ConcentrationSite;
import heist.interfaces.ControlCollectionSite;
import heist.interfaces.Logger;
import heist.interfaces.Museum;


/**
 * OrdinaryThief Thread.
 * Thieves enter the museum stole canvas and come back to return it to the MasterThief.
 */
public class OrdinaryThief extends Thread
{
    /**
     * Concentration site state
     */
    public static final int CONCENTRATION_SITE = 1000;
    
    /**
     * Crawling inwards state
     */
    public static final int CRAWLING_INWARDS = 2000;
    
    /**
     * At a room state
     */
    public static final int AT_A_ROOM = 3000;
    
    /**
     * Crawling outwards
     */
    public static final int CRAWLING_OUTWARDS = 4000;

    /**
     * Collection site state
     */
    public static final int COLLECTION_SITE = 5000;

    /**
     * Thief unique id.
     */
    private int id;
    
    /**
     * Concentration site
     */
    private final ConcentrationSite concentration;
    
    /**
     * Collection site
     */
    private final ControlCollectionSite controlCollection;
    
    /**
     * Museum
     */
    private final Museum museum;

    /**
     * AssaultParties
     */
    private final AssaultParty[] parties;
    
    /**
     * Logger
     */
    private final Logger logger;

    /**
     * How far can the thief move in one step.
     */
    private int maximumDisplacement;

    /**
     * Thief crawling position.
     */
    private int position;

    /**
     * Party id
     */
    private int party;
    
    /**
     * Thief state.
     */
    private int state;

    /**
     * True if the thief carries a canvas.
     */
    private boolean hasCanvas;

    
    /**
     * Ordinary Thief constructor.
     * @param id Thief id.
     * @param controlCollection ControlCollectionSite
     * @param concentration ConcentrationSite
     * @param museum Museum
     * @param parties AssaultParties
     * @param logger Logger
     * @param configuration Simulation configuration
     */
    public OrdinaryThief(int id, ControlCollectionSite controlCollection, ConcentrationSite concentration, Museum museum, AssaultParty[] parties, Logger logger, Configuration configuration)
    {
        this.id = id;
        this.state = OrdinaryThief.CONCENTRATION_SITE;
        this.museum = museum;
        this.party = -1;
        this.controlCollection = controlCollection;
        this.concentration = concentration;
        this.maximumDisplacement = configuration.thiefDisplacement.generateInRange();
        this.parties = parties;
        this.hasCanvas = false;
        this.logger = logger;
        this.position = 0;
    }
    
    /**
     * @return Thief state.
     */
    public int state()
    {
        return this.state;
    }

    /**
     * Change OrdinaryThief state.
     * @param state New state.
     */
    private void setState(int state)
    {
        this.state = state;
    }

    /**
     * @return Returns a P if has party or a W otherwise.
     */
    public char hasParty()
    {
        if(this.party == -1)
        {
            return 'W';
        }
        return 'P';
    }

    /**
     * @return Thief position
     */
    public int getPosition()
    {
        return this.position;
    }

    /**
     * @return Party assigned to the OrdinaryThief.
     */
    public int getParty()
    {
        return this.party;
    }
    
    /**
     * @param party Assault party.
     */
    public void setParty(int party)
    {
        this.party = party;
    }

    /**
     * @return Thief maximum displacement.
     */
    public int getDisplacement()
    {
        return this.maximumDisplacement;
    }
    
    /**
     * Leave party is called after handing the canvas to the MasterThief.
     * @throws Exception
     */
    public void leaveParty() throws Exception
    {
        if(this.party != -1)
        {

            this.parties[this.party].removeThief(this.id);
            this.party = -1;
        }
    }

    /**
     * Check if thieve has a canvas.
     * @return Return 1 if thief has a canvas 0 otherwise.
     */
    public int hasCanvas()
    {
        if(this.hasCanvas == true){
            return 1;
        }
        return 0;
    }

    /**
     * @return True if there is a canvas to hand, false otherwise.
     */
    public boolean deliverCanvas()
    {
        boolean canvas = this.hasCanvas;
        this.hasCanvas = false;
        return canvas;
    }

    /**
     * @return Thief ID
     */
    public int getID()
    {
        return this.id;
    }

    /**
     * Prepare Excursion, assign party to thief and change state to crawling inwards.
     * @throws Exception Exception
     */
    private void prepareExcursion() throws Exception
    {
        this.setParty(this.concentration.prepareExcursion(this));
        this.logger.log(this);
    }

    /**
     * Updates thief position inside the museum and set thief back to sleep, until another thief wakes it up.
     * @throws java.lang.InterruptedException Exception
     */
    private void crawlIn() throws Exception
    {
        this.setState(OrdinaryThief.CRAWLING_INWARDS);
        while(this.parties[this.party].canICrawl(this))
        {
            this.position = this.parties[this.party].crawlIn(this);
            this.logger.log(this);
        }
    }

    /**
     * Check if the thief is still needed.
     * @throws java.lang.InterruptedException Exception
     */
    private boolean amINeeded() throws Exception
    {

        //this.setState(OrdinaryThief.CONCENTRATION_SITE); //
        boolean amINeeded = this.concentration.amINeeded(this);
        //System.out.println("amINeeded-> " + amINeeded);
        if(amINeeded == true)
        {
            this.logger.log(this);
        }
        return amINeeded;
    }

    /**
     * Collect a canvas from the room.
     */
    private void rollACanvas() throws Exception
    {
        this.setState(OrdinaryThief.AT_A_ROOM);
        int target = this.parties[this.party].getTargetRoomID();
        this.logger.log(this);
        this.hasCanvas = this.museum.rollACanvas(target);
        this.logger.log(this);
    }
    
    /**
     * Change state to crawling outwards.
     */
    private void reverseDirection() throws Exception
    {
        this.parties[this.party].reverseDirection(this);
        this.logger.log(this);
    }
    
    /**
     * Update position crawling out of the museum.
     * @throws java.lang.InterruptedException Exception
     */
    private void crawlOut() throws Exception
    {
        this.setState(OrdinaryThief.CRAWLING_OUTWARDS);
        while(this.parties[this.party].canICrawl(this))
        {
            this.position = this.parties[this.party].crawlOut(this);
            this.logger.log(this);
        }

    }
    
    /**
     * Hand the canvas, if there is one, to the master thief.
     */
    private void handACanvas() throws Exception
    {
        this.setState(OrdinaryThief.COLLECTION_SITE);
        this.controlCollection.handACanvas(this);
        this.logger.log(this);
        this.leaveParty();
    }

    /**
     * Ordinary Thief life cycle.
     */
    @Override
    public void run()
    {
        try
        {
            //&& !this.concentration.roomsAreClear()
            while(this.amINeeded())
            {
                this.prepareExcursion();
                this.crawlIn();
                this.rollACanvas();
                this.reverseDirection();
                this.crawlOut();
                this.handACanvas();
                //System.out.println("CHECKPOINTTTTTTTTTTTTTTTTTTTTTTTTTT");
            }
        }
        catch(Exception e)
        {
            System.out.println("ERROR in OrdinaryThief " + this.id);
            e.printStackTrace(); //shows the sequence of method calls that led to the error
            System.exit(1);
        }
        System.out.println("OrdinaryThief " + this.id + " ended");
    }
}
