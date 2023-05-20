
package clientSide.stubs;

import commInfra.*;
import genclass.GenericIO;

public class GeneralReposStub {
     /**
     *  Name of the platform where is located the general repository server.
     */
     private String serverHostName;

     /**
      *  Port number for listening to service requests.
      */
     private int serverPortNumb;
 
     /**
      *   Instantiation of a stub to the general repository.
      *
      *     @param serverHostName name of the platform where is located the barber shop server
      *     @param serverPortNumb port number for listening to service requests
      */
     public GeneralReposStub (String serverHostName, int serverPortNumb)
     {
         this.serverHostName = serverHostName;
         this.serverPortNumb = serverPortNumb;
     }
     

     /**
      *   Set master state.
      *
      *     @param id master id
      *     @param state master state
      */
     public void setMasterState (int id, int state)
     {
         ClientCom com;                                                 // communication channel
         Message outMessage, inMessage;
 
         com = new ClientCom (serverHostName, serverPortNumb);
         while (!com.open ())
         { try
         { Thread.sleep ((long) (1000));
         }
         catch (InterruptedException e) {}
         }
         outMessage = new Message (MessageType.STMST, state);
         com.writeObject (outMessage);
         inMessage = (Message) com.readObject ();
         if (inMessage.getMsgType() != MessageType.SACK)
         { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
             GenericIO.writelnString (inMessage.toString ());
             System.exit (1);
         }
         com.close ();
     }
 
     /**
      *   Set Ordinary state.
      *
      *     @param id Ordinary id
      *     @param state Ordinary state
      */
 
     public void setOrdinaryState (int id, int state)
     {
         ClientCom com;                                                 // communication channel
         Message outMessage, inMessage;
 
         com = new ClientCom (serverHostName, serverPortNumb);
         while (!com.open ())
         { try
         { Thread.sleep ((long) (1000));
         }
         catch (InterruptedException e) {}
         }
         outMessage = new Message (MessageType.STOST, id, state);
         com.writeObject (outMessage);
         inMessage = (Message) com.readObject ();
         if (inMessage.getMsgType() != MessageType.SACK)
         { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
             GenericIO.writelnString (inMessage.toString ());
             System.exit (1);
         }
         com.close ();
     }

      /**
      * Set Ordinary situation
      * 
      * @param id ordinary
      * @param sit ordinary situation
      */
      public void setOrdinarySituation (int id, char sit)
     {
         ClientCom com;                                                 // communication channel
         Message outMessage, inMessage;
 
         com = new ClientCom (serverHostName, serverPortNumb);
         while (!com.open ())
         { try
         { Thread.sleep ((long) (1000));
         }
         catch (InterruptedException e) {}
         }
         outMessage = new Message (MessageType.STOSIT, id, sit);
         com.writeObject (outMessage);
         inMessage = (Message) com.readObject ();
         if (inMessage.getMsgType() != MessageType.SACK)
         { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
             GenericIO.writelnString (inMessage.toString ());
             System.exit (1);
         }
         com.close ();
     }


    /**
     * Set ordinary thieves maximum distances
     * 
     * @param id of the ordinary thieve
     * @return ordinary md
     */
    public int getOrdinaryMD(int id) {
        ClientCom com;                                                 // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.GTOMD, id);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.GTOMDDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        return inMessage.getMD();
    }

    /**
     * Set assault party room
     * @param ap assault party
     * @param room room to heist
     */
    public void setAssaultPartyRoom(int ap, int room){
        ClientCom com;                                                 // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.STAPR, ap, room);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SACK)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Set Assault Party element
     * 
     * @param elem index
     * @param tid  id of the ordinary thief
     */
    public void setAssaultPartyElement(int elem, int tid) {
        ClientCom com;                                                 // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.STAPE, elem, tid);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SACK)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Set canvas
     * 
     * @param elem   index
     * @param canvas
     */
    public void setCanvas(int elem, int canvas) {
        ClientCom com;                                                 // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.STCVS, elem, canvas);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SACK)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Update thief pos
     * 
     * @param elem index
     * @param pos  actual pos of the thief in the line
     */
    public void setPosition(int elem, int pos) {
        ClientCom com;                                                 // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.STPOS, elem, pos);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SACK)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Set room paitings
     * 
     * @param paitings on each room
     */
    public void setRoomPaitings(int[] paitings) {
        ClientCom com;                                                 // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.STRMP, paitings);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SACK)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Get room distances
     * 
     * @return rooms distance
     */
    public int[] getRoomDistances() {
        ClientCom com;                                                 // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ()) {
            try { 
                Thread.sleep ((long) (1000));
            } catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.GTRD);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.GTRDDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        return inMessage.getDistances();
    }

    /**
     * Set heisted paintings
     */
    public void setRobbedPaintings() {
        ClientCom com;                                                 // communication channel
        Message outMessage, inMessage;

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.STRBP);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SACK)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }


     /**
      *   Server shutdown.
      */
     public void shutdown ()
     {
         ClientCom com;                                                 // communication channel
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
