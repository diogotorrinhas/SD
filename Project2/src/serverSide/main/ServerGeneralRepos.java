package serverSide.main;

import serverSide.entities.*;
import serverSide.sharedRegions.*;
import commInfra.*;
import genclass.GenericIO;
import java.net.*;

/**
 * Server side of the General Repository of Information.
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */

public class ServerGeneralRepos {
    /**
     * Flag signaling the service is active (like the barbers).
     */

    public static boolean waitConnection;

    /**
     * Main method.
     *
     * @param args runtime arguments
     *             args[0] - port number for listening to service requests
     */

    public static void main(String[] args) {
        GeneralRepos GeneralRepository; // General Repository (service to be rendered)
        GeneralReposInterface GeneralRepositoryInter; // Interface to the General Repository
        int portNumb = -1; // Port number for listening to service requests
        ServerCom scon, sconi; // Communication channels

        if (args.length != 1) {
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

        /* Service setup and initialization */

        GeneralRepository = new GeneralRepos(); // Service is instantiated
        GeneralRepositoryInter = new GeneralReposInterface(GeneralRepository); // Interface for the service is instantiated
        scon = new ServerCom(portNumb); // Listening channel at the public port is established
        System.out.println("CHECKPOINT111111111");
        scon.start();
        GenericIO.writelnString("Service is established and listening for service requests.");

        /* Processing incoming service requests */

        GeneralReposClientProxy cliProxy; // Service provider agent

        waitConnection = true;
        while (waitConnection) {
            try {
                sconi = scon.accept(); // Listening
                cliProxy = new GeneralReposClientProxy(sconi, GeneralRepositoryInter);
                cliProxy.start();
            } catch (SocketTimeoutException e) {
            }
        }
        scon.end(); // End of the operations
        GenericIO.writelnString("Server shutdown.");
    }
}