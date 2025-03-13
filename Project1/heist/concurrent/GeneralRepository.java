package heist.concurrent;

import heist.concurrent.shared.SharedConcentrationSite;
import heist.concurrent.shared.SharedAssaultParty;
import heist.concurrent.shared.SharedLogger;
import heist.concurrent.shared.SharedMuseum;
import heist.concurrent.shared.SharedControlCollectionSite;
import heist.Configuration;
import heist.interfaces.AssaultParty;
import heist.interfaces.ConcentrationSite;
import heist.interfaces.ControlCollectionSite;
import heist.interfaces.Museum;
import heist.thief.MasterThief;
import heist.thief.OrdinaryThief;

/**
 * It is a shared memory region that is accessed by every active entity in the system.
 * Is responsible for starting/killing all running instances.
 */
public class GeneralRepository
{
    /**
     * Museum to be assaulted.
     */
    private final Museum museum;

    /**
     * Master Thief
     */
    private final MasterThief master;
    
    /**
     * The collection site.
     */
    private final ControlCollectionSite controlCollection;
    
    /**
     * Concentration site where the OrdinaryThieves wait to be assigned to an AssaultParty.
     */
    private final ConcentrationSite concentration;

    /**
     * Logger object used to log the state of this GeneralRepository
     */
    private final SharedLogger logger;
    
    /**
     * AssaultParties to be used in the simulation.
     */
    private final AssaultParty[] parties;

    /**
     * OrdinaryThieves array
     */
    private final OrdinaryThief[] thieves;

    /**
     * Configuration used for the simulation.
     */
    private final Configuration configuration;
    
    /**
     * Constructor
     * @param configuration Configuration to be used to create elements in this simulation.
     * @throws Exception
     */
    public GeneralRepository(Configuration configuration) throws Exception
    {
        this.configuration = configuration;

        this.parties = new AssaultParty[configuration.numberParties];
        for(int i = 0; i < this.parties.length; i++)
        {
            this.parties[i] = new SharedAssaultParty(i, configuration);
        }
        
        this.museum = new SharedMuseum(this.configuration);
        this.concentration = new SharedConcentrationSite(this.parties, this.configuration, this.museum);
        this.controlCollection = new SharedControlCollectionSite(this.parties, this.museum, this.configuration, this.concentration);
        
        this.logger = new SharedLogger(this.parties, this.museum, this.controlCollection, this.configuration);
        
        this.thieves = new OrdinaryThief[configuration.numberThieves];
        for(int i = 0; i < this.thieves.length; i++)
        {
            this.thieves[i] = new OrdinaryThief(i, this.controlCollection, this.concentration, this.museum,  this.parties, this.logger, this.configuration);
        }
        this.master = new MasterThief(this.controlCollection, this.concentration, this.museum, this.parties, this.logger, this.configuration);
    }

    /**
     * @return Museum
     */
    public Museum getMuseum()
    {
        return this.museum;
    }

    /**
     * @return MasterThief.
     */
    public MasterThief getMasterThief()
    {
        return this.master;
    }
    
    /**
     * @return OrdinaryThief array.
     */
    public OrdinaryThief[] getOrdinaryThieves()
    {
        return this.thieves;
    }
}
