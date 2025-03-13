package heist.concurrent.shared;

import heist.room.Room;
import heist.Configuration;
import heist.interfaces.Museum;


public class SharedMuseum implements Museum
{

    /**
     * Rooms inside the museum.
     */
    private final Room[] rooms;

    /**
     * Museum constructor
     * @param configuration is the config values of this simulation
     */
    public SharedMuseum(Configuration configuration)
    {
        this.rooms = new Room[configuration.numberRooms];
        
        for(int i = 0; i < this.rooms.length; i++)
        {

            int distance = configuration.roomDistance.generateInRange();
            int paintings = configuration.numberPaintings.generateInRange();
            this.rooms[i] = new Room(i, distance, paintings);
        }
        System.out.println("Museum has " + this.countPaintings() + " paintings!");
    }

    public Room[] getRooms()
    {
        return this.rooms;
    }
    
    /**
     * @return Number of paitings in the museum
     */
    private synchronized int countPaintings()
    {
        int sum = 0;
        
        for(int i = 0; i < this.rooms.length; i++)
        {
            sum += this.rooms[i].getPaintings();
        }
        
        return sum;
    }

    public synchronized boolean rollACanvas(int id)
    {
        return this.rooms[id].rollACanvas();
    }
}
