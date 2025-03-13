package heist.concurrent.shared;

import heist.Configuration;
import heist.commInfra.MemException;
import heist.interfaces.Museum;
import heist.room.Room;
import heist.room.RoomStatus;
import heist.thief.MasterThief;
import heist.thief.OrdinaryThief;
import heist.interfaces.AssaultParty;
import heist.interfaces.ConcentrationSite;
import heist.commInfra.MemFIFO;
import heist.commInfra.*;


/**
 * The concentration site is where OrdinaryThieves wait for the MasterThief to assign them a AssaultParty.
 */
public class SharedConcentrationSite implements ConcentrationSite
{
    /**
     * Simulation configuration.
     */
    private final Configuration configuration;

    /**
     * List of thieves waiting to enter in an assaultParty.
     */
    private final MemFIFO<OrdinaryThief> waitingThieves;


    /**
     * List of parties waiting to be filled with thieves.
     */
    private final MemFIFO<Integer> waitingParties;


    /**
     * AssaultParties
     */
    private final AssaultParty[] assaultParties;

    /**
     * Flag to indicate if the heist has been terminated.
     * True when the MasterThieve decides that the Museum is clear.
     */
    private boolean heistOver;

    /**
     * Museum used to get information about rooms distance.
     */
    private final Museum museum;

    /**
     * List for OrdinaryThieves waiting to check if they are still needed.
     */
    private Queue<OrdinaryThief> amINeededQueue;

    /**
     * RoomStatus array, used to store information about who are assaulting rooms, how many paintings where stolen from each room and if the room is empty.
     */
    private RoomStatus[] rooms;

    /**
     * ConcentrationSite constructor.
     * @param parties AssaultParties
     * @param configuration Configuration to be used in the simulation.
     */
    public SharedConcentrationSite(AssaultParty[] parties, Configuration configuration,Museum museum) throws MemException {
        MemFIFO<Integer> waitingParties1;
        MemFIFO<OrdinaryThief> waitingThieves1;
        this.assaultParties = parties;
        this.configuration = configuration;
        this.museum = museum;
        this.amINeededQueue = new ArrayQueue<>(configuration.numberThieves);
        try {
            waitingParties1 = new MemFIFO<>(new Integer[configuration.numberParties]);
            waitingThieves1 = new MemFIFO<>(new OrdinaryThief[configuration.numberThieves]);
        }catch (MemException e){
            System.out.println(e.getMessage());
            waitingThieves1 = null;
            waitingParties1 = null;
        }
        this.waitingParties = waitingParties1;
        this.waitingThieves = waitingThieves1;
        this.rooms = null;
        this.heistOver = false;
    }

    /**
     * Called by the master thief to verify where are the rooms inside the museum.
     * @throws Exception
     */
    public synchronized void startOperations() throws Exception
    {
        Room[] museumRooms = this.museum.getRooms();

        this.rooms = new RoomStatus[museumRooms.length];
        for(int i = 0; i < museumRooms.length; i++)
        {
            this.rooms[i] = new RoomStatus(museumRooms[i].getID(), museumRooms[i].getDistance());
            //System.out.println(this.rooms[i].getID());
        }
        //System.out.println("checkpoint");
    }
    @Override
    public synchronized void addThievesToParty(int partyID) throws Exception
    {
        if(this.waitingParties.contains(partyID) == true)
        {
            throw new Exception("Party already in the list " + partyID);
        }
        this.waitingParties.write(partyID);
        this.notifyAll();
        //Party not full, we wait until it's full
        while(this.assaultParties[partyID].partyFull() == false)
        {
            this.wait();
        }
        //Party full, we remove it
        this.waitingParties.read();
    }

    @Override
    public synchronized int prepareExcursion(OrdinaryThief thief) throws Exception
    {
        this.waitingThieves.write(thief);
        while(this.waitingParties.size() == 0)
        {
            this.wait();
        }
        int partyID = this.waitingParties.peek();
        if (this.assaultParties[partyID].partyFull()) {
            partyID++;
        }
        this.waitingThieves.read();
        this.assaultParties[partyID].addThief(thief);

        if(this.assaultParties[partyID].partyFull() == true)
        {
            this.notifyAll();
        }
        return partyID;
    }

    /**
     * @return True if there is some party available.
     * @throws Exception
     */
    public synchronized boolean partyAvailable() throws Exception
    {
        for(int i = 0; i < this.assaultParties.length; i++)
        {
            if(this.assaultParties[i].getAssaultPartyState() == SharedAssaultParty.DISMISSED)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @return True if all rooms are clear.
     */
    public synchronized boolean roomsAreClear()
    {
        for(int i = 0; i < this.rooms.length; i++)
        {
            if(this.rooms[i].isClear() == false || this.rooms[i].underAttack() == true)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @return Next room to assign, otherwise null if all the rooms are assign to a party
     */
    public synchronized RoomStatus getNextTargetRoom()
    {
        for(int i = 0; i < this.rooms.length; i++)
        {
            if(this.rooms[i].isClear()== false && this.rooms[i].underAttack() == false)
            {
                return this.rooms[i];
            }
        }
        return null;
    }

    /**
     * @return Returns a room that would be attacked
     */
    public synchronized RoomStatus getRoomToAttack()
    {
        RoomStatus room = this.getNextTargetRoom();
        room.addThievesAttacking(configuration.partySize);
        return room;
    }

    /**
     * @return True if there is a room under attack.
     */
    private synchronized boolean thievesAttackingRooms()
    {
        for(int i = 0; i < this.rooms.length; i++)
        {
            if(this.rooms[i].underAttack() == true)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @return True if is possible to create a new party
     * @throws Exception Exception
     */
    public synchronized boolean isPossibleToCreateParty() throws Exception
    {
        if (this.getNextTargetRoom() != null && this.amINeededQueue.size() >= this.configuration.partySize && this.partyAvailable() == true){
            return true;
        }
        return false;
    }

    public synchronized boolean amINeeded(OrdinaryThief thief) throws InterruptedException, MemException {
        this.amINeededQueue.push(thief);
        this.notifyAll();
        do
        {
            this.wait();
        }
        while(this.amINeededQueue.contains(thief));
        if(this.heistOver == true){
            return false;
        }
        return true;
    }

    @Override
    public synchronized int createAssaultParty(RoomStatus room) throws Exception
    {
        for(int i = 0; i < this.assaultParties.length; i++)
        {
            AssaultParty party = this.assaultParties[i];
            if(party.getAssaultPartyState() == SharedAssaultParty.DISMISSED)
            {
                party.assignARoom(room);
                return party.getAssaultPartyID();
            }
        }
        return -1;
    }

    /**
     * MasterThief decides what should be the next state of the ordinaryThief
     * @return New state of the OrdinaryThief
     * @throws Exception Exception
     */
    public synchronized int appraiseSit() throws Exception
    {
        if(this.roomsAreClear())
        {
            while(this.amINeededQueue.size() < this.configuration.numberThieves)
            {
                this.wait();
            }
            this.notifyAll();
            return MasterThief.PRESENTING_THE_REPORT;
        }
        else if(this.isPossibleToCreateParty())
        {
            for(int i = 0; i < this.configuration.partySize; i++)
            {
                this.amINeededQueue.pop();
            }
            this.notifyAll();
            return MasterThief.ASSEMBLING_A_GROUP;
        }
        else if(this.thievesAttackingRooms())
        {
            return MasterThief.WAITING_FOR_GROUP_ARRIVAL;
        }
        this.wait();
        return MasterThief.DECIDING_WHAT_TO_DO;
    }

    /**
     * MasterThief indicate the results of the heist and terminates it
     */
    public synchronized void sumUpResults()
    {
        this.heistOver = true;
        this.amINeededQueue.clear();
        this.notifyAll();
    }

    public RoomStatus[] getRoomsStatus(){
        return this.rooms;
    }
}
