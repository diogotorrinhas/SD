package heist.concurrent.shared;

import heist.Configuration;
import heist.commInfra.MemException;
import heist.commInfra.MemFIFO;
import heist.interfaces.AssaultParty;
import heist.interfaces.ConcentrationSite;
import heist.room.RoomStatus;
import heist.thief.OrdinaryThief;
import heist.interfaces.ControlCollectionSite;
import heist.interfaces.Museum;


public class SharedControlCollectionSite implements ControlCollectionSite
{

    /**
     * Configuration used by this ControlCollectionSite.
     */
    private final Configuration configuration;

    /**
     * Museum used to get information about rooms distance.
     */
    private final Museum museum;

    /**
     * Concentration site
     */
    private ConcentrationSite concentrationSite;

    /**
     * AssaultParties used for the simulation.
     */
    private final AssaultParty[] assaultParties;


    /**
     * List for OrdinaryThieves waiting to deliver a canvas.
     */
    private MemFIFO<OrdinaryThief> waitingToDelieverCanvas;


    /**
     * Is the vision that master thief have about the room
     * RoomStatus array, used to store information (Who are assaulting the rooms, if the room is empty, number paiting stolen from each room...)
     */
    private RoomStatus[] rooms;

    /**
     * Collection site constructor
     * @param assaultParties AssaultParties
     * @param museum Museum
     * @param configuration Config Simulation
     * @throws Exception
     */
    public SharedControlCollectionSite(AssaultParty[] assaultParties, Museum museum, Configuration configuration, ConcentrationSite concentrationSite) throws Exception
    {
        this.configuration = configuration;
        this.assaultParties = assaultParties;
        this.museum = museum;
        try {
            this.waitingToDelieverCanvas = new MemFIFO<>(new OrdinaryThief[configuration.numberThieves]);

        }catch (MemException e){
            System.out.println(e.getMessage());
        }
        this.rooms = null;
        this.concentrationSite = concentrationSite;
    }

    public synchronized int totalPaintingsStolen()
    {
        int sum = 0;


        for(int i = 0; i < this.rooms.length; i++)
        {
            sum += this.rooms[i].getPaintings();
        }

        return sum;
    }


    /**
     * Function used to make MasterThief wait until a OrdinaryThief arrives and wakes him up.
     * @throws InterruptedException
     */
    public synchronized void takeARest() throws InterruptedException
    {
        while(this.waitingToDelieverCanvas.empty())
        {
            this.wait();
        }
    }

    /**
     * Wake up the MasterThief to collect its canvas.
     * The OrdinaryThief enters the queue, wakes up the MasterThief and waits until is waken up.
     * @param thief Thief.
     * @throws java.lang.InterruptedException Exception.
     */
    public synchronized void handACanvas(OrdinaryThief thief) throws InterruptedException, MemException {
        this.waitingToDelieverCanvas.write(thief);
        this.notifyAll();
        while(this.waitingToDelieverCanvas.contains(thief))
        {
            this.wait();
        }
    }

    /**
     * MasterThief gets the canvas brought by OrdinaryThieves and MasterThief wakes up the ordinaryThieves waiting for their canvas to be collect.
     * @throws java.lang.InterruptedException Exception
     */
    public synchronized void collectCanvas() throws InterruptedException, MemException {

        while(this.concentrationSite.getRoomsStatus() == null){
            this.wait();
        }
        this.rooms = this.concentrationSite.getRoomsStatus();
        if(this.waitingToDelieverCanvas.empty() == false)
        {
            OrdinaryThief thief = this.waitingToDelieverCanvas.read();
            AssaultParty party = this.assaultParties[thief.getParty()];
            try
            {
                int targetID = party.getTargetRoomID();
                if(thief.deliverCanvas() == false)
                {
                    this.rooms[targetID].setClear();
                }
                else
                {
                    this.rooms[targetID].deliverPainting();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            this.notifyAll();
        }
    }
}
