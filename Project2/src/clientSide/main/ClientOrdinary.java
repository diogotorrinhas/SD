package clientSide.main;

import clientSide.entities.*;
import clientSide.stubs.*;
import serverSide.main.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *    Client side of the Museum Heist (Ordinary Thief).
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class ClientOrdinary {
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
     *        args[8] - name of the platform where is located the museum server
     *        args[9] - port number for listening to service requests
     *        args[10] - name of the platform where is located the GeneralRepository server
     *        args[11] - port number for listening to service requests
     */

     public static void main (String [] args)
     {
         String GenReposServerHostName;
         int GenReposServerPortNumb = -1;

         String ConcentrationSiteServerHostName;
         int ConcentrationSiteServerPortNumb = -1;

         String ControlCollectionSiteServerHostName;
         int ControlCollectionSiteServerPortNumb = -1;

         String museumServerHostName;
         int museumServerPortNumb = -1;

         String AssaultPartyServerHostName0;
         int AssaultPartyServerPortNumb0 = -1;
         String AssaultPartyServerHostName1;
         int AssaultPartyServerPortNumb1 = -1;

         AssaultPartyStub[] assaultPartyStub = new AssaultPartyStub[2];
         MuseumStub museumStub;
         ConcentrationSiteStub concentrationSiteStub;
         ControlCollectionSiteStub controlCollectionSiteStub;
         GeneralReposStub GenReposStub;

         /* getting problem runtime parameters */
 
         if (args.length != 12)
         { GenericIO.writelnString ("Wrong number of parameters!");
             System.exit (1);
         }

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

         museumServerHostName = args[8];
         try
         { museumServerPortNumb = Integer.parseInt (args[9]);
         }
         catch (NumberFormatException e)
         { GenericIO.writelnString ("args[9] is not a number!");
             System.exit (1);
         }
         if ((museumServerPortNumb < 4000) || (museumServerPortNumb >= 65536))
         { GenericIO.writelnString ("args[9] is not a valid port number!");
             System.exit (1);
         }

         GenReposServerHostName = args[10];
         try
         { GenReposServerPortNumb = Integer.parseInt (args[11]);
         }
         catch (NumberFormatException e)
         { GenericIO.writelnString ("args[11] is not a number!");
             System.exit (1);
         }
         if ((GenReposServerPortNumb < 4000) || (GenReposServerPortNumb >= 65536))
         { GenericIO.writelnString ("args[11] is not a valid port number!");
             System.exit (1);
         }

         /* problem initialization */
         assaultPartyStub[0] = new AssaultPartyStub (AssaultPartyServerHostName0, AssaultPartyServerPortNumb0);
         assaultPartyStub[1] = new AssaultPartyStub (AssaultPartyServerHostName1, AssaultPartyServerPortNumb1);
         concentrationSiteStub = new ConcentrationSiteStub (ConcentrationSiteServerHostName, ConcentrationSiteServerPortNumb);
         controlCollectionSiteStub = new ControlCollectionSiteStub (ControlCollectionSiteServerHostName, ControlCollectionSiteServerPortNumb);
         museumStub = new MuseumStub (museumServerHostName, museumServerPortNumb);
         GenReposStub = new GeneralReposStub (GenReposServerHostName, GenReposServerPortNumb);

         Ordinary[] ordinaryThieves = new Ordinary[SimulConsts.M-1];
         for (int i = 0; i < SimulConsts.M-1; i++)
             ordinaryThieves[i] = new Ordinary(i, OrdinaryStates.CONCENTRATION_SITE, GenReposStub, concentrationSiteStub, controlCollectionSiteStub, assaultPartyStub, museumStub);
 
         /* start of the simulation */

         for (int i = 0; i < SimulConsts.M-1; i++)
             ordinaryThieves[i].start ();
 
         /* waiting for the end of the simulation */
 
         GenericIO.writelnString ();
 
         try {
            for (int i = 0; i < SimulConsts.M-1; i++)
                ordinaryThieves[i].join();
         } catch (InterruptedException e) {}
         GenericIO.writelnString ("The ordinaries has terminated.");
 
         GenericIO.writelnString ();
         assaultPartyStub[0].shutdown ();
         assaultPartyStub[1].shutdown ();
         concentrationSiteStub.shutdown ();
         controlCollectionSiteStub.shutdown ();
         museumStub.shutdown ();
         GenReposStub.shutdown ();
     }
 }

