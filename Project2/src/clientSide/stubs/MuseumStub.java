package clientSide.stubs;

import commInfra.*;
import genclass.GenericIO;

public class MuseumStub {
    /**
     * Name of the platform where is located the Museum server.
     */
     private String serverHostName;

     /**
      * Port number for listening to service requests.
      */
     private int serverPortNumb;
 
     /**
      * Instantiation of a stub to the Museum.
      *
      * @param serverHostName name of the platform where is located the Museum server
      * @param serverPortNumb port number for listening to service requests
      */
     public MuseumStub (String serverHostName, int serverPortNumb) {
         this.serverHostName = serverHostName;
         this.serverPortNumb = serverPortNumb;
     }

     /**
     * RollACanvas operation by a thief
     * 
     * @param room target room where the assault takes place
     * @param ap assault party
     * @param members id member
     * @return number of canvas stolen by the thief
     */
    public int rollACanvas(int room, int ap, int members) {
        ClientCom com;              // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ()) //waits for a connections to be established
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.RAC, room, ap, members);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.RACDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Message Type invalid!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        return inMessage.rollACanvas();
    }


    /**
      *  Server shutdown.
      */
    public void shutdown () {
        ClientCom com;             // communication channel
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
