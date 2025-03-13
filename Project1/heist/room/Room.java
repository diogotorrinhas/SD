package heist.room;

/**
 * Rooms that contains paintings and is a shared region accessed by all the thieves inside the museum.
 */
public class Room
{
    /**
     * Room identification.
     */
    private final int id;

    /**
     * Number of paintings currently inside the room.
     */
    protected int paintings;

    /**
     * Room distance inside the museum.
     */
    private final int distance;

    /**
     * Room constructor
     * @param id Room id.
     * @param distance Room distance.
     * @param paintings Amount of paintings inside the room.
     */
    public Room(int id, int distance, int paintings)
    {
        this.id = id;
        this.distance = distance;
        this.paintings = paintings;
    }

    /**
     * Get room position inside the museum.
     * @return Room position.
     */
    public int getDistance()
    {
        return this.distance;
    }

    /**
     * Get room paintings.
     * @return Room paintings.
     */
    public int getPaintings()
    {
        return this.paintings;
    }

    /**
     * Get room ID.
     * @return Room id.
     */
    public int getID()
    {
        return this.id;
    }

    /**
     * Remove a painting from the room.
     * @return True if the painting was removed.
     */
    public boolean rollACanvas()
    {
        if(this.paintings > 0)
        {
            this.paintings--;
            return true;
        }

        return false;
    }
}
