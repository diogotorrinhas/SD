package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type ConcentrationSite.
 *
 *     It provides the functionality to access the ConcentrationSite.
 */

public interface ConcentrationSiteInterface extends Remote {

    /**
     * Get room assign to the assault party
     * @param ap assault party
     * @return room id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public int getRoom(int ap) throws RemoteException;


    /**
     * Return one assault party available.
     * @return assult party id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public int getAssautlParty() throws RemoteException;


    /**
     * Master thief appraise the situation
     * @param roomState of the museum
     * @return master decision
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public int appraiseSit(boolean roomState) throws RemoteException;


    /**
     * The master thief prepares an assault party
     * @param ap assault party
     * @param room to heist
     * @return thief state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public int prepareAssaultParty(int ap, int room) throws RemoteException;


    /**
     * Ordinary thief prepare excursion
     * @param oid ordinary id
     * @return assault party and state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public ReturnInt prepareExcursion(int oid) throws RemoteException;


    /**
     * Ordinary thief indicates to the master that he is free
     * @param ap assault party from last heist
     * @param oid ordinary id
     * @return true if thief has work to do and state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public ReturnBoolean amINeeded(int ap, int oid) throws RemoteException;


    /**
     * Sum up the results from the heist
     * @return state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public int sumUpResults() throws RemoteException;

    /**
   *  Operation end of work.
   *
   *   @param ordId ordinary id
   *   @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
   */

   public void endOperation (int ordId) throws RemoteException;

    /**
     *   Operation server shutdown.
     *
     *   New operation.
     *
     *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
   public void shutdown () throws RemoteException;
    
}
