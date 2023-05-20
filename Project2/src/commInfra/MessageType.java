package commInfra;

/**
 *   Type of the exchanged messages.
 *
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on a communication channel under the TCP protocol.
 */

public class MessageType
{
    /**
     *  Server shutdown (service request).
     */

    public static final int SHUT = 1;

    /**
     *  Server was shutdown (reply).
     */

    public static final int SHUTDONE = 2;

    /**
     *  End of work - master (service request).
     */

     public static final int ENDOP = 3;

     /**
      *  master goes home (reply).
      */
 
     public static final int EOPDONE = 4;


    /**
     *  Set master state (service request).
     */

    public static final int STMST = 5;


    /**
     *  Set ordinary state (service request).
     */

    public static final int STOST = 6;

    /**
     *  Set ordinary situation (service request).
     */

     public static final int STOSIT = 7;

    /**
     *  Get ordinary md (service request).
     */

     public static final int GTOMD = 8;


     /**
     *  Get ordinary md (service reply).
     */

     public static final int GTOMDDONE = 9;


    /**
     *  Set ap room (service request).
     */

     public static final int STAPR = 10;

    /**
     *  Set ap element (service request).
     */

     public static final int STAPE = 11;

    /**
     *  Set canvas (service request).
     */

     public static final int STCVS = 12;

    /**
     *  Set position (service request).
     */

     public static final int STPOS = 13;

    /**
     *  Set room paitings (service request).
     */

     public static final int STRMP = 14;

    /**
     *  Get room distances (service request).
     */

     public static final int GTRD = 15;

    /**
     *  Get room distances (service reply).
     */

     public static final int GTRDDONE = 16;

    /**
     *  Set robbed paitings (service request).
     */

     public static final int STRBP = 17;

    /**
     *  Setting acknowledged (reply).
     */

    public static final int SACK = 18;


    /**
     *  Request start operation (service request).
     */

    public static final int  SO = 19;

    /**
     *  Operation started started (reply).
     */

    public static final int  SODONE = 20;

    /**
     *  Request get room index (service request).
     */

    public static final int  GRI = 21;

    /**
     *  Get room index (reply).
     */

    public static final int  GRIDONE = 22;

    /**
     *  Request appraise situation (service request).
     */

    public static final int AS = 23;

    /**
     *  Appraise situation (reply).
     */

    public static final int ASDONE = 24;

    /**
     *  Request get assult party (service request).
     */

     public static final int GAP = 25;

     /**
      *  get assult party (reply).
      */
 
     public static final int GAPDONE = 26;

    /**
     *  Request prepare assult party (service request).
     */

    public static final int PAP = 27;

    /**
     *  Prepare assult party (reply).
     */

    public static final int PAPDONE = 28;

    /**
     *  Request Send assault party (service request).
     */

    public static final int SAP = 29;

    /**
     *  Send assault party (reply).
     */

    public static final int SAPDONE = 30;

    /**
     *  Request get room at concentration site (service request).
     */

    public static final int GRCS = 31;

    /**
     *  get room at concentration site done (reply).
     */

    public static final int GRCSDONE = 32;

    /**
     *  Request take a rest (service request).
     */

    public static final int TAR = 33;

    /**
     *  take a rest (reply).
     */

    public static final int TARDONE = 34;

    /**
     *  Request collect a canvas (service request).
     */

    public static final int CAC = 35;

    /**
     *  collect a canvas done (reply).
     */

    public static final int CACDONE = 36;

    /**
     *  Request sum up the results (service request).
     */

    public static final int SUTR = 37;

    /**
     *  sum up the results done (reply).
     */

    public static final int SUTRDONE = 38;

    /**
     *  Request am I needed (service request).
     */

    public static final int AIN = 39;

    /**
     *  am I needed done (reply).
     */

    public static final int AINDONE = 40;

    /**
     *  Request prepare excursion (service request).
     */

    public static final int PE = 41;

    /**
     *  prepare excursion done (reply).
     */

    public static final int PEDONE = 42;

    /**
     *  Request assign member (service request).
     */

    public static final int AM = 43;

    /**
     * assign member done (reply).
     */
    public static final int AMDONE = 44;

    /**
     *  Request crawl in (service request).
     */

    public static final int CI = 45;

    /**
     *  crawl in done (reply).
     */

    public static final int CIDONE = 46;

    /**
     *  Request get room at assault party (service request).
     */

     public static final int GRAP = 47;

     /**
      *  get room at assault party done (reply).
      */
 
     public static final int GRAPDONE = 48;

    /**
     *  Request roll a canvas (service request).
     */

    public static final int RAC = 49;

    /**
     *  roll a canvas done (reply).
     */

    public static final int RACDONE = 50;

    /**
     *  Request reverse direction (service request).
     */

    public static final int RD = 51;

    /**
     * reverse direction done (reply).
     */

    public static final int RDDONE = 52;

    /**
     *  Request crawl out (service request).
     */

    public static final int CO = 53;

    /**
     *  crawl out done (reply).
     */

    public static final int CODONE = 54;

    /**
     *  Request hand a canvas (service request).
     */

    public static final int HAC = 55;

    /**
     * hand a canvas done (reply).
     */

    public static final int HACDONE = 56;



}
