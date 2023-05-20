package serverSide.main;

import serverSide.entities.*;
import serverSide.sharedRegions.*;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;
import java.net.*;

/**
 * Server side of the Control and Collection site of Information.
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */

public class ServerControlCollectionSite {
    /**
     * Flag signaling the service is active (like the barbers).
     */

    public static boolean waitConnection;

    /**
     * Main method.
     *
     * @param args runtime arguments
     *             args[0] - Port number for listening to service requests
     *             args[1] - Name of the platform where is located the server for
     *             the general repository
     *             args[2] - Port number where the server for the general repository
     *             is listening to service requests
     */

    public static void main(String[] args) {
        GeneralReposStub GeneralRepositoryStub; // Stub to the general repository
        ControlCollectionSiteInterface ControlCollectionSiteInter; // Interface to the ControlCollectionSite
        ControlCollectionSite ControlCollection_Site; // ControlCollectionSite (service to be rendered)
        String GeneralRepositoryServerName; // Name of the platform where is located the server for the general repository
        int portNumb = -1; // Port number for listening to service requests
        int reposPortNumb = -1; // Port number where the server for the general repository is listening to service requests
        ServerCom scon, sconi; // Communication channels

        if (args.length != 3) {
            GenericIO.writelnString("Wrong number of parameters!");
            System.exit(1);
        }
        try {
            portNumb = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[0] is not a number!");
            System.exit(1);
        }
        if ((portNumb < 4000) || (portNumb >= 65536)) {
            GenericIO.writelnString("args[0] is not a valid port number!");
            System.exit(1);
        }
        GeneralRepositoryServerName = args[1];
        try {
            reposPortNumb = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[2] is not a number!");
            System.exit(1);
        }
        if ((reposPortNumb < 4000) || (reposPortNumb >= 65536)) {
            GenericIO.writelnString("args[2] is not a valid port number!");
            System.exit(1);
        }

        /* Service setup and initialization */

        GeneralRepositoryStub = new GeneralReposStub(GeneralRepositoryServerName, reposPortNumb); // Communication with the general repository is instantiated
        ControlCollection_Site = new ControlCollectionSite(GeneralRepositoryStub); // Service is instantiated
        ControlCollectionSiteInter = new ControlCollectionSiteInterface(ControlCollection_Site); // Interface for the service is instantiated
        scon = new ServerCom(portNumb); // Listening channel at the public port is established
        scon.start();
        GenericIO.writelnString("Service is established and listening for service requests.");

        /* Processing incoming service requests */

        ControlCollectionSiteClientProxy cliProxy; // Service provider agent

        waitConnection = true;
        while (waitConnection) {
            try {
                sconi = scon.accept(); // Listening
                cliProxy = new ControlCollectionSiteClientProxy(sconi, ControlCollectionSiteInter);
                cliProxy.start();
            } catch (SocketTimeoutException e) {
            }
        }
        scon.end(); //End of the operations
        GenericIO.writelnString("Server shutdown.");
    }
}