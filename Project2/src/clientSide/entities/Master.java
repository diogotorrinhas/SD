package clientSide.entities;

import clientSide.stubs.*;
import serverSide.main.SimulConsts;

public class Master extends Thread {

    /**
     * Master Id
     */
    private int masterId;

    /**
     * Master State
     */
    private int masterState;

    /**
     * Assault partyStub
     */
    private final AssaultPartyStub[] partyStub;

    /**
     * ConcentrationSite
     */
    private final ConcentrationSiteStub concetrationSiteStub;

    /**
     * ControlCollectionSite
     */
    private final ControlCollectionSiteStub controlCollectionSiteStub;

    /**
     * generalRepository
     */
    private final GeneralReposStub reposStub;

    /**
     * Instantiation of a master thread.
     * 
     * @param masterId        Master Id
     * @param masterState     Master state
     * @param partyStub       AssaultpartyStub
     * @param concetrationSiteStub         ConcentrationSite
     * @param controlCollectionSiteStub    ControlCollectionSite
     * @param reposStub       GeneralreposStub
     */
    public Master(int masterId, int masterState, GeneralReposStub reposStub, ConcentrationSiteStub concetrationSiteStub, ControlCollectionSiteStub controlCollectionSiteStub, AssaultPartyStub[] partyStub) {
        this.masterState = masterState;
        this.masterId = masterId;
        this.partyStub = partyStub;
        this.concetrationSiteStub = concetrationSiteStub;
        this.controlCollectionSiteStub = controlCollectionSiteStub;
        this.reposStub = reposStub;
    }


    /**
     * Get master Id
     * 
     * @return master Id
     */
    public int getMasterId() {
        return masterId;
    }

    /**
     * Set master Id
     * 
     * @param masterId master Id
     */
    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    /**
     * Get master State
     * 
     * @return master state
     */
    public int getMasterState() {
        return masterState;
    }

    /**
     * Set master State
     * 
     * @param masterState master state
     */
    public void setMasterState(int masterState) {
        this.masterState = masterState;
    }

    /**
     * @return GeneralreposStub
     */
    public GeneralReposStub getReposStub() {
        return reposStub;
    }

    /**
     * Master thief life cycle
     */
    @Override
    public void run() {
        int room;
        controlCollectionSiteStub.startOperation();
        
        boolean heist = true;
        while (heist) {
            System.out.println("getRoomIdx");
            room = controlCollectionSiteStub.getRoomIdx();
            System.out.println("appraiseSit");
            switch (concetrationSiteStub.appraiseSit(room>=SimulConsts.N)){
                case 1:
                    int ap = concetrationSiteStub.getAssautlParty();
                    System.out.println("prepareAssaultParty");
                    concetrationSiteStub.prepareAssaultParty(ap, room);
                    System.out.println("sendAssaultParty");
                    partyStub[ap].sendAssaultParty(concetrationSiteStub.getRoom(ap));
                    break;

                case 2:
                    System.out.println("takeARest");
                    controlCollectionSiteStub.takeARest();
                    System.out.println("collectACanvas");
                    controlCollectionSiteStub.collectACanvas();
                    break;

                case 3:
                    System.out.println("sumUpResults");
                    concetrationSiteStub.sumUpResults();
                    heist = false;
                    break;
            }
        }

    }

}
