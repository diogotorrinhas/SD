package clientSide.stubs;


import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

public class ConcentrationSiteStub {
    /**
     * Name of the platform where is located the ConcentrationSite server.
     */
     private String serverHostName;

     /**
      * Port number for listening to service requests.
      */
 
     private int serverPortNumb;
 
     /**
      * Instantiation of a stub to the ConcentrationSite.
      *
      * @param serverHostName name of the platform where is located the ConcentrationSite server
      * @param serverPortNumb port number for listening to service requests
      */
 
     public ConcentrationSiteStub (String serverHostName, int serverPortNumb) {
         this.serverHostName = serverHostName;
         this.serverPortNumb = serverPortNumb;
     }

     /**
     * Get room
     * 
     * @param ap assault party
     * @return room target
     */
    public int getRoom(int ap){
        ClientCom com;                    // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.GRCS, ap);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.GRCSDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        return inMessage.getRoomCS();
    }

    /**
     * Get assaultParty
     * 
     * @return assault party
     */
    public int getAssautlParty(){
        ClientCom com;                      // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.GAP);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.GAPDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        return inMessage.getAssautlParty();
    }

    /**
     * Master thief takes a decision
     * 
     * @param roomState state of the room (if all rooms are empty or not)
     * @return decision of the master
     */
    public int appraiseSit(boolean roomState) {
        ClientCom com;                    // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.AS, roomState);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.ASDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        return inMessage.appraiseSit();
    }


    /**
     * Master Thief prepare assault party
     * 
     * @param ap assault party that will be prepared
     * @param room target room
     */
    public  void prepareAssaultParty(int ap, int room) {
        ClientCom com;                  // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.PAP, ap, room, ((Master) Thread.currentThread()).getMasterState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.PAPDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Ordinary Thief prepare excursion
     * 
     * @return joined assault party
     */
    public  int prepareExcursion() {
        ClientCom com;                     // communication channel
        Message outMessage,                // outgoing message
                inMessage;                 // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.PE, ((Ordinary) Thread.currentThread()).getOrdinaryId() ,((Ordinary) Thread.currentThread()).getOrdinaryState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.PEDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        return inMessage.prepareExcursion();
    }

    /**
     * Ordinary Thief is available, so he indicates to the master that he is available
     * 
     * @param ap assault party
     * @return decision from the master service
     */
    public  boolean amINeeded(int ap){
        ClientCom com;                // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.AIN, ap, ((Ordinary) Thread.currentThread()).getOrdinaryId() ,((Ordinary) Thread.currentThread()).getOrdinaryState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.AINDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        return inMessage.amINeeded();
    }

    /**
     * Master Thief sum up the results from the heist
     */
    public  void sumUpResults() {
        ClientCom com;                   // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SUTR, ((Master) Thread.currentThread()).getMasterState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SUTRDONE)
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
        ClientCom com;              // communication channel
        Message outMessage,         // outgoing message
                inMessage;          // incoming message

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
