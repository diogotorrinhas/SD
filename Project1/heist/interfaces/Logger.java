package heist.interfaces;

import heist.thief.MasterThief;
import heist.thief.OrdinaryThief;

/**
 * Logger object is used to create a detailed log of everything inside the GeneralRepository.
 */
public interface Logger
{
    /**
     * Write message directly to the PrintStream.
     * @param message Message to display.
     * @throws Exception
     */
    public void debug(String message) throws Exception;

    /**
     * Called by the MasterThief to create a log entry using the data sent
     * Flushes after log has been written.
     * @param master MasterThief to be updated.
     * @throws Exception
     */
    public void log(MasterThief master) throws Exception;

    /**
     * Called by the OrdinaryThief to create a log entry using the data sent.
     * @param thief OrdinaryThief to be updated.
     * @throws Exception
     */
    public void log(OrdinaryThief thief) throws Exception;

    /**
     * End the log and close the internal PrintStream.
     * @throws Exception
     */
    public void end() throws Exception;
}
