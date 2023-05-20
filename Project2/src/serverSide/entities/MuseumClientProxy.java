package serverSide.entities;

import serverSide.sharedRegions.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 * Service provider agent for access to the Barber Shop.
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */

public class MuseumClientProxy extends Thread implements OrdinaryCloning {
    /**
     * Number of instantiayed threads.
     */

    private static int nProxy = 0;

    /**
     * Communication channel.
     */

    private ServerCom sconi;

    /**
     * Ordinary identification
     */
    private int OrdinaryID;

    /**
     * Ordinary state.
     */

    private int OrdinaryState;

    /**
     * Museum interface.
     */

    private MuseumInterface MuseumInter;

    /**
     * Instantiation of a client proxy.
     *
     * @param sconi   communication channel
     * @param MuseumInter interface to the barber shop
     */

    public MuseumClientProxy(ServerCom sconi, MuseumInterface MuseumInter) {
        super("MuseumClientProxy" + MuseumClientProxy.getProxyId());
        this.sconi = sconi;
        this.MuseumInter = MuseumInter;
    }

    /**
     * Generation of the instantiation identifier.
     *
     * @return instantiation identifier
     */

    private static int getProxyId() {
        Class<?> cl = null; // representation of the MuseumClientProxy object in JVM
        int proxyId; // instantiation identifier

        try {
            cl = Class.forName("serverSide.entities.MuseumClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("Data type MuseumClientProxy was not found!");
            e.printStackTrace();
            System.exit(1);
        }
        synchronized (cl) {
            proxyId = nProxy;
            nProxy += 1;
        }
        return proxyId;
    }

    /**
     * Life cycle of the service provider agent.
     */

     @Override
    public void run() {
        Message inMessage = null, // service request
                outMessage = null; // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject(); // get service request
        try {
            outMessage = MuseumInter.processAndReply(inMessage); // process it
        } catch (MessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage); // send service reply
        sconi.close(); // close the communication channel
    }


    /**
     * Getter ordinary state
     * @return get ordinary state
     */

    public int getOrdinaryState() {
        return OrdinaryState;
    }

    /**
     * Setter ordinary state
     * @param ordinaryState thieve state
     */

    public void setOrdinaryState(int ordinaryState) {
        OrdinaryState = ordinaryState;
    }

    /**
     * Getter ordinary id
     * @return get ordinary id
     */
    
    public int getOrdinaryId() {
        return OrdinaryID;
    }

    /**
     * Setter ordinary id
     * @param ordinaryId thieve id
     */
    
    public void setOrdinaryId(int ordinaryId) {
        OrdinaryID = ordinaryId;
    }

}