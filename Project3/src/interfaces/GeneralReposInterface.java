package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type GeneralRepository.
 *
 *     It provides the functionality to access the GeneralRepository.
 */

public interface GeneralReposInterface extends Remote {

    /**
     * Set master thief state.
     *
     * @param state of the mater
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void setMasterState(int state) throws RemoteException;


    /**
     * Set ordinary thief state.
     *
     * @param id of the ordinary
     * @param state of the ordinary
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void setOrdinaryState(int id, int state) throws RemoteException;


    /**
     * Set ordinary thief situation
     *
     * @param id of the ordinary
     * @param sit situation
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void setOrdinarySituation(int id, char sit) throws RemoteException;


    /**
     * Get ordinary thieves maximum distances
     *
     * @param id of the ordinary
     * @return ordinary md
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public int getOrdinariesMD(int id) throws RemoteException;


    /**
     * Set assault party room
     *
     * @param ap assault party
     * @param room id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void setApRoom(int ap, int room) throws RemoteException;


    /**
     * Set Assault Party element.
     *
     * @param elem id
     * @param tid thiefe id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void setApElement(int elem, int tid) throws RemoteException;


    /**
      * Set canvas
      * 
      * @param elem   index
      * @param canvas carry
      * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
      */
      public void setCanvas(int elem, int canvas) throws RemoteException;


    /**
     * Update thief pos
     *
     * @param elem id 
     * @param pos thieve position
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void setPosition(int elem, int pos) throws RemoteException;


    /**
     * Set number of paintings in the room
     *
     * @param paitings number on the museum
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void setRoomPaitings(int[] paitings) throws RemoteException;


    /**
     * Get distance of the rooms
     *
     * @return distance to each museum room
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public int[] getRoomDistances() throws RemoteException;


    /**
     * Set robbed paintings
     *
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public void setRobbedPaintings() throws RemoteException;

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
