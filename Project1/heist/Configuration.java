package heist;

import heist.utils.Range;

/**
 * This class stores all the configuration values used in this simulation
 */
public class Configuration
{
    /**
     * Number of thieves to be created for this simulation.
     */
    public int numberThieves;
    
    /**
     * Number of rooms inside the museum.
     */
    public int numberRooms;
    
    /**
     * Party size.
     */
    public int partySize;
    
    /**
     * Number of parties in this simulation.
     */
    public int numberParties;
    
    /**
     * Maximum distance between crawling thieves.
     */
    public int distanceBetweenThieves;
            
    /**
     * Random value of possible thief displacements between a range of values
     */
    public Range thiefDisplacement;
    
    /**
     * Random value of room distances inside the museum in this Range
     */
    public Range roomDistance;
    
    /**
     * Random value of paitings inside the room in this Range
     */
    public Range numberPaintings;
    
    /**
     * Flag to enable debug messages.
     */
    public boolean debug;
    
    /**
     * File path to log write log file.
     */
    public String logFile;
    
    /**
     * Flag to set if log is written to a file.
     */
    public boolean logToFile;
    
    /**
     * Print log messages as specified in the document.
     */
    public boolean  log;
    
    /**
     * If true the log header is printed every time.
    */
    public boolean logHeader;
    
    /**
     * If true the log is printed on receive, else the log is store and printed at the end sorted.
     */
    public boolean logImmediate;
    
    /**
     * Config values that will be used in the simulation
     */
    public Configuration()
    {
        this.numberRooms = 5;
        this.numberThieves = 6;
        this.numberParties = 2;
        this.partySize = 3;
        this.distanceBetweenThieves = 3;
        this.thiefDisplacement = new Range(2,6);
        this.roomDistance = new Range(15,30);
        this.numberPaintings = new Range(8,16);
        this.logFile = "logger.txt";
        this.logHeader = true;
        this.logToFile = true;
        this.logImmediate = true;
        this.log = true;
        this.debug = false;
    }
}
