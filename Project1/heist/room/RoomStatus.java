package heist.room;

/**
 * Class to represent the room as the MasterThief perceives it.
 */
public class RoomStatus extends Room
{
    /**
     * Stores the number of thieves currently attacking (or on their way to attack) the room.
     */
    private int thievesAttacking;

    /**
     * Flag to mark if the room has been cleared out.
     */
    private boolean clear;

    /**
     * RoomStatus constructor
     * @param id Room id.
     * @param distance Room distance.
     */
    public RoomStatus(int id, int distance)
    {
        super(id, distance, 0);
        
        this.clear = false;
        this.thievesAttacking = 0;
    }

    /**
     * @return True if the room is cleared out.
     */
    public boolean isClear()
    {
        return this.clear;
    }

    /**
     * Incremet the attacking thieves to this room
     * @param thievesAttacking Number of thieves attacking the room.
     */
    public void addThievesAttacking(int thievesAttacking)
    {
        this.thievesAttacking += thievesAttacking;
    }
    

    /**
     * @return True if there is a party assigned to this room.
     */
    public boolean underAttack()
    {
        return this.thievesAttacking > 0;
    }


    /**
     * Deliver painting to this room and reduces the number of thieves attacking the room
     * @throws java.lang.Exception
     */
    public void deliverPainting() throws Exception
    {
        this.thievesAttacking--;
        this.paintings++;
        
        if(this.thievesAttacking < 0)
        {
            throw new Exception("Delivering more canvas than the ammout of thieves that attacked!");
        }
    }

    /**
     * Mark the room as clear.
     * @throws java.lang.Exception
     */
    public void setClear() throws Exception
    {
        this.thievesAttacking--;
        this.clear = true;

        if(this.thievesAttacking < 0)
        {
            throw new Exception("Delivering more canvas than the ammout of thieves that attacked!");
        }
    }
}