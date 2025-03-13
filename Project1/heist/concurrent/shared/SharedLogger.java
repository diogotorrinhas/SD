package heist.concurrent.shared;

import heist.Configuration;
import heist.interfaces.AssaultParty;
import heist.interfaces.ControlCollectionSite;
import heist.room.Room;
import heist.thief.MasterThief;
import heist.thief.OrdinaryThief;
import heist.interfaces.Logger;
import heist.interfaces.Museum;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.PrintStream;
import java.util.LinkedList;
import heist.utils.Log;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class SharedLogger implements Logger
{

    /**
     * Configuration object.
     */
    private Configuration configuration;
    
    /**
     * Museum to be assaulted by AssaultParties.
     */
    private Museum museum;
    
    /**
     * AssaultParties to be used in the simulation.
     */
    private AssaultParty[] parties;
    
    /**
     * MasterThieve that controls and assigns OrdinaryThieves to AssaultParties.
     */
    private MasterThief master;
    
    /**
     * OrdinaryThieves array
     */
    private OrdinaryThief[] thieves;
    
    /**
     * CollectionSite
     */
    private ControlCollectionSite controlCollection;
    
    /**
     * PrintStream used to output logging data.
     */
    private PrintStream out;
    
    /**
     * Queue with all messages waiting to be sorted.
     */
    private LinkedList<Log> list;
    
    /**
     * Constructor
     * Configuration file specifies where the log data is written to.
     * @param parties AssaultParties
     * @param museum Museum
     * @param controlCollection ControlCollectionSite
     * @param configuration Configuration
     */
    public SharedLogger(AssaultParty[] parties, Museum museum, ControlCollectionSite controlCollection, Configuration configuration)
    {
        this.configuration = configuration;
        
        this.thieves = new OrdinaryThief[configuration.numberThieves];
        this.master = null;
        this.parties = parties;
        this.museum = museum;
        this.controlCollection = controlCollection;
        
        this.list = new LinkedList<>();
        
        if(this.configuration.logToFile)
        {
            try
            {
                this.out = new PrintStream(new File(this.configuration.logFile));
            }
            catch(FileNotFoundException e){}
        }
        else
        {
            this.out = System.out;
        }
    }
    
    /**
     * Write message directly to the PrintStream.
     * Flushes the PrintStream after every message.
     * @param message Message to display.
     */
    public synchronized void debug(String message)
    {                
        if(this.configuration.debug)
        {
            this.out.println(message);
        }
    }
    
    /**
     * Updates the MasterThief information and creates new log entry.
     * @param master MasterThief
     */
    @Override
    public synchronized void log(MasterThief master) throws Exception
    {
        this.master = master;
        this.log();
    }
    
    /**
     * Updates the OrdinaryThief information and creates new log entry.
     * @param thief OrdinaryThief
     */
    @Override
    public synchronized void log(OrdinaryThief thief) throws Exception
    {        
        this.thieves[thief.getID()] = thief;
        this.log();
    }
    
    /**
     * Create a log entry of everything
     * Flushes after log has been written.
     * @throws Exception Exception
     */
    private void log() throws Exception
    {        
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(stream);
        int timestamp = 0;

        if(this.configuration.log)
        {
            //Thieves header
            if(this.configuration.logHeader)
            {
                out.print("\n\nMstT      ");
                for(int i = 0; i < this.thieves.length; i++)
                {
                    out.print("Thief " + (i+1) + "      ");
                }
                
                
                out.print("\nStat     ");
                for(int i = 0; i < this.thieves.length; i++)
                {
                    out.print("Stat S MD    ");
                }
            }
            
            //Master thief state
            String stateM = "";
            if(this.master == null)
            {
                out.print("\n----     ");
            }
            else {

                switch (master.state()) {

                    case 1000:

                        stateM = "PTHT";
                        break;
                    case 2000:
                        stateM = "DWTD";
                        break;
                    case 3000:

                        stateM = "ASAG";
                        break;
                    case 4000:

                        stateM = "WGAR";
                        break;
                    case 5000:

                        stateM = "PTRP";
                        break;

                }
                out.print("\n" + stateM + "     ");//
            }


            String stateOT = " ";
            //Ordinary thieves state
            for(int i = 0; i < this.thieves.length; i++)

            {

                if(this.thieves[i] == null)
                {
                    out.printf("---- - --    ");
                }
                else
                {
                    switch (thieves[i].state()) {
                        case 1000:
                            stateOT = "CNST"; // Concentration Site
                            break;
                        case 2000:
                            stateOT = "CRIN"; // Crawling Inwards
                            break;
                        case 3000:
                            stateOT = "ATAR"; // At A Room
                            break;
                        case 4000:
                            stateOT = "CROT"; // Crawling Outwards
                            break;
                        case 5000:
                            stateOT = "CLST"; // Collection Site
                            break;
                        default:
                            stateOT = "UNK";
                            break;

                    }
                    out.printf("%4s %c %2d    ", stateOT, this.thieves[i].hasParty(), this.thieves[i].getDisplacement());
                }
            }
            out.print("\n");
            
            //Assault party header
            if(this.configuration.logHeader)
            {
                out.print("\n");
                for(int i = 0; i < this.parties.length; i++)
                {
                    out.print("              Assault party " + (this.parties[i] != null ? this.parties[i].getAssaultPartyID()+1 : "--") + "        ");
                }
                out.print("                 Museum");

                out.print("\n");
                for(int i = 0; i < this.parties.length; i++)
                {
                    out.print("   ");
                    for(int j = 0; j < this.configuration.partySize; j++)
                    {
                        out.print("     Elem " + (j+1));
                    }
                }
                
                out.print("   ");
                for(int j = 0; j < this.configuration.numberRooms; j++)
                {
                    out.print("  Room " + (j+1));
                }

                out.print("\n");
                for(int i = 0; i < this.parties.length; i++)
                {
                    out.print("RId  ");
                    for(int j = 0; j < this.configuration.partySize; j++)
                    {
                        out.print("Id Pos Cv  ");
                    }
                }

                for(int j = 0; j < this.configuration.numberRooms; j++)
                {
                    out.print(" NP DT  ");
                }

                out.print("\n");
            }
            
            //Assault party state
            for(int i = 0; i < this.parties.length; i++)
            {
                if(this.parties[i].getAssaultPartyState() == SharedAssaultParty.DISMISSED)
                {
                    out.print("--   ");
                    for(int j = 0; j < this.configuration.partySize; j++)
                    {
                        out.print("-- --  --  ");
                    }
                }
                else
                {
                    out.printf("%2d   ", this.parties[i].getTargetRoomID());

                    int[] thievesID = this.parties[i].getThieves();
                    
                    for(int j = 0; j < this.configuration.partySize; j++)
                    {
                        if(j < thievesID.length && this.thieves[thievesID[j]] != null)
                        {
                            OrdinaryThief thief = this.thieves[thievesID[j]];
                            out.printf("%2d %2d  %2d  ", thief.getID(), thief.getPosition(), thief.hasCanvas());
                        }
                        else
                        {
                            out.print("-- --  --  "); 
                        }
                    }
                }
            }
            
            //Museum state
            Room[] rooms = this.museum.getRooms();
            for(int i = 0; i < rooms.length; i++)
            {
                out.printf(" %2d %2d  ", rooms[i].getPaintings(), rooms[i].getDistance());
            }
            
            out.println("");
            out.flush();
        }
        
        if(this.configuration.logImmediate)
        {
            this.out.println(new String(stream.toByteArray(), StandardCharsets.UTF_8));
        }
        else
        {
            Log log = new Log(new String(stream.toByteArray(), StandardCharsets.UTF_8), timestamp);
            this.list.push(log);
        }
    }
    
    /**
     * End the log and close the internal PrintStream.
     * @throws Exception Exception
     */
    public synchronized void end() throws Exception
    {
        int paintings = this.controlCollection.totalPaintingsStolen();
        //Sort and print log entries
        if(!this.configuration.logImmediate)
        {
            this.list.sort(null);

            java.util.Iterator<Log> it = this.list.iterator();
            while(it.hasNext())
            {
                this.out.println(it.next().message);
            }
        }
        System.out.println("Info: My friends, tonight's effort produced " + paintings + " priceless paintings!");
        this.out.println("\nMy friends, tonight's effort produced " + paintings + " priceless paintings!");
        this.out.close();
    }
}
