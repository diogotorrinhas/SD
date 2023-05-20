package clientSide.stubs;

import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

public class ControlCollectionSiteStub {
    /**
     * Name of the platform where is located the ControlCollectionSite server.
     */
     private String serverHostName;

     /**
      * Port number for listening to service requests.
      */
     private int serverPortNumb;
 
     /**
      * Instantiation of a stub to the ControlCollectionSite.
      *
      * @param serverHostName name of the platform where is located the ControlCollectionSite server
      * @param serverPortNumb port number for listening to service requests
      */
     public ControlCollectionSiteStub (String serverHostName, int serverPortNumb) {
         this.serverHostName = serverHostName;
         this.serverPortNumb = serverPortNumb;
     }

     /**
     * Get room index
     * 
     * @return room state list
     */
    public int getRoomIdx(){
        ClientCom com;                 // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.GRI);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.GRIDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        return inMessage.getRoomIdx();
    }

    /**
     * Start of Operations
     */
    public void startOperation(){
        ClientCom com;                     // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SO, ((Master) Thread.currentThread()).getMasterState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SODONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Master take a rest until the return of the ordinary thief's
     */
    public void takeARest(){
        ClientCom com;                 // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.TAR, ((Master) Thread.currentThread()).getMasterState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.TARDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Ordinary thieves deliever the canvas to the master thief
     * 
     * @param canvas to be delievered or nothing
     * @param ap assault party
     * @param members member id
     * @param room target room
     */
    public void handACanvas(int canvas, int room, int ap, int members){
        ClientCom com;                  // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.HAC, canvas, room, ap, members);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.HACDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Master thief collect a canvas from the ordinary thieves
     */
    public void collectACanvas(){
        ClientCom com;                      // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.CAC, ((Master) Thread.currentThread()).getMasterState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.CACDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }


    /**
      *   Server shutdown.
      */
    public void shutdown () {
        ClientCom com;                    // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SHUT);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SHUTDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }
}
