package heist.interfaces;

import heist.room.RoomStatus;
import heist.thief.OrdinaryThief;

/**
 * The concentration site is where OrdinaryThieves wait for the MasterThief to assign them a AssaultParty.
 */
public interface ConcentrationSite
{
    /**
     * Auxiliar function to build prepareAssaultParty (called by master thief)
     * Add thieves to an assault party with partyID
     * @param partyID party to be filled with thieves
     * @throws Exception
     */
    public void addThievesToParty(int partyID) throws Exception;

    public boolean roomsAreClear();

    /**
     * The last OrdinaryThief to join the assault party wakes up the MasterThief
     * @param thief to be added to the party
     * @throws Exception
     * @return the id of the assault Party group assign to the thief
     */
    public int prepareExcursion(OrdinaryThief thief) throws Exception;

    /**
     * Called by the MasterThief to get its next state
     * @return MasterThief next state
     * @throws Exception
     */
    public int appraiseSit() throws Exception;

    /**
     Called by the MasterThief to terminate the simulation and tell the results to the OrdinaryThieves.
     * @throws Exception
     */
    public void sumUpResults() throws Exception;


    /**
     * Auxiliar function to build prepareAssaultParty (called by master thief)
     * Create an assaultParty
     * @param room Target room.
     * @return party ID of the assaultParty created
     * @throws Exception
     */
    public int createAssaultParty(RoomStatus room) throws Exception;

    /**
     * Auxiliar function to build prepareAssaultParty (called by master thief)
     * @return Room to attack next.
     * @throws Exception
     */
    public RoomStatus getRoomToAttack() throws Exception;

    /**
     * Called by the OrdinaryThieves to check if they are still needed.
     * @param thief Thief checking if he is needed.
     * @throws Exception
     * @return True if the thief is still needed, false otherwise.
     */
    public boolean amINeeded(OrdinaryThief thief) throws Exception;

    public void startOperations() throws Exception;

    public RoomStatus[] getRoomsStatus();

    /**
     * Called by the MasterThief to end the simulation.
     * @throws Exception
     */
    public default void end() throws Exception {}
}
