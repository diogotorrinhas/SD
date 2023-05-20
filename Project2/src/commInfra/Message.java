package commInfra;

import java.io.*;
import genclass.GenericIO;

/**
 *   Internal structure of the exchanged messages.
 *
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on a communication channel under the TCP protocol.
 */

public class Message implements Serializable
{
    /**
     *  Serialization key.
     */

    private static final long serialVersionUID = 2021L;

    /**
     *  Message type.
     */

    private int msgType = -1;

    /**
     *  Master state.
     */

    private int masterState = -1;

    /**
     *  Ordinary identification.
     */

    private int ordinaryId = -1;

    /**
     *  Ordinary state.
     */

    private int ordinaryState = -1;


    /**
     *  End of operations.
     */
    private boolean endOp = false;


    /**
     * Assault party
     */
    private int ap = -1;

    /**
     *  Getting ap.
     *
     *     @return ap
     */

     public int getAp () {
        return (ap);
    }

    /**
     * room id
     */
    private int room = -1;

    /**
     *  Getting room.
     *
     *     @return room
     */

     public int getRoom () {
        return (room);
    }

    /**
     * ap element
     */
    private int elem = -1;

    /**
     *  Getting ap element.
     *
     *     @return elem
     */

     public int getElem () {
        return (elem);
    }

    /**
     * ordinary maximum distance
     */
    private int md = -1;

    /**
     *  Getting md.
     *
     *     @return md
     */

     public int getMD () {
        return (md);
    }

    /**
     * canvas possesion
     */
    private int canvas = -1;

    /**
     *  Getting canvas.
     *
     *     @return canvas
     */

     public int getCanvas () {
        return (canvas);
    }

    /**
     * ordinary position
     */
    private int pos = -1;

    /**
     *  Getting pos.
     *
     *     @return pos
     */

     public int getPos () {
        return (pos);
    }

    /**
     * thread id
     */
    private int tid = -1;

    /**
     *  Getting tid.
     *
     *     @return tid
     */

     public int getTid () {
        return (tid);
    }

    /**
     * ap member id
     */
    private int member = -1;

    /**
     *  Getting member.
     *
     *     @return member
     */

     public int getMember () {
        return (member);
    }


    /**
     * museum room state
     */
    private boolean roomStt;

    /**
     *  Getting room state.
     *
     *     @return roomStt
     */

     public boolean getRoomStt () {
        return (roomStt);
    }

    /**
     * paitings in each museum room
     */
    private int[] paitings = {-1, -1, -1, -1, -1, -1};

    /**
     *  Getting paintings.
     *
     *     @return paintings
     */

     public int[] getPaintings () {
        return (paitings);
    }

    /**
     * distance to each museum room
     */
    private int[] distances = {-1, -1, -1, -1, -1, -1};

    /**
     *  Getting distances.
     *
     *     @return distances
     */

     public int[] getDistances () {
        return (distances);
    }

    /**
     * ordinary situation
    */
    private char sit = 'W';

    /**
     *  Getting situation.
     *
     *     @return sit
     */

     public char getSit () {
        return (sit);
    }

    /**
     * assignMember operation result
     */
    private int am;

    /**
     * assignMember at assault party
     * @return am variable
     */
    public int assignMember(){
        return am;
    }

    /**
     * getRoom assault party operation result
     */
    private int grap;

    /**
     * getRoom at assault party
     * @return grap variable
     */
    public int getRoomAP(){
        return grap;
    }

    /**
     * crawl in operation result
     */
    private boolean ci;

    /**
     * crawl in at assault party
     * @return ci variable
     */
    public boolean crawlIn(){
        return ci;
    }

    /**
     * crawl out operation result
     */
    private boolean co;

    /**
     * crawl out at assault party
     * @return co variable
     */
    public boolean crawlOut(){
        return co;
    }

    /**
     *  getRoom operation result
     */
    private int grcs;

    /**
     * getRoom at concentration site
     * @return grcs variable
     */
    public int getRoomCS(){
        return grcs;
    }

    /**
     * get ap operation result
     */
    private int gap;

    /**
     * get ap at concentration site
     * @return gap variable
     */
    public int getAssautlParty(){
        return gap;
    }

    /**
     *  appraisse situation operation result
     */
    private int as;

    /**
     * appraise sit at concentration site
     * @return as variable
     */
    public int appraiseSit(){
        return as;
    }

    /**
     *  prepare excursion operation result
     */
    private int pe;

    /**
     * prepare excursion at concentration site
     * @return pe variable
     */
    public int prepareExcursion(){
        return pe;
    }

    /**
     *  am i needed operation result
     */
    private boolean ain;

    /**
     * am i needed concentration site
     * @return ain variable
     */
    public boolean amINeeded(){
        return ain;
    }

    /**
     * get room idx operation result
     */
    private int gri;

    /**
     * getRoomIdx at control collection site
     * @return gri variable
     */
    public int getRoomIdx(){
        return gri;
    }

    /**
     * rool a canvas operation result
     */
    private int rac;

    /**
     * rollACanvas at museum
     * @return rac variable
     */
    public int rollACanvas(){
        return rac;
    }

    /**
     *  Message instantiation (form 1i).
     *
     *     @param type message type
     */

    public Message (int type) {
        msgType = type;
    }

    /**
     *  Message instantiation (form 2i).
     *
     *     @param type message type
     *     @param i integer
     */

    public Message (int type, int i) {
        msgType = type;
        
        if ((msgType == MessageType.GRCS)) {
            ap = i;
        } else if((msgType == MessageType.SUTR) || (msgType == MessageType.SO) || (msgType == MessageType.TAR) || (msgType == MessageType.CAC) || (msgType == MessageType.STMST)) {
            masterState = i;
        } else if(msgType == MessageType.GTOMD){
            ordinaryId = i;
        } else if(msgType == MessageType.GTOMDDONE) {
            md = i;
        } else if(msgType == MessageType.GRIDONE) {
            gri = i;
        } else if(msgType == MessageType.ASDONE) {
            as = i;
        } else if(msgType == MessageType.GAPDONE) {
            gap = i;
        } else if(msgType == MessageType.PEDONE) {
            pe = i;
        } else if(msgType == MessageType.GRCSDONE) {
            grcs = i;
        } else if(msgType == MessageType.GRAPDONE) {
            grap = i;
        } else if(msgType == MessageType.AMDONE) {
            am = i;
        } else if(msgType == MessageType.RACDONE) {
            rac = i;     
        } else {
            GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
    }


    /**
     *  Message instantiation (form 3i).
     *
     *     @param type message type
     *     @param i0 integer
     *     @param i1 integer
     */

    public Message (int type, int i0, int i1) {
        msgType = type;
        if((msgType == MessageType.AM) ){
            ap = i0;
            ordinaryId = i1;
        } else if((msgType == MessageType.SAP)){
            room = i0;
            masterState = i1;
        } else if((msgType == MessageType.PE) || (msgType == MessageType.STOST)) {
            ordinaryId = i0;
            ordinaryState = i1;
        } else if((msgType == MessageType.STAPR)){
            ap = i0;
            room = i1;
        } else if((msgType == MessageType.STAPE)){
            elem = i0;
            tid = i1;
        } else if((msgType == MessageType.STCVS)){
            elem = i0;
            canvas = i1;
        } else if((msgType == MessageType.STPOS)){
            elem = i0;
            pos = i1;
        } else {
            GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
    }


    /**
     *  Message instantiation (form 4i).
     *
     *     @param type message type
     *     @param i0 integer
     *     @param i1 integer
     *     @param i2 integer
     */

     public Message (int type, int i0, int i1, int i2) {
        msgType = type;
        if((msgType == MessageType.RD) ){
            member = i0;
            ordinaryId = i1;
            ordinaryState = i2;
        }
        else if((msgType == MessageType.PAP) ){
            ap = i0;
            room = i1;
            masterState = i2;
        }
        else if((msgType == MessageType.AIN) ){
            ap = i0;
            ordinaryId = i1;
            ordinaryState = i2;

        }
        else if((msgType == MessageType.RAC) ){
            room = i0;
            ap = i1;
            member = i2;
        }
        else {
            GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
     }

     /**
     *  Message instantiation (form 5i).
     *
     *     @param type message type
     *     @param i0 identification
     *     @param i1 integer
     *     @param i2 integer
     *     @param i3 integer
     */

     public Message (int type, int i0, int i1, int i2, int i3) {
        msgType = type;
        if((msgType == MessageType.HAC) ){
            canvas = i0;
            room = i1;
            ap = i2;
            member = i3;
        }
        else {
            GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
     }


     /**
     *  Message instantiation (form 6i).
     *
     *     @param type message type
     *     @param i0 integer
     *     @param i1 integer
     *     @param i2 integer
     *     @param i3 integer
     *     @param i4 integer
     */

     public Message (int type, int i0, int i1, int i2, int i3, int i4) {
        msgType = type;
        if((msgType == MessageType.CI) || (msgType == MessageType.CO)){
            ap = i0;
            member = i1;
            md = i2;
            ordinaryId = i3;
            ordinaryState = i4;
        } else {
            GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
     }


    /**
     *  Message instantiation (form 1i,1b).
     *
     *     @param type message type
     *     @param b boolean
     */

    public Message (int type, boolean b) {
        msgType = type;
        if((msgType == MessageType.AS))
            roomStt = b;
        else if(msgType == MessageType.AINDONE)
            ain = b;
        else if(msgType == MessageType.CIDONE)
            ci = b;
        else if(msgType == MessageType.CODONE)
            co = b;
        else if((msgType == MessageType.EOPDONE))
           endOp = b;
        else {
            GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
    }


    /**
     *  Message instantiation (form 1i,1i[]).
     *
     *     @param type message type
     *     @param i integer array
     */

     public Message (int type, int[] i) {
        msgType = type;
        if((msgType == MessageType.STRMP))
            paitings = i;
        else if(msgType == MessageType.GTRDDONE){
            distances = i;
        } else {
            GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
    }


    /**
     *  Message instantiation (form 2i,1c).
     *
     *     @param type message type
     *     @param i integer
     *     @param c char
     */

     public Message (int type, int i, char c) {
        msgType = type;
        if((msgType == MessageType.STOSIT)) {
            ordinaryId = i;
            sit = c;
        }
        else {
            GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
    }



    /**
     *  Getting message type.
     *
     *     @return message type
     */

    public int getMsgType () {
        return (msgType);
    }

    /**
     *  Getting Master state.
     *
     *     @return Master state
     */

    public int getMasterState () {
        return (masterState);
    }

    /**
     *  Getting Ordinary identification.
     *
     *     @return Ordinary identification
     */

    public int getOrdinaryId ()
    {
        return (ordinaryId);
    }

    /**
     *  Getting Ordinary state.
     *
     *     @return Ordinary state
     */

    public int getOrdinaryState () {
        return (ordinaryState);
    }



    /**
     *  Getting end of operations flag (barber).
     *
     *     @return end of operations flag
     */

    public boolean getEndOp ()
    {
        return (endOp);
    }

    /**
     *  Printing the values of the internal fields.
     *
     *  It is used for debugging purposes.
     *
     *     @return string containing, in separate lines, the pair field name - field value
     */

    @Override
    public String toString ()
    {
        return ("Message type = " + msgType +
                "\nMaster State = " + masterState +
                "\nOrdinary Id = " + ordinaryId +
                "\nOrdinary State = " + ordinaryState +
                "\nEnd of Operations (Ordinary) = " + endOp +
                "\nAp = " + ap +
                "\nRoom = " + room +
                "\nElem = " + elem +
                "\nMd = " + md +
                "\nCanvas = " + canvas +
                "\nPos = " + pos +
                "\nTid = " + tid +
                "\nMember = " + member +
                "\nRoomStt = " + roomStt +
                "\nPaitings = " + paitings +
                "\nDistances = " + distances +
                "\nSit = " + sit );
    }
}
