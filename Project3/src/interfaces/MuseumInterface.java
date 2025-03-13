package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type Museum.
 *
 *     It provides the functionality to access the Museum.
 */

public interface MuseumInterface extends Remote {

    /**
     * The thief picks a canvas and roll it
     *
     * @param room of the museum
     * @param ap assault party
     * @param members thieves
     * @return canvas
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
    */
    public int rollACanvas(int room, int ap, int members) throws RemoteException;


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
