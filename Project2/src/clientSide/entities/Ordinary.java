package clientSide.entities;

import clientSide.stubs.*;

public class Ordinary extends Thread {

    /**
     * Ordinary Id
     */
    private int ordinaryId;

    /**
     * Ordinary State
     */
    private int ordinaryState;

    /**
     * Assault Party
     */
    private final AssaultPartyStub[] partyStub;

    /**
     * Concentration Site
     */
    private final ConcentrationSiteStub concentrationSiteStub;

    /**
     * ControlCollectionSite
     */
    private final ControlCollectionSiteStub controlCollectionSiteStub;

    /**
     * Museum
     */
    private final MuseumStub museumStub;

    /**
     * generalRepository
     */
    private final GeneralReposStub reposStub;

    /**
     * Instantiation of a ordinary thread.
     * @param ordinaryId    ordinary Id
     * @param ordinaryState ordinary state
     * @param partyStub       AssaultpartyStub
     * @param concentrationSiteStub          ConcentrationSite
     * @param controlCollectionSiteStub         ControlCollectionSite
     * @param museumStub      Museum
     * @param reposStub       GeneralreposStub
     */
    public Ordinary(int ordinaryId, int ordinaryState, GeneralReposStub reposStub, ConcentrationSiteStub concentrationSiteStub, ControlCollectionSiteStub controlCollectionSiteStub, AssaultPartyStub[] partyStub, MuseumStub museumStub) {
        this.ordinaryState = ordinaryState;
        this.ordinaryId = ordinaryId;
        this.partyStub = partyStub;
        this.concentrationSiteStub = concentrationSiteStub;
        this.controlCollectionSiteStub = controlCollectionSiteStub;
        this.museumStub = museumStub;
        this.reposStub = reposStub;
    }


    /**
     * Get ordinary Id
     * 
     * @return ordinary Id
     */
    public int getOrdinaryId() {
        return ordinaryId;
    }

    /**
     * Set ordinary Id
     * 
     * @param ordinaryId ordinary Id
     */
    public void setOrdinaryId(int ordinaryId) {
        this.ordinaryId = ordinaryId;
    }

    /**
     * Get ordinary State
     * 
     * @return ordinary state
     */
    public int getOrdinaryState() {
        return ordinaryState;
    }

    /**
     * Set ordinary State
     * 
     * @param ordinaryState ordinary state
     */
    public void setOrdinaryState(int ordinaryState) {
        this.ordinaryState = ordinaryState;
    }

    /**
     * @return GeneralRepos
     */
    public GeneralReposStub getRepos() {
        return reposStub;
    }

    /**
     * Ordinary thief life cycle
     */
    @Override
    public void run() {
        int memberId, room, canvas, ap = -1;
        int md = reposStub.getOrdinaryMD(ordinaryId);
        System.out.println("amINeeded");
        while (concentrationSiteStub.amINeeded(ap)) {
            System.out.println("prepareExcursion");
            ap = concentrationSiteStub.prepareExcursion();
            memberId = partyStub[ap].assignMember(ap);
            boolean atRoom = true;
            while (atRoom){
                System.out.println("crawlIn");
                atRoom = partyStub[ap].crawlIn(ap, memberId, md);
            }
            room = partyStub[ap].getRoom();
            System.out.println("rollACanvas");
            canvas = museumStub.rollACanvas(room, ap, memberId);
            memberId = partyStub[ap].assignMember(ap);
            System.out.println("reverseDirection");
            partyStub[ap].reverseDirection(memberId);
            boolean atSite = true;
            while (atSite){
                System.out.println("crawlOut");
                atSite = partyStub[ap].crawlOut(ap, memberId, md);
            }   
            System.out.println("handACanvas");
            controlCollectionSiteStub.handACanvas(canvas, concentrationSiteStub.getRoom(ap), ap, memberId);
        }
    }

}
