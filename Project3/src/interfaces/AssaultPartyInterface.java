package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type AssaultParty.
 *
 *     It provides the functionality to access the AssaultParty.
 */
public interface AssaultPartyInterface extends Remote {

    /**
     * Assign a member to the assault party.
     * @param ap assault party
     * @param oid ordinary id
     * @return member id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public int assignMember(int ap, int oid) throws RemoteException;


    /**
     * Get room ID.
     * @return room id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public int getRoom() throws RemoteException;


    /**
     * Operation Reverse Direction of the thieves.
     * @param member thieve id
     * @param oid ordinary id
     * @return state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public int reverseDirection(int member, int oid) throws RemoteException;


    /**
     * Operation send assault party.
     * @param room
     * @return state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public int sendAssaultParty(int room) throws RemoteException;


    /**
     * Crawl in movement
     * @param ap assault party
     * @param member id
     * @param md ordinady maximum distance
     * @param oid ordinary id
     * @return reach room + state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public ReturnBoolean crawlIn(int ap, int member, int md, int oid) throws RemoteException;


    /**
     * Crawl out movement
     * @param ap assault party
     * @param member id
     * @param md ordinady maximum distance
     * @param oid ordinary id
     * @return reach room + state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry service fails
     */
    public ReturnBoolean crawlOut(int ap, int member, int md, int oid) throws RemoteException;


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
