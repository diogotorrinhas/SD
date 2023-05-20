package serverSide.sharedRegions;

import commInfra.*;

public class GeneralReposInterface {
    /**
     * Reference to the GeneralRepository
     */
    private final GeneralRepos GeneralRepository;

    /**
     * Instantiation of an interface to the GeneralRepository
     * 
     * @param repos reference to the General Repository
     */
    public GeneralReposInterface(GeneralRepos repos) {
        this.GeneralRepository = repos;
    }

    /**
     * Processing of the incoming messages
     * Validation, execution of the corresponding method and generation of the
     * outgoing message.
     * 
     * @param inMessage service request
     * @return service reply
     * @throws MessageException if incoming message was not valid
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        // outGoing message
        Message outMessage = null;

        /* Validation of the incoming message */

        switch (inMessage.getMsgType()) {
            case MessageType.STMST:
            case MessageType.STOST:
            case MessageType.STOSIT:
            case MessageType.GTOMD:
            case MessageType.STAPR:
            case MessageType.STAPE:
            case MessageType.STCVS:
            case MessageType.STPOS:
            case MessageType.STRMP:
            case MessageType.GTRD:
            case MessageType.STRBP:
            case MessageType.SHUT:
                break;
            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        /* Processing of the incoming message */

        switch (inMessage.getMsgType()) {

            case MessageType.STMST:
                GeneralRepository.setMasterState(inMessage.getMasterState());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.STOST:
                GeneralRepository.setOrdinaryState(inMessage.getOrdinaryId(), inMessage.getOrdinaryState());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.STOSIT:
                GeneralRepository.setOrdinarySituation(inMessage.getOrdinaryId(), inMessage.getSit());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.GTOMD:
                int md = GeneralRepository.getOrdinaryMD(inMessage.getOrdinaryId());
                outMessage = new Message(MessageType.GTOMDDONE, md);
                break;

            case MessageType.STAPR:
                GeneralRepository.setApRoom(inMessage.getAp(), inMessage.getRoom());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.STAPE:
                GeneralRepository.setApElement(inMessage.getElem(), inMessage.getTid());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.STCVS:
                GeneralRepository.setCanvas(inMessage.getElem(), inMessage.getCanvas());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.STPOS:
                GeneralRepository.setPosition(inMessage.getElem(), inMessage.getPos());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.STRMP:
                GeneralRepository.setRoomPaitings(inMessage.getPaintings());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.GTRD:
                int[] dists = GeneralRepository.getRoomDistances();
                outMessage = new Message(MessageType.GTRDDONE, dists);
                break;

            case MessageType.STRBP:
                GeneralRepository.setRobbedPaintings();
                outMessage = new Message(MessageType.SACK);
                break;


            case MessageType.SHUT:
                GeneralRepository.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }
        return (outMessage);
    }

}