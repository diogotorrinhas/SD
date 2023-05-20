package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import clientSide.entities.*;
import commInfra.*;

public class AssaultPartyInterface {
    
    /**
   *  Reference to the assault party.
   */

   private final AssaultParty assaultParty;

   /**
    *  Instantiation of an interface to the assault party.
    *
    *    @param ap reference to the assault party
    */
 
    public AssaultPartyInterface (AssaultParty ap)
    {
        this.assaultParty = ap;
    }
 
   /**
    *  Processing of the incoming messages.
    *
    *  Validation, execution of the corresponding method and generation of the outgoing message.
    *
    *    @param inMessage service request
    *    @return service reply
    *    @throws MessageException if the incoming message is not valid
    */
 
    public Message processAndReply (Message inMessage) throws MessageException
    {
        Message outMessage = null;                                     // outgoing message
    
        /* validation of the incoming message */
    
        switch (inMessage.getMsgType ()) {
            case MessageType.AM: 
                if ((inMessage.getOrdinaryId () < 0) || (inMessage.getOrdinaryId () >= SimulConsts.M-1))
                    throw new MessageException ("Invalid ordinary id!", inMessage);  
                                     
                break;

            case MessageType.RD:
            case MessageType.CI:
            case MessageType.CO:  
                if ((inMessage.getOrdinaryId () < 0) || (inMessage.getOrdinaryId () >= SimulConsts.M-1))
                    throw new MessageException ("Invalid ordinary id!", inMessage);
                else if ((inMessage.getOrdinaryState () < OrdinaryStates.CONCENTRATION_SITE) || (inMessage.getOrdinaryState () > OrdinaryStates.COLLECTION_SITE))
                    throw new MessageException ("Invalid ordinary state!", inMessage);
                break;

            case MessageType.SAP: 
                if ((inMessage.getMasterState () < MasterStates.PLANNING_THE_HEIST) || (inMessage.getMasterState () > MasterStates.PRESENTING_THE_REPORT))
                throw new MessageException ("Invalid master state!", inMessage);                     
                break;

            case MessageType.SHUT:
            case MessageType.GRAP:     
                break;
            default:                   throw new MessageException ("Invalid message type!", inMessage);
       }

        /* processing */
    
        switch (inMessage.getMsgType ()) { 
            
            case MessageType.AM: 
                ((AssaultPartyClientProxy) Thread.currentThread ()).setOrdinaryId (inMessage.getOrdinaryId ());
                int am = assaultParty.assignMember(inMessage.getAp());
                outMessage = new Message (MessageType.AMDONE, am);
                break;

            case MessageType.RD: 
                ((AssaultPartyClientProxy) Thread.currentThread ()).setOrdinaryId (inMessage.getOrdinaryId ());
                ((AssaultPartyClientProxy) Thread.currentThread ()).setOrdinaryState (inMessage.getOrdinaryState ());
                assaultParty.reverseDirection(inMessage.getMember());
                outMessage = new Message (MessageType.RDDONE);
                break;

            case MessageType.CI: 
                ((AssaultPartyClientProxy) Thread.currentThread ()).setOrdinaryId (inMessage.getOrdinaryId ());
                ((AssaultPartyClientProxy) Thread.currentThread ()).setOrdinaryState (inMessage.getOrdinaryState ());
                boolean ci = assaultParty.crawlIn(inMessage.getAp(), inMessage.getMember(), inMessage.getMD());
                outMessage = new Message (MessageType.CIDONE, ci);
                break;

            case MessageType.CO: 
                ((AssaultPartyClientProxy) Thread.currentThread ()).setOrdinaryId (inMessage.getOrdinaryId ());
                ((AssaultPartyClientProxy) Thread.currentThread ()).setOrdinaryState (inMessage.getOrdinaryState ());
                boolean co = assaultParty.crawlOut(inMessage.getAp(), inMessage.getMember(), inMessage.getMD());
                outMessage = new Message (MessageType.CODONE, co);
                break;

            case MessageType.SAP:
                ((AssaultPartyClientProxy) Thread.currentThread ()).setMasterState (inMessage.getMasterState ());
                assaultParty.sendAssaultParty(inMessage.getRoom());
                outMessage = new Message (MessageType.SAPDONE);
                break;

            case MessageType.GRAP: 
                int grap = assaultParty.getRoom();
                outMessage = new Message (MessageType.GRAPDONE, grap);
                break;
                                    
            case MessageType.SHUT:
                assaultParty.shutdown ();
                outMessage = new Message (MessageType.SHUTDONE);
                break;
            }
        return (outMessage);
    }
}
