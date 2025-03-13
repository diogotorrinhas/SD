package clientSide.entities;

import java.rmi.*;
import interfaces.*;
import genclass.GenericIO;


/**
 *    Ordinary thread.
 *
 *      It simulates the Ordinary life cycle.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on remote calls under Java RMI.
 */
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
    private final AssaultPartyInterface[] partyStub;

    /**
     * Concentration Site
     */
    private final ConcentrationSiteInterface csStub;

    /**
     * ´Control Collection Site
     */
    private final ControlCollectionSiteInterface ccsStub;

    /**
     * Museum
     */
    private final MuseumInterface museumStub;

    /**
     * General Repository
     */
    private final GeneralReposInterface reposStub;

    /**
     * Instantiation of a ordinary thread.
     * 
     * @param name          thread name
     * @param ordinaryId    ordinary Id
     * @param partyStub     Reference to assault party
     * @param csStub        Reference to concentration site
     * @param ccsStub       Reference to control collection site
     * @param museumStub    Reference to museum
     * @param reposStub     Reference to GeneralRepos
     */
    public Ordinary(String name, int ordinaryId, AssaultPartyInterface[] partyStub, ConcentrationSiteInterface csStub, ControlCollectionSiteInterface ccsStub, MuseumInterface museumStub, GeneralReposInterface reposStub) {
        super(name);
        this.ordinaryState = OrdinaryStates.CONCENTRATION_SITE;
        this.ordinaryId = ordinaryId;
        this.partyStub = partyStub;
        this.csStub = csStub;
        this.ccsStub = ccsStub;
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
     * Get GeneralRepos.
     * @return GeneralRepos
     */
    public GeneralReposInterface getRepos() {
        return reposStub;
    }


    /**
     * Get ordinary md
     * @param ordinaryId ordinary id
     * @return ordinary md
     *
     */
    private int getOrdinaryMD(int ordinaryId){
        int ret = 0;

        try {
            ret = reposStub.getOrdinariesMD(ordinaryId);
        } catch (RemoteException e)
        { GenericIO.writelnString ("ordinary " + ordinaryId + " remote exception on callACustomer: " + e.getMessage ());
            System.exit (1);
        }
        return ret;
    }

    /**
     * AmINeed operation.
     * @param ap assault party id
     * @return ordinary necessary
     */
    private boolean amINeeded(int ap){
        ReturnBoolean ret = null;                                 // return value

        try {
            ret = csStub.amINeeded( ap, ordinaryId);
        } catch (RemoteException e)
        { GenericIO.writelnString ("ordinary " + ordinaryId + " remote exception on goToSleep: " + e.getMessage ());
            System.exit (1);
        }
        ordinaryState = ret.getIntStateVal ();
        return ret.getBooleanVal ();
    }

    /**
     * Prepare Excursion operation.
     * @return assault party
     */
    private int prepareExcursion(){
        ReturnInt ret = null;                                // return value

        try {
            ret = csStub.prepareExcursion(ordinaryId);
        } catch (RemoteException e)
        { GenericIO.writelnString ("ordinary " + ordinaryId + " remote exception on callACustomer: " + e.getMessage ());
            System.exit (1);
        }
        ordinaryState = ret.getIntStateVal ();
        return ret.getIntVal ();
    }

    /**
     * Assign a member to a party.
     * @param ap assault party
     * @return member id
     */
    private int assignMember(int ap){
        int ret = 0;

        try {
            ret = partyStub[ap].assignMember(ap, ordinaryId);
        } catch (RemoteException e)
        { GenericIO.writelnString ("ordinary " + ordinaryId + " remote exception on callACustomer: " + e.getMessage ());
            System.exit (1);
        }
        return ret;
    }

    /**
     * CrawlIn movement.
     * @param ap assault party
     * @param memberId party id
     * @param md maximum distance
     * @return crawling done
     */
    private boolean crawlIn(int ap, int memberId, int md){
        ReturnBoolean ret = null;                                 // return value

        try {
            ret = partyStub[ap].crawlIn( ap, memberId, md, ordinaryId);
        } catch (RemoteException e)
        { GenericIO.writelnString ("ordinary " + ordinaryId + " remote exception on goToSleep: " + e.getMessage ());
            System.exit (1);
        }
        ordinaryState = ret.getIntStateVal ();
        return ret.getBooleanVal ();
    }

    /**
     * Get target room ID.
     * @param ap assault party
     * @return ap room
     */
    private int getRoomAP(int ap){
        int ret = 0;

        try {
            ret = partyStub[ap].getRoom();
        } catch (RemoteException e)
        { GenericIO.writelnString ("ordinary " + ordinaryId + " remote exception on callACustomer: " + e.getMessage ());
            System.exit (1);
        }
        return ret;
    }

    /**
     * RollACanvas.
     * @param room room id
     * @param ap assault party
     * @param memberId party id
     * @return canvas
     */
    private int rollACanvas(int room, int ap, int memberId){
        int ret = 0;                                // return value

        try {
            ret = museumStub.rollACanvas(room, ap, memberId);
        } catch (RemoteException e)
        { GenericIO.writelnString ("ordinary " + ordinaryId + " remote exception on callACustomer: " + e.getMessage ());
            System.exit (1);
        }
        return ret;
    }

    /**
     * Reverse direction.
     * @param memberId party id
     * @param ap assault party
     */
    private void reverseDirection(int memberId, int ap){
        try {
            ordinaryState = partyStub[ap].reverseDirection( memberId, ordinaryId);
        } catch (RemoteException e)
        { GenericIO.writelnString ("ordinary " + ordinaryId + " remote exception on receivePayment: " + e.getMessage ());
            System.exit (1);
        }
    }

    /**
     * CrawlOut movement.
     * @param ap assault party
     * @param memberId party id
     * @param md ordinary maximum distance
     * @return crawl out done
     */
    private boolean crawlOut(int ap, int memberId, int md){
        ReturnBoolean ret = null;                                 // return value

        try {
            ret = partyStub[ap].crawlOut( ap, memberId, md, ordinaryId);
        } catch (RemoteException e)
        { GenericIO.writelnString ("ordinary " + ordinaryId + " remote exception on goToSleep: " + e.getMessage ());
            System.exit (1);
        }
        ordinaryState = ret.getIntStateVal ();
        return ret.getBooleanVal ();
    }

    /**
     * Get room assign to the assault party
     * @param ap assault party
     * @return room id
     */
    private int getRoomCS(int ap){
        int ret = 0;

        try {
            ret = csStub.getRoom(ap);
        } catch (RemoteException e)
        { GenericIO.writelnString ("ordinary " + ordinaryId + " remote exception on callACustomer: " + e.getMessage ());
            System.exit (1);
        }
        return ret;
    }


    /**
     * Ordinary hand a canvas to master
     * @param canvas in thief hand
     * @param room to heist
     * @param ap thief party
     * @param memberId thief party id
     */
    private void handACanvas(int canvas, int room, int ap, int memberId){
        try {
            ccsStub.handACanvas(canvas, room, ap, memberId);
        } catch (RemoteException e)
        { GenericIO.writelnString ("ordinary " + ordinaryId + " remote exception on receivePayment: " + e.getMessage ());
            System.exit (1);
        }
    }

    /**
     * Ordinary thief life cycle.
     */
    @Override
    public void run() {
        int memberId, room, canvas, ap = -1;
        System.out.println("getOrdinaryMD");
        int md = getOrdinaryMD(ordinaryId);
        System.out.println("amINeeded");
        while (amINeeded(ap)) {
            System.out.println("prepareExcursion");
            ap = prepareExcursion();
            System.out.println("assignMember");
            memberId = assignMember(ap);
            boolean atRoom = true;
            while (atRoom){
                System.out.println("crawlIn");
                atRoom = crawlIn(ap, memberId, md);
            }

            System.out.println("getRoom");
            room = getRoomAP(ap);
            System.out.println("rollACanvas");
            canvas = rollACanvas(room, ap, memberId);
            System.out.println("assignMember");
            memberId = assignMember(ap);
            System.out.println("reverseDirection");
            reverseDirection(memberId, ap);
            boolean atSite = true;
            while (atSite){
                System.out.println("crawlOut");
                atSite = crawlOut(ap, memberId, md);
            }   
            System.out.println("handACanvas");
            handACanvas(canvas, getRoomCS(ap), ap, memberId);
        }
    }
}