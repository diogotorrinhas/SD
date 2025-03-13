package clientSide.entities;

import java.rmi.*;
import interfaces.*;
import genclass.GenericIO;
import serverSide.main.SimulConsts;

/**
 *    Master thread.
 *
 *      It simulates the Master life cycle.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on remote calls under Java RMI.
 */
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
    private final AssaultPartyInterface[] partyStub;

    /**
     * ConcentrationSite
     */
    private final ConcentrationSiteInterface csStub;

    /**
     * Control Collection Site
     */
    private final ControlCollectionSiteInterface ccsStub;

    /**
     * Instantiation of a master thread.
     * 
     * @param name            thread name
     * @param masterId        Master Id
     * @param partyStub       Reference to AssaultpartyStub
     * @param csStub          Reference to ConcentrationSite
     * @param ccsStub         Reference to ControlCollectionSite
     */
    public Master(String name, int masterId, AssaultPartyInterface[] partyStub, ConcentrationSiteInterface csStub, ControlCollectionSiteInterface ccsStub) {
        super(name);
        this.masterState = MasterStates.PLANNING_THE_HEIST;
        this.masterId = masterId;
        this.partyStub = partyStub;
        this.csStub = csStub;
        this.ccsStub = ccsStub;
    }


    /**
     * Master starts operation
     */
    private void startOperation(){
        try { masterState = ccsStub.startOperation();
        } catch (RemoteException e)
        { GenericIO.writelnString ("master " + masterId + " remote exception on receivePayment: " + e.getMessage ());
            System.exit (1);
        }
    }


    /**
     *  Get room idx.
     *
     *  Remote operation.
     *
     *     @return room idx
     */
    private int getRoomIdx() {
        int ret = 0;;

        try {
            ret = ccsStub.getRoomIdx();
        } catch (RemoteException e) {
            GenericIO.writelnString ("master " + masterId + " remote exception on callACustomer: " + e.getMessage ());
            System.exit (1);
        }

        return ret;
    }


    /**
     * Master appraise situation.
     *
     * @param roomstt museum rooms state
     * @return master decision
     */
    private int appraiseSit(boolean roomstt) {
        int ret = 0;;

        try {
            ret = csStub.appraiseSit(roomstt);
        } catch (RemoteException e) {
            GenericIO.writelnString ("master " + masterId + " remote exception on callACustomer: " + e.getMessage ());
            System.exit (1);
        }
        return ret;
    }


    /**
     *  Get assault party.
     *
     *  Remote operation.
     *
     *     @return assault party id
     */
    private int getAssautlParty() {
        int ret = 0;;

        try {
            ret = csStub.getAssautlParty();
        } catch (RemoteException e) {
            GenericIO.writelnString ("master " + masterId + " remote exception on callACustomer: " + e.getMessage ());
            System.exit (1);
        }
        return ret;
    }


    /**
     *  Master prepare an assault party.
     *
     *  @param ap Assault party id
     *  @param room Room number
     *  Remote operation.
     */
    private void prepareAssaultParty(int ap, int room) {
        try {
            masterState = csStub.prepareAssaultParty(ap, room);
        } catch (RemoteException e) {
            GenericIO.writelnString ("master " + masterId + " remote exception on receivePayment: " + e.getMessage ());
            System.exit (1);
        }
    }


    /**
     *  Get room.
     *
     *  @param ap Assault party id
     *  Remote operation.
     *
     *     @return room id
     */
    private int getRoom(int ap){
        int ret = 0;;

        try {
            ret = csStub.getRoom(ap);
        } catch (RemoteException e) {
            GenericIO.writelnString ("master " + masterId + " remote exception on callACustomer: " + e.getMessage ());
            System.exit (1);
        }
        return ret;
    }


    /**
     * Master send an assault party.
     *
     * @param gtcs room
     * @param ap assault party
     */
    private void sendAssaultParty(int gtcs, int ap) {
        try {
            masterState = partyStub[ap].sendAssaultParty(gtcs);
        } catch (RemoteException e) {
            GenericIO.writelnString ("master " + masterId + " remote exception on receivePayment: " + e.getMessage ());
            System.exit (1);
        }
    }


    /**
     *  Master take a rest while waiting for ordinaries.
     *
     *  Remote operation.
     */
    private void takeARest(){
        try {
            masterState = ccsStub.takeARest();
        } catch (RemoteException e) {
            GenericIO.writelnString ("master " + masterId + " remote exception on receivePayment: " + e.getMessage ());
            System.exit (1);
        }
    }


    /**
     * Master collect a canvas from ordinaries.
     *
     * Remote operation.
     */
    private void collectACanvas(){
        try {
            masterState = ccsStub.collectACanvas();
        } catch (RemoteException e) {
            GenericIO.writelnString ("master " + masterId + " remote exception on receivePayment: " + e.getMessage ());
            System.exit (1);
        }
    }


    /**
     *  Master sum up the results.
     *
     *  Remote operation.
     */
    private void sumUpResults(){
        try {
            masterState = csStub.sumUpResults();
        } catch (RemoteException e) {
            GenericIO.writelnString ("master " + masterId + " remote exception on receivePayment: " + e.getMessage ());
            System.exit (1);
        }
    }

    /**
     * Master thief life cycle.
     */
    @Override
    public void run() {
        int room;
        startOperation();
        
        boolean assault = true;
        while (assault) {
            System.out.println("getRoomIdx");
            room = getRoomIdx();
            System.out.println("appraiseSit");
            switch (appraiseSit(room>=SimulConsts.N)){
                case 1:
                    System.out.println("getAssautlParty");
                    int ap = getAssautlParty();
                    System.out.println("master gap = "+ap);
                    System.out.println("prepareAssaultParty");
                    prepareAssaultParty(ap, room);
                    System.out.println("sendAssaultParty");
                    sendAssaultParty(getRoom(ap), ap);
                    break;

                case 2:
                    System.out.println("takeARest");
                    takeARest();
                    System.out.println("collectACanvas");
                    collectACanvas();
                    break;

                case 3:
                    System.out.println("sumUpResults");
                    sumUpResults();
                    assault = false;
                    break;
            }
        }
    }
}