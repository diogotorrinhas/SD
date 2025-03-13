package heist.interfaces;

import heist.room.Room;

/**
 * Museum has rooms inside of it, the OrdinaryThieves attack the Museum to stole the paintings hanging in those rooms.
 */
public interface Museum
{
    /**
     * @return Array with the Rooms
     * @throws Exception
     */
    public Room[] getRooms() throws Exception;

    /**
     * Called by the OrdinaryThieves to roll a canvas.
     * @param id Room id where the thief is getting the canvas
     * @return True if he gets the canvas or false if room is empty
     * @throws Exception
     */
    public boolean rollACanvas(int id) throws Exception;

    /**
     * MasterThief call this function to end the simulation.
     * @throws Exception
     */
    public default void end() throws Exception {}
}
