package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type ControlCollectionSite.
 *
 *     It provides the functionality to access the ControlCollectionSite.
 */

public interface ControlCollectionSiteInterface extends Remote {

    /**
     * Get room index.
     * @return room index
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public int getRoomIdx() throws RemoteException;


    /**
     * Master start operations
     * @return state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public int startOperation() throws RemoteException;


    /**
     * Master take a rest until return of the ordinary thieves
     * @return state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public int takeARest() throws RemoteException;


    /**
     * Ordinary thieves hands the canvas to the master thief (if it has one)
     * @param canvas possession
     * @param room of the museum
     * @param ap assault party id
     * @param members id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void handACanvas(int canvas, int room, int ap, int members) throws RemoteException;


    /**
     * The master thief collects a canvas from ordinary thief
     * @return state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public int collectACanvas() throws RemoteException;


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
