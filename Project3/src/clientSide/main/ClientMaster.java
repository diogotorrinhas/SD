package clientSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import clientSide.entities.*;
import serverSide.main.*;
import interfaces.*;
import genclass.GenericIO;


/**
 *    Client side of Master Thief.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */
public class ClientMaster {
    /**
   *  Main method.
   *
   *    @param args runtime arguments
   *        args[0] - name of the platform where is located the RMI registering service
   *        args[1] - port number where the registering service is listening to service requests
   */
   public static void main (String [] args)
   {
    String rmiRegHostName;                                         // name of the platform where is located the RMI registering service
    int rmiRegPortNumb = -1;                                       // port number where the registering service is listening to service requests

   /* getting problem runtime parameters */

    if (args.length != 2)
       { GenericIO.writelnString ("Wrong number of parameters!");
         System.exit (1);
       }
    rmiRegHostName = args[0];
    try
    { rmiRegPortNumb = Integer.parseInt (args[1]);
    }
    catch (NumberFormatException e)
    { GenericIO.writelnString ("args[1] is not a number!");
      System.exit (1);
    }
    if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536))
       { GenericIO.writelnString ("args[1] is not a valid port number!");
         System.exit (1);
       }
    

   /* problem initialization */

    String nameEntryGeneralRepos = "GeneralRepository";               // public name of the general repository object
    GeneralReposInterface reposStub = null;                           // remote reference to the general repository object

    String nameEntryAP0 = "AssaultParty0";                            // public name of the assault party0 object
    String nameEntryAP1 = "AssaultParty1";                            // public name of the assault party1 object
    AssaultPartyInterface[] apStub = new AssaultPartyInterface[2];    // remote reference to the assault party object

    String nameEntryCS= "ConcentrationSite";                          // public name of the concentration site object
    ConcentrationSiteInterface csStub = null;                         // remote reference to the concentration site object

    String nameEntryCCS = "ControlCollectionSite";                    // public name of the Collection Site object
    ControlCollectionSiteInterface ccsStub = null;                    // remote reference to the Collection Site object

    Registry registry = null;                                         // remote reference for registration in the RMI registry service

    try
    { registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }

    try
    { reposStub = (GeneralReposInterface) registry.lookup (nameEntryGeneralRepos);
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("GeneralRepos lookup exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }
    catch (NotBoundException e)
    { GenericIO.writelnString ("GeneralRepos not bound exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }

    try
    { apStub[0] = (AssaultPartyInterface) registry.lookup (nameEntryAP0);
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("AssaultParty0 lookup exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }
    catch (NotBoundException e)
    { GenericIO.writelnString ("AssaultParty0 not bound exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }

    try
    { apStub[1] = (AssaultPartyInterface) registry.lookup (nameEntryAP1);
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("AssaultParty1 lookup exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }
    catch (NotBoundException e)
    { GenericIO.writelnString ("AssaultParty1 not bound exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }

    try
    { csStub = (ConcentrationSiteInterface) registry.lookup (nameEntryCS);
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("ConcentrationSite lookup exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }
    catch (NotBoundException e)
    { GenericIO.writelnString ("ConcentrationSite not bound exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }

    try
    { ccsStub = (ControlCollectionSiteInterface) registry.lookup (nameEntryCCS);
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("ControlCollectionSite lookup exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }
    catch (NotBoundException e)
    { GenericIO.writelnString ("ControlCollectionSite not bound exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }

    Master master = null;                                             // master thread
    master = new Master("master", 0, apStub, csStub, ccsStub);

   /* start of the simulation */

    master.start ();

   /* waiting for the end of the simulation */

    { try
      { master.join ();
      }
      catch (InterruptedException e) {}
      GenericIO.writelnString ("The master has terminated.");
    }
    GenericIO.writelnString ();

    try
    { apStub[0].shutdown ();
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("master generator remote exception on AssaultParty0 shutdown: " + e.getMessage ());
      System.exit (1);
    }
    try
    { apStub[1].shutdown ();
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("master generator remote exception on AssaultParty1 shutdown: " + e.getMessage ());
      System.exit (1);
    }

    try
    { csStub.shutdown ();
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("master generator remote exception on ConcentrationSite shutdown: " + e.getMessage ());
      System.exit (1);
    }

    try
    { ccsStub.shutdown ();
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("master generator remote exception on ControlCollectionSite shutdown: " + e.getMessage ());
      System.exit (1);
    }
    
    try
    { reposStub.shutdown ();
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("master generator remote exception on GeneralRepos shutdown: " + e.getMessage ());
      System.exit (1);
    }
 }
}
