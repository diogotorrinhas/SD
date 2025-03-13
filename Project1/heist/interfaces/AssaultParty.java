package heist.interfaces;

import heist.room.RoomStatus;
import heist.thief.OrdinaryThief;

/**
 * AssaultParty represents a group of OrdinaryThieves attacking the museum.
 */
public interface AssaultParty
{
    /**
     * AssaultParty Waiting state.
     */
    public static final int WAITING = 1000;

    /**
     * AssaultParty Dismissed state.
     * This state represents when the last ordinary thief delievers the canvas.
     */
    public static final int DISMISSED = 4000;
    
    /**
     * AssaultParty Crawling IN state.
     */
    public static final int CRAWLING_IN = 2000;

    
    /**
     * AssaultParty Crawling out state.
     */
    public static final int CRAWLING_OUT = 3000;


    /**
     * @return The target room ID
     * @throws Exception
     */
    public int getTargetRoomID() throws Exception;

    /**
     * @return Party ID
     * @throws Exception
     */
    public int getAssaultPartyID() throws Exception;

    /**
     * @return Array with the id's of the thieves that belongs to this assault party
     * @throws Exception
     */
    public int[] getThieves() throws Exception;

    /**
     * @return AssaultParty state.
     * @throws Exception
     */
    public int getAssaultPartyState() throws Exception;

    /**
     * @return True if the party is full.
     * @throws Exception
     */
    public boolean partyFull() throws Exception;

    /**
     * Besides assigning a room, it sets the state of the party to WAITING
     * @param room Target Room.
     * @throws Exception
     */
    public void assignARoom(RoomStatus room) throws Exception;

    /**
     * Add thief to party
     * @param thief is the thief to be added to the assault party
     * @throws Exception
     */
    public void addThief(OrdinaryThief thief) throws Exception;

    /**
     * Remove thief from party.
     * @param id ID of the thief to be removed
     * @throws Exception
     */
    public void removeThief(int id) throws Exception;

    /**
     * @param thief Thief
     * @return Position of the thief to be updated
     * @throws Exception
     */
    public int crawlIn(OrdinaryThief thief) throws Exception;

    /**
     * @param thief Thief
     * @return Position of the thief to be updated
     * @throws Exception
     */
    public int crawlOut(OrdinaryThief thief) throws Exception;

    /**
     * Called by the MasterThief to send this party to the museum.
     * Party can start crawling after this method was called.
     * @throws Exception A exception may be thrown depending on the implementation.
     */
    public void sendAssaultParty() throws Exception;

    /**
     * @param thief Thief
     * @return True if the thief still did can crawl, i.e, thief did not reach the destination
     * @throws Exception
     */
    public boolean canICrawl(OrdinaryThief thief) throws Exception;
    

    /**
     * @param thief Thief that is going to reverse direction
     * @throws Exception
     */
    public void reverseDirection(OrdinaryThief thief) throws Exception;

    
    /**
     * MasterThief call this function end the simulation.
     * @throws Exception
     */
    public default void end() throws Exception {}
}
