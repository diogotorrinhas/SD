package serverSide.objects;

import java.rmi.registry.*;
import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Museum
 *
 *    All public methods are executed in mutual exclusion.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */
public class Museum implements MuseumInterface {
    /**
     * Number of paintings hanging in each room
     */
    private int[] paintings;

    /**
     *  Ordinary threads.
     */
    private final Thread [] ord;

    /**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     * General repository.
     */
    private final GeneralReposInterface reposStub;

    /**
     * Museum instantiation
     *
     * @param reposStub reference to the general repository
     */
    public Museum(GeneralReposInterface reposStub) {

        this.reposStub = reposStub;
        this.paintings = new int[SimulConsts.N];
        for(int i=0; i<SimulConsts.N; i++) 
            paintings[i] = SimulConsts.p +(int)(Math.random() * (SimulConsts.P-SimulConsts.p)+1); 
            try{
                reposStub.setRoomPaitings(paintings);
            }catch (RemoteException e) { 
                GenericIO.writelnString ("museum remote exception on setRoomPaitings: " + e.getMessage ());
                System.exit (1);
              }  

            ord = new Thread [SimulConsts.M-1];
            for (int i = 0; i < SimulConsts.M-1; i++)
                ord[i] = null;
            nEntities = 0;
    }

    /**
     * The thief picks a canvas and roll it
     * 
     * @param room where assault is happening
     * @param ap assault party
     * @param members id member
     * @return number of canvas stolen by the thieve
     */
    public synchronized int rollACanvas(int room, int ap, int members) throws RemoteException{

        if(paintings[room]>0) {
            paintings[room]--;
            reposStub.setRoomPaitings(paintings);
            reposStub.setCanvas(ap * SimulConsts.E + members, 1);
            return 1;
        }
        reposStub.setCanvas(ap * SimulConsts.E + members, 0);
        return 0;
    }


    /**
   *   Operation server shutdown.
   *
   *   New operation.
   */
   public synchronized void shutdown () throws RemoteException{
       nEntities += 1;
       if (nEntities >= SimulConsts.SHT-1)
          ServerMuseum.shutdown();
       notifyAll ();                                        // the barber may now terminate
   }
}
