package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import clientSide.stubs.*;


public class Museum {
    /**
     * Number of paintings in each room
     */
    private int[] paintings;

    /**
     *  ordinary threads.
     */
    private final MuseumClientProxy [] ord;

    /**
     *  Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     * general repository.
     */
    private final GeneralReposStub reposStub;

    /**
     * Museum instantiation
     *
     * @param reposStub reference to the general repository
     */
    public Museum(GeneralReposStub reposStub) {
        this.reposStub = reposStub;
        this.paintings = new int[SimulConsts.N];
        for(int i=0; i<SimulConsts.N; i++) 
            paintings[i] = SimulConsts.p +(int)(Math.random() * (SimulConsts.P-SimulConsts.p)+1); 
            reposStub.setRoomPaitings(paintings);

            ord = new MuseumClientProxy [SimulConsts.M-1];
            for (int i = 0; i < SimulConsts.M-1; i++)
                ord[i] = null;
            nEntities = 0;
    }

    /**
     * The thief picks a canvas and roll it
     * 
     * @param room target room where the assault takes place
     * @param ap assault party
     * @param members id member
     * @return number of canvas
     */
    public synchronized int rollACanvas(int room, int ap, int members) {
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

   public synchronized void shutdown () {
       nEntities = nEntities + 1;
       if (nEntities >= SimulConsts.SHT-1)
          ServerMuseum.waitConnection = false;
       notifyAll ();
   }
}
