package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import clientSide.entities.*;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;
import genclass.TextFile;
import java.util.Objects;


public class GeneralRepos {
    
    /**
     * Loggin file name
     */

     private final String logFileName;

     /**
      * State of the Master Thief.
      */
 
     private int masterState;
 
     /**
      * State of the ordinary thieves.
      */
 
     private int[] ordinaryState;
 
     /**
      * Situation of the ordinary thieves.
      */
     private char[] ordinarySituation;
 
     /**
      * Maximum displacement of the ordinary thieves.
      */
     private int[] MDs;
 
     /**
      * Elements in the assault party
      */
     private int[][] elements;
 
     /**
      * target room of an assault party
      */
     private int[] apRoom;
 
     /**
      * Number of paintings in a room
      */
     private int[] paintings;
 
     /**
      * Distance of the rooms from outside
      */
     private int[] distances;
 
     /**
      * Number of robbed paintings
      */
     private int robbed;

    /**
     *   Number of entity groups requesting the shutdown.
     */
   private int nEntities;
 
     /**
      * Instantiation of a general repository
      */
 
     public GeneralRepos() {
        logFileName = "logger";
 
         // master thieve
         masterState = MasterStates.PLANNING_THE_HEIST;
 
         // ordinarie thives
         ordinaryState = new int[SimulConsts.M - 1];
         ordinarySituation = new char[SimulConsts.M - 1];
         MDs = new int[SimulConsts.M - 1];
         for (int i = 0; i < SimulConsts.M - 1; i++) {
             ordinaryState[i] = OrdinaryStates.CONCENTRATION_SITE;
             ordinarySituation[i] = 'W';
             MDs[i] = 2 + (int) (Math.random() * (SimulConsts.MD - 2) + 1);
         }
 
         // museum
         paintings = new int[SimulConsts.N];
         distances = new int[SimulConsts.N];
         for(int i=0; i<SimulConsts.N; i++)
            distances[i] = SimulConsts.d +(int)(Math.random()* (SimulConsts.D-SimulConsts.d)+1);
 
         // assault parties
         apRoom = new int[2];
         apRoom[0] = -1;
         apRoom[1] = -1;
 
         elements = new int[SimulConsts.M - 1][3];
         for (int i = 0; i < SimulConsts.M - 1; i++) {
             elements[i][0] = -1;
             elements[i][0] = 0;
             elements[i][0] = 0;
         }
 
         // robbed paintings
         robbed = 0;
 
         nEntities = 0;
         reportInitialStatus();
     }
 

    /**
   *   Operation server shutdown.
   *
   *   New operation.
   */

   public synchronized void shutdown () {
        nEntities += 1;
        if (nEntities >= SimulConsts.SHT){
            ServerGeneralRepos.waitConnection = false;
            reportFinalStatus();
        }
          
   }

     /**
      * Set master thief state.
      *
      * @param state master state
      */
 
     public synchronized void setMasterState(int state) {
         masterState = state;
         reportStatus();
     }
 
     /**
      * Set ordinary thief state.
      *
      * @param id    ordinary id
      * @param state ordinary state
      */
 
     public synchronized void setOrdinaryState(int id, int state) {
         ordinaryState[id] = state;
         reportStatus();
     }
 
     /**
      * Set ordinary thief situation
      * @param id of the ordinary thief
      * @param sit situation of the thief
      */
     public synchronized void setOrdinarySituation(int id, char sit) {
         ordinarySituation[id] = sit;
         reportStatus();
     }
 
     /**
      * Get ordinary thieves maximum distances
      * 
      * @param id of the ordinary thief
      * @return md of the ordinary thief
      */
     public synchronized int getOrdinaryMD(int id) {
        return MDs[id];
     }
 
     /**
      * Set assault party room
      * @param ap assault party
      * @param room room to heist
      */
     public synchronized void setApRoom(int ap, int room){
         apRoom[ap] = room;
     }
 
     /**
      * Set Assault Party element
      * 
      * @param elem index
      * @param tid  ordinary thief id
      */
     public synchronized void setApElement(int elem, int tid) {
         elements[elem][0] = tid;
         reportStatus();
     }
 
     /**
      * Set canvas
      * 
      * @param elem   index
      * @param canvas carry
      */
     public synchronized void setCanvas(int elem, int canvas) {
         elements[elem][2] = canvas;
         reportStatus();
     }
 
     /**
      * Update thief pos
      * 
      * @param elem index
      * @param pos  actual pos of the thief
      */
     public synchronized void setPosition(int elem, int pos) {
         elements[elem][1] = pos;
         reportStatus();
     }
 
     /**
      * Set number of paintings in the room
      * 
      * @param paitings on each room
      */
     public synchronized void setRoomPaitings(int[] paitings) {
         this.paintings = paitings;
         reportStatus();
     }
 
     /**
      * Get distance of the rooms
      * 
      * @return distances
      */
     public synchronized int[] getRoomDistances() {
        return distances;
     }
 
     /**
      * Set robbed paintings
      */
     public synchronized void setRobbedPaintings() {
         this.robbed++;
     }
 
     /**
      * Write the header to the logging file.
      */
 
     private void reportInitialStatus() {
         TextFile log = new TextFile(); // instantiation of a text file handler
 
         if (!log.openForWriting(".", logFileName)) {
             GenericIO.writelnString("The operation of creating the file " + logFileName + " failed!");
             System.exit(1);
         }
 
         log.writelnString("                           Heist to the Museum - Description of the internal state\n");
         log.writelnString("  Mstr   Thief 1     Thief 2     Thief 3     Thief 4     Thief 5     Thief 6");
         log.writelnString("Stat    Stat S MD   Stat S MD   Stat S MD   Stat S MD   Stat S MD   Stat S MD");
 
         log.writelnString(
                 "                Assault party 1                         Assault party 2                             Museum");
         log.writelnString(
                 "      Elem 1      Elem 2      Elem 3          Elem 1      Elem 2      Elem 3    Room 1   Room 2   Room 3   Room 4   Room 5");
         log.writelnString(
                 "RId Id Pos Cv   Id Pos Cv   Id Pos Cv   RId Id Pos Cv   Id Pos Cv   Id Pos Cv   NP DT    NP DT    NP DT    NP DT    NP DT");
 
         if (!log.close()) {
             GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
             System.exit(1);
         }
         reportStatus();
     }
 
     /**
      * Write the final status to the logging file.
      */
 
     public void reportFinalStatus() {
         TextFile log = new TextFile();
 
         if (!log.openForAppending(".", logFileName)) {
             GenericIO.writelnString("The operation of opening for appending the file " + logFileName + " failed!");
             System.exit(1);
         }
 
         log.writelnString("My friends, tonight's effort produced " + robbed + " priceless paintings!");
 
         if (!log.close()) {
             GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
             System.exit(1);
         }
     }
 
     /**
      * Write a state line at the end of the logging file.
      */
 
     private void reportStatus() {
         TextFile log = new TextFile(); // instantiation of a text file handler
         String lineStatus = "", line2Status = ""; // state line to be printed
 
         if (!log.openForAppending(".", logFileName)) {
             GenericIO.writelnString("The operation of opening for appending the file " + logFileName + " failed!");
             System.exit(1);
         }
 
         switch (masterState) {
             case MasterStates.PLANNING_THE_HEIST:
                 lineStatus += "PTH  ";
                 break;
             case MasterStates.DECIDING_WHAT_TO_DO:
                 lineStatus += "DWTD ";
                 break;
             case MasterStates.ASSEMBLING_A_GROUP:
                 lineStatus += "AAG  ";
                 break;
             case MasterStates.WAITING_FOR_GROUP_ARRIVAL:
                 lineStatus += "WFGA ";
                 break;
             case MasterStates.PRESENTING_THE_REPORT:
                 lineStatus += "PTR  ";
                 break;
         }
 
         for (int i = 0; i < SimulConsts.M - 1; i++)
             switch (ordinaryState[i]) {
                 case OrdinaryStates.CONCENTRATION_SITE:
                     lineStatus += "   CNS  " + ordinarySituation[i] + "  " + MDs[i];
                     break;
                 case OrdinaryStates.CRAWLING_INWARDS:
                     lineStatus += "   CIN  " + ordinarySituation[i] + "  " + MDs[i];
                     break;
                 case OrdinaryStates.AT_A_ROOM:
                     lineStatus += "   AAR  " + ordinarySituation[i] + "  " + MDs[i];
                     break;
                 case OrdinaryStates.CRAWLING_OUTWARDS:
                     lineStatus += "   COUT  " + ordinarySituation[i] + "  " + MDs[i];
                     break;
                 case OrdinaryStates.COLLECTION_SITE:
                     lineStatus += "   CLS " + ordinarySituation[i] + " " + MDs[i];
                     break;
             }
 
         log.writelnString(lineStatus); // states of thieves
         for (int i = 0; i < SimulConsts.M - 1; i++){
             if(i% SimulConsts.E==0) line2Status += String.format("%2d> ", apRoom[i/SimulConsts.E]);
             line2Status += String.format("%2d  %2d %2d | ", elements[i][0], elements[i][1], elements[i][2]);
         }
 
         for (int i = 0; i < SimulConsts.N; i++)
             line2Status += String.format("%2d %2d    ", paintings[i], distances[i]);
         log.writelnString(line2Status + '\n'); // status of shared regions
 
         if (!log.close()) {
             GenericIO.writelnString("The operation of closing the file " + logFileName + " failed!");
             System.exit(1);
         }
     }

}
