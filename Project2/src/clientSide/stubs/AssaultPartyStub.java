package clientSide.stubs;

import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

public class AssaultPartyStub {
    /**
     * Name of the platform where is located the AssaultParty server.
     */
     private String serverHostName;

     /**
      * Port number for listening to service requests.
      */
     private int serverPortNumb;
 
     /**
      * Instantiation of a stub to the AssaultParty.
      *
      * @param serverHostName name of the platform where is located the AssaultParty server
      * @param serverPortNumb port number for listening to service requests
      */
 
     public AssaultPartyStub (String serverHostName, int serverPortNumb) {
         this.serverHostName = serverHostName;
         this.serverPortNumb = serverPortNumb;
     }

     /**
     * Assigned an assaultParty to a thief
     * 
     * @param ap assault party
     * @return thief id
     */
    public int assignMember(int ap) {
        ClientCom com;                      // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.AM, ap, ((Ordinary) Thread.currentThread()).getOrdinaryId());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.AMDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        return inMessage.assignMember();
    }

    /**
     * Get room
     * 
     * @return room id
     */
    public int getRoom() {
        ClientCom com;                     // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.GRAP);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.GRAPDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        return inMessage.getRoomAP();
    }

    /**
     * Last thief indicates to reverse direction to the first ordinary thief
     * 
     * @param member id
     */
    public void reverseDirection(int member) {
        ClientCom com;                      // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.RD, member, ((Ordinary) Thread.currentThread()).getOrdinaryId(), ((Ordinary) Thread.currentThread()).getOrdinaryState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.RDDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Master thief sends the assaultParty to the museum
     * 
     * @param room target room
     */
    public void sendAssaultParty(int room) {
        ClientCom com;                       // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SAP, room, ((Master) Thread.currentThread()).getMasterState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SAPDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Crawling in to the museum room
     *
     * @param ap     number
     * @param member thief id in the crawl queue
     * @param md     maximum distance capable by the thief
     * @return true if the thief reached the museum room
     */
    public boolean crawlIn(int ap, int member, int md) {
        ClientCom com;                 // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.CI, ap, member, md, ((Ordinary) Thread.currentThread()).getOrdinaryId() ,((Ordinary) Thread.currentThread()).getOrdinaryState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.CIDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        return inMessage.crawlIn();
    }

    /**
     * Crawling out to the outside gathering
     * 
     * @param ap     number
     * @param member thief id in the crawl queue
     * @param md     maximum distance capable by the thief
     * @return true if the thief reached the outside zone
     */
    public boolean crawlOut(int ap, int member, int md) {
        ClientCom com;                  // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.CO, ap, member, md, ((Ordinary) Thread.currentThread()).getOrdinaryId() ,((Ordinary) Thread.currentThread()).getOrdinaryState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.CODONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close (); 
        return inMessage.crawlOut();
    }


    /**
      * Server shutdown.
      */
    public void shutdown () {
        ClientCom com;          // communication channel
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
