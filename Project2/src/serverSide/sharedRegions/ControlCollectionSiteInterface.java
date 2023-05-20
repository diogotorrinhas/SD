package serverSide.sharedRegions;

import serverSide.entities.*;
import clientSide.entities.*;
import commInfra.*;


public class ControlCollectionSiteInterface {

    /**
   *  Reference to the controlCollectionSite.
   */

   private final ControlCollectionSite controlCollectionSite;

   /**
    *  Instantiation of an interface to the controlCollectionSite.
    *
    *    @param ccs reference to the controlCollectionSite
    */
 
    public ControlCollectionSiteInterface (ControlCollectionSite ccs)
    {
       this.controlCollectionSite = ccs;
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
    
        switch (inMessage.getMsgType ()){ 

            case MessageType.SO: 
            case MessageType.TAR:
            case MessageType.CAC:
                if ((inMessage.getMasterState () < MasterStates.PLANNING_THE_HEIST) || (inMessage.getMasterState () > MasterStates.PRESENTING_THE_REPORT))
                throw new MessageException ("Invalid master state!", inMessage);                     
                break;

            case MessageType.SHUT:
            case MessageType.GRI:   
            case MessageType.HAC:
                break;
            default:                   throw new MessageException ("Invalid message type!", inMessage);
        } 

        /* processing */
 
        switch (inMessage.getMsgType ()) { 
            
            case MessageType.SO: 
                ((ControlCollectionSiteClientProxy) Thread.currentThread ()).setMasterState (inMessage.getMasterState ());
                controlCollectionSite.startOperation();
                outMessage = new Message (MessageType.SODONE);
                break;

            case MessageType.TAR:
                ((ControlCollectionSiteClientProxy) Thread.currentThread ()).setMasterState (inMessage.getMasterState ());
                controlCollectionSite.takeARest();
                outMessage = new Message (MessageType.TARDONE);
                break;

            case MessageType.CAC:
                ((ControlCollectionSiteClientProxy) Thread.currentThread ()).setMasterState (inMessage.getMasterState ());
                controlCollectionSite.collectACanvas();
                outMessage = new Message (MessageType.CACDONE);
                break;

            case MessageType.GRI:   
                int gri = controlCollectionSite.getRoomIdx();
                outMessage = new Message (MessageType.GRIDONE, gri);
                break;

            case MessageType.HAC:
                controlCollectionSite.handACanvas(inMessage.getCanvas(), inMessage.getRoom(), inMessage.getAp(), inMessage.getMember());
                outMessage = new Message (MessageType.HACDONE);
                break;
                                    
            case MessageType.SHUT:
                controlCollectionSite.shutdown ();
                outMessage = new Message (MessageType.SHUTDONE);
                break;
            }

        return (outMessage);
    }
    
}
