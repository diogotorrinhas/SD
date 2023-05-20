package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import clientSide.entities.*;
import commInfra.*;


public class ConcentrationSiteInterface {

    /**
   *  Reference to the concentrationSite.
   */

   private final ConcentrationSite concentrationSite;

   /**
    *  Instantiation of an interface to the concentrationSite.
    *
    *    @param cs reference to the concentrationSite
    */
 
    public ConcentrationSiteInterface (ConcentrationSite cs)
    {
       this.concentrationSite = cs;
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


            case MessageType.PE:
            case MessageType.AIN:  
                if ((inMessage.getOrdinaryId () < 0) || (inMessage.getOrdinaryId () >= SimulConsts.M))
                    throw new MessageException ("Invalid ordinary id!", inMessage);
                else if ((inMessage.getOrdinaryState () < OrdinaryStates.CONCENTRATION_SITE) || (inMessage.getOrdinaryState () > OrdinaryStates.COLLECTION_SITE))
                    throw new MessageException ("Invalid ordinary state!", inMessage);
                break;


            case MessageType.PAP: 
            case MessageType.SUTR: 
                if ((inMessage.getMasterState () < MasterStates.PLANNING_THE_HEIST) || (inMessage.getMasterState () > MasterStates.PRESENTING_THE_REPORT))
                throw new MessageException ("Invalid master state!", inMessage);                     
                break;


            case MessageType.SHUT:
            case MessageType.GRCS: 
            case MessageType.GAP: 
            case MessageType.AS:     
                break;
            default:                   throw new MessageException ("Invalid message type!", inMessage);
        }
 
        /* processing */
    
        switch (inMessage.getMsgType ()) { 
            
            case MessageType.PE:
                ((ConcentrationSiteClientProxy) Thread.currentThread ()).setOrdinaryId (inMessage.getOrdinaryId ());
                ((ConcentrationSiteClientProxy) Thread.currentThread ()).setOrdinaryState (inMessage.getOrdinaryState ());
                int pe = concentrationSite.prepareExcursion();
                outMessage = new Message (MessageType.PEDONE, pe);
                break;

            case MessageType.AIN: 
                ((ConcentrationSiteClientProxy) Thread.currentThread ()).setOrdinaryId (inMessage.getOrdinaryId ());
                ((ConcentrationSiteClientProxy) Thread.currentThread ()).setOrdinaryState (inMessage.getOrdinaryState ());
                boolean ain = concentrationSite.amINeeded(inMessage.getAp());
                outMessage = new Message (MessageType.AINDONE, ain);
                break;

            case MessageType.PAP: 
                ((ConcentrationSiteClientProxy) Thread.currentThread ()).setMasterState (inMessage.getMasterState ());
                concentrationSite.prepareAssaultParty(inMessage.getAp(), inMessage.getRoom());
                outMessage = new Message (MessageType.PAPDONE);
                    break;

            case MessageType.SUTR: 
                ((ConcentrationSiteClientProxy) Thread.currentThread ()).setMasterState (inMessage.getMasterState ());
                concentrationSite.sumUpResults();
                outMessage = new Message (MessageType.SUTRDONE);
                break;

            case MessageType.GRCS: 
                int gr = concentrationSite.getRoom(inMessage.getAp());
                outMessage = new Message (MessageType.GRCSDONE, gr);
                break;

            case MessageType.GAP: 
                int gap = concentrationSite.getAssautlParty();
                outMessage = new Message (MessageType.GAPDONE, gap);
                break;

            case MessageType.AS: 
                int as = concentrationSite.appraiseSit(inMessage.getRoomStt());
                outMessage = new Message (MessageType.ASDONE, as);
                break;
            
            case MessageType.ENDOP:
                concentrationSite.endOperation(inMessage.getOrdinaryId());
                outMessage = new Message (MessageType.SHUTDONE);
                break;    

            case MessageType.SHUT:
                concentrationSite.shutdown ();
                outMessage = new Message (MessageType.SHUTDONE);
                break;
            }

        return (outMessage);
    }
}
