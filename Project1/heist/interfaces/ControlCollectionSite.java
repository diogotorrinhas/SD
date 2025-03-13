package heist.interfaces;

import heist.thief.OrdinaryThief;

/**
 * The ControlCollection site is where the OrdinaryThieves deliver the canvas to the MasterThief.
 */
public interface ControlCollectionSite
{
    /**
     * Called by the OrdinaryThief to be added to the collectionSite and wake up the MasterThief to collect its canvas.
     * @param thief Thief that is going to be added
     * @throws Exception
     */
    public void handACanvas(OrdinaryThief thief) throws Exception;

    /**
     * Called by the MasterThief to wait until the OrdinaryThieves return from the museum.
     * @throws Exception
     */
    public void takeARest() throws Exception;

    /**
     * Called by MasterThief to collect canvas from ordinaryThieves
     * MasterThief wakes up the ordinary thieves waiting for their canvas to be collected
     * @throws Exception
     */
    public void collectCanvas() throws Exception;


    /**
     * @return Number of paintings.
     * @throws Exception
     */
    public int totalPaintingsStolen() throws Exception;


    /**
     * Called by the MasterThief to end the simulation.
     * @throws Exception
     */
    public default void end() throws Exception {}
}
