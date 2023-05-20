
package serverSide.main;

import serverSide.entities.*;
import serverSide.sharedRegions.*;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;
import java.net.*;

/**
 * Server side of the Assault Party of Information.
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */

public class ServerAssaultParty {
    /**
     * Flag signaling the service is active (like the barbers).
     */
    public static boolean waitConnection;

    /**
     * Main method.
     * @param args runtime arguments
     *             args[0] - Port number for listening to service requests
     *             args[1] - Name of the platform where is located the server for the general repository
     *             args[2] - Port number where the server for the general repository is listening to service requests
     */

    public static void main(String[] args) {
        GeneralReposStub GenRepositoryStub;     // Stub to the general repository
        AssaultPartyInterface AssaultPartyInter;  // Interface to the Assault Party
        AssaultParty Assault_Party;                // Assault Party (Service to be rendered)
        String GeneralRepositoryServerName;         // Name of the platform where is located the server for the general repository
        int portNumber = -1;              // Port number for listening to service requests
        int reposPortNumber = -1;         // Port number where the server for the general repository is listening to service requests
        ServerCom scon, sconi;          // Communication channels

        if (args.length != 3) {
            GenericIO.writelnString("Wrong number of parameters!");
            System.exit(1);
        }
        try {
            portNumber = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[0] is not a number!");
            System.exit(1);
        }
        if ((portNumber < 4000) || (portNumber >= 65536)) {
            GenericIO.writelnString("args[0] is not a valid port number!");
            System.exit(1);
        }
        GeneralRepositoryServerName = args[1];
        try {
            reposPortNumber = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[2] is not a number!");
            System.exit(1);
        }
        if ((reposPortNumber < 4000) || (reposPortNumber >= 65536)) {
            GenericIO.writelnString("args[2] is not a valid port number!");
            System.exit(1);
        }

        /* Service setup and initialization */

        //System.out.println("ESTOU AQUI!");
        GenRepositoryStub = new GeneralReposStub(GeneralRepositoryServerName, reposPortNumber); // Communication with the general repository is instantiated
        Assault_Party = new AssaultParty(GenRepositoryStub); // Service is instantiated
        AssaultPartyInter = new AssaultPartyInterface(Assault_Party); // Interface for the service is instantiated
        scon = new ServerCom(portNumber); // Listening channel at the public port is established
        //System.out.println("CHECKPOINTTTTT!");
        scon.start();
        //System.out.println("ESTOU AQUI222222222!");
        GenericIO.writelnString("Service is established and listening for service requests.");

        /* Processing incoming service requests */

        AssaultPartyClientProxy cliProxy; // Service provider agent

        waitConnection = true;
        while (waitConnection) {
            try {
                sconi = scon.accept(); // Listening
                //System.out.println("ESTOU AQUI333333333!");
                cliProxy = new AssaultPartyClientProxy(sconi, AssaultPartyInter);
                cliProxy.start();
                //System.out.println("ESTOU AQUI444444444!");
            } catch (SocketTimeoutException e) {
            }
        }
        System.out.println("ENDDDDDD!");
        scon.end(); //End of the operations
        GenericIO.writelnString("Server shutdown!");
    }
}