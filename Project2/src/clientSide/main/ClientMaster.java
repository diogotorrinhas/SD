package clientSide.main;

import clientSide.entities.*;
import clientSide.stubs.*;
import genclass.GenericIO;

/**
 *    Client side of the Museum Heist (Master Thief).
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class ClientMaster {
    /**
     *  Main method.
     *
     *    @param args runtime arguments
     *        args[0] - name of the platform where is located the AssaultParty0 server
     *        args[1] - port number for listening to service requests
     *        args[2] - name of the platform where is located the AssaultParty1 server
     *        args[3] - port number for listening to service requests
     *        args[4] - name of the platform where is located the ConcentrationSite server
     *        args[5] - port number for listening to service requests
     *        args[6] - name of the platform where is located the ControlCollectionSite server
     *        args[7] - port number for listening to service requests
     *        args[8] - name of the platform where is located the GeneralRepository server
     *        args[9] - port number for listening to service requests
     */

     public static void main (String [] args)
     {
         String GenReposServerHostName;
         int GenReposServerPortNumb = -1;
         String ConcentrationSiteServerHostName;
         int ConcentrationSiteServerPortNumb = -1;
         String ControlCollectionSiteServerHostName;
         int ControlCollectionSiteServerPortNumb = -1;

         String AssaultPartyServerHostName0;
         int AssaultPartyServerPortNumb0 = -1;
         String AssaultPartyServerHostName1;
         int AssaultPartyServerPortNumb1 = -1;

         ControlCollectionSiteStub ControllCollectionSiteStub;
         ConcentrationSiteStub ConcentrationSiteStub;
         AssaultPartyStub[] AssaultPartyStub = new AssaultPartyStub[2];
         GeneralReposStub genReposStub;


         /* getting problem runtime parameters */
 
         if (args.length != 10)
         { GenericIO.writelnString ("Wrong number of parameters!");
             System.exit (1);
         }

         //AssaultPartyServerHostName[0] = args[0];
         AssaultPartyServerHostName0 = args[0];
         try
         { AssaultPartyServerPortNumb0 = Integer.parseInt (args[1]);
         }
         catch (NumberFormatException e)
         { GenericIO.writelnString ("args[1] is not a number!");
             System.exit (1);
         }
         if ((AssaultPartyServerPortNumb0 < 4000) || (AssaultPartyServerPortNumb0 >= 65536))
         { GenericIO.writelnString ("args[1] is not a valid port number!");
             System.exit (1);
         }

         //AssaultPartyServerHostName[1] = args[2];
         AssaultPartyServerHostName1 = args[2];
         try
         { AssaultPartyServerPortNumb1 = Integer.parseInt (args[3]);
         }
         catch (NumberFormatException e)
         { GenericIO.writelnString ("args[3] is not a number!");
             System.exit (1);
         }
         if ((AssaultPartyServerPortNumb1 < 4000) || (AssaultPartyServerPortNumb1 >= 65536))
         { GenericIO.writelnString ("args[3] is not a valid port number!");
             System.exit (1);
         }

         ConcentrationSiteServerHostName = args[4];
         try
         { ConcentrationSiteServerPortNumb = Integer.parseInt (args[5]);
         }
         catch (NumberFormatException e)
         { GenericIO.writelnString ("args[5] is not a number!");
             System.exit (1);
         }
         if ((ConcentrationSiteServerPortNumb < 4000) || (ConcentrationSiteServerPortNumb >= 65536))
         { GenericIO.writelnString ("args[5] is not a valid port number!");
             System.exit (1);
         }

         ControlCollectionSiteServerHostName = args[6];
         try
         { ControlCollectionSiteServerPortNumb = Integer.parseInt (args[7]);
         }
         catch (NumberFormatException e)
         { GenericIO.writelnString ("args[7] is not a number!");
             System.exit (1);
         }
         if ((ControlCollectionSiteServerPortNumb < 4000) || (ControlCollectionSiteServerPortNumb >= 65536))
         { GenericIO.writelnString ("args[7] is not a valid port number!");
             System.exit (1);
         }

         GenReposServerHostName = args[8];
         try
         { GenReposServerPortNumb = Integer.parseInt (args[9]);
         }
         catch (NumberFormatException e)
         { GenericIO.writelnString ("args[9] is not a number!");
             System.exit (1);
         }
         if ((GenReposServerPortNumb < 4000) || (GenReposServerPortNumb >= 65536))
         { GenericIO.writelnString ("args[9] is not a valid port number!");
             System.exit (1);
         }
 
 
         /* problem initialization */
         AssaultPartyStub[0] = new AssaultPartyStub (AssaultPartyServerHostName0, AssaultPartyServerPortNumb0);
         AssaultPartyStub[1] = new AssaultPartyStub (AssaultPartyServerHostName1, AssaultPartyServerPortNumb1);
         ConcentrationSiteStub = new ConcentrationSiteStub (ConcentrationSiteServerHostName, ConcentrationSiteServerPortNumb);
         ControllCollectionSiteStub = new ControlCollectionSiteStub (ControlCollectionSiteServerHostName, ControlCollectionSiteServerPortNumb);

         genReposStub = new GeneralReposStub (GenReposServerHostName, GenReposServerPortNumb);
         Master master = new Master (0, MasterStates.PLANNING_THE_HEIST, genReposStub, ConcentrationSiteStub, ControllCollectionSiteStub, AssaultPartyStub);
 
         /* start of the simulation */
         master.start ();
 
         /* waiting for the end of the simulation */
 
         GenericIO.writelnString ();
 
         try {
             master.join();
         } catch (InterruptedException e) {}
         GenericIO.writelnString ("The master has terminated.");
         GenericIO.writelnString ();
         AssaultPartyStub[0].shutdown ();
         AssaultPartyStub[1].shutdown ();
         ConcentrationSiteStub.shutdown ();
         ControllCollectionSiteStub.shutdown ();
         genReposStub.shutdown ();
     }
}
