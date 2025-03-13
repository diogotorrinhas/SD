package heist.concurrent.shared;

import heist.Configuration;
import heist.commInfra.MemException;
import heist.commInfra.MemFIFO;
import heist.thief.OrdinaryThief;
import heist.interfaces.AssaultParty;
import heist.room.RoomStatus;
import java.util.Iterator;


public class SharedAssaultParty implements AssaultParty
{
    /**
     * Assault Party ID.
     */
    private final int id;

    /**
     * Maximum distance between crawling OrdinaryThieves.
     */
    private final int distanceBetweenThieves;
    
    /**
     * Party size.
     */
    private final int assaultPartySize;

    /**
     * Target room.
     */
    private RoomStatus room;
    
    /**
     * OrdinaryThieves in the party.
     */
    private  MemFIFO<OrdinaryThief> thieves;

    /**
     * Crawling Queue.
     */
    private  MemFIFO<Integer> crawlingQueue;

    /**
     * Number of OrdinaryThieves waiting to reverse direction.
     */
    private int OTwaitingToReverse;
    
    /**
     * Assault Party state.
     */
    private int assaultPartystate;
    
    /**
     * AssaultParty constructor
     * @param id AssaultParty id.
     * @param configuration Config used in this simulation
     */
    public SharedAssaultParty(int id, Configuration configuration)
    {
        MemFIFO<Integer> crawlingQueue1;
        MemFIFO<OrdinaryThief> thieves1;
        this.id = id;
        try{
            this.thieves = new MemFIFO<>(new OrdinaryThief[configuration.partySize]);
            this.crawlingQueue = new MemFIFO<>(new Integer[configuration.partySize]);
        }catch (MemException e){
            System.out.println(e.getMessage());
        }
        this.room = null;
        this.assaultPartySize = configuration.partySize;
        this.distanceBetweenThieves = configuration.distanceBetweenThieves;
        this.assaultPartystate = SharedAssaultParty.DISMISSED;
        this.OTwaitingToReverse = 0;
    }
    
    /**
     * @return Party ID
     */
    @Override
    public synchronized int getAssaultPartyID()
    {
        return this.id;
    }

    /**
     * @return Target room ID
     */
    @Override
    public synchronized int getTargetRoomID()
    {
        if(this.room == null)
        {
            return -1;
        }

        return this.room.getID();
    }

    /**
     * Check if the party is full.
     * @return True if the party element queue has the same size as the party size.
     */
    @Override
    public synchronized boolean partyFull()
    {
        return this.assaultPartySize == this.thieves.size();
    }

    /**
     * Get party state.
     * @return Party state.
     */
    @Override
    public synchronized int getAssaultPartyState()
    {
        return this.assaultPartystate;
    }

    /**
     * @return Assault Party target room distance.
     */
    public synchronized int getTargetDistance()
    {
        return this.room.getDistance();
    }

    /**
     * Reset assaultParty
     */
    private synchronized void reset()
    {
        this.assaultPartystate = SharedAssaultParty.DISMISSED;
        this.room = null;
        this.OTwaitingToReverse = 0;
        this.crawlingQueue.clear();
        this.thieves.clear();
    }

    /**
     * @return Array with the thieves.
     */
    @Override
    public synchronized int[] getThieves()
    {
        int[] thieves = new int[this.thieves.size()];
        
        Iterator<OrdinaryThief> it = this.thieves.iterator();
        for(int i = 0; i < thieves.length; i++)
        {
            thieves[i] = it.next().getID();
        }
//        int i = 0;
//        for (int j = 0; j < this.thieves.size(); j++) {
//            thieves[i] = this.thieves.mem[j].getID();
//            i++;
//        }
        return thieves;
    }

    @Override
    public void assignARoom(RoomStatus room)
    {
        this.assaultPartystate = SharedAssaultParty.WAITING;
        this.room = room;
    }

    @Override
    public synchronized void addThief(OrdinaryThief thief) throws Exception
    {
        //System.out.println("THIEVES SIZE" + this.thieves.size());
        //System.out.println("CHECKPOINT party full-> " + this.partyFull());
        if(!this.partyFull())
        {
            //System.out.println("SIZE BEFORE ADD " + this.crawlingQueue.size());
            this.thieves.write(thief);
            this.crawlingQueue.write(thief.getID());
            //System.out.println("SIZE AFTER ADD " + this.crawlingQueue.size());
        }
        else
        {
            throw new Exception("Party is full!");
        }
    }

    public synchronized void removeThief(int id)
    {
        Iterator<OrdinaryThief> it = this.thieves.iterator();
        while(it.hasNext())
        {
            OrdinaryThief thief = it.next();
            if(thief.getID() == id)
            {
                this.thieves.remove(thief);
                break;
            }
        }
        //System.out.println("SIZE OF THIEVES QUEUE: " + this.thieves.size());
        //System.out.println("IS EMPTY? -> " + this.thieves.empty());
        if(this.thieves.empty())
        {
            //System.out.println("RESETTTTTTTTTTTTTTTTTTTTTTTT");
            this.reset();
        }
    }

    @Override
    public synchronized void sendAssaultParty() throws InterruptedException
    {
        this.assaultPartystate = SharedAssaultParty.CRAWLING_IN;
        this.notifyAll();
    }

    public boolean canICrawl(OrdinaryThief thief) throws Exception
    {
        if(this.assaultPartystate == SharedAssaultParty.CRAWLING_OUT)
        {
            if(thief.getPosition() == 0)
            {
                return false;
            }
        }
        else if(this.assaultPartystate == SharedAssaultParty.CRAWLING_IN)
        {
            if(thief.getPosition() == this.room.getDistance())
            {
                return false;
            }
        }
        return true;
    }

    public synchronized int crawlIn(OrdinaryThief thief) throws Exception
    {
        if(this.crawlingQueue.size() == 0)
        {
            throw new Exception("Queue's size is 0!");
        }
        while(this.crawlingQueue.peek() != thief.getID() || this.assaultPartystate != SharedAssaultParty.CRAWLING_IN)
        {
            this.wait();
        }

        int newPosition = thief.getPosition();
        for(int i = thief.getDisplacement(); i > 0; i--)
        {
            int position = newPosition + i;
            boolean emptyPosition = true;
            boolean distaceIsValid = true;
            Iterator<OrdinaryThief> ita = this.thieves.iterator();
            //System.out.println("OUTPNT: " + this.thieves.getOutPnt() + " INPNT: " + this.thieves.getInPnt());
            //while loop to check if the pos is valid
            while(ita.hasNext())
            {
                //System.out.println("DEPOISSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
                //Search all thieves
                OrdinaryThief ta = ita.next();
                //System.out.println("THIEF ->" + ta);
                if(ta != thief) //Is not the same
                {
                    //Checks if some ordinary thief is in the thief's position
                    if(ta.getPosition() == position)
                    {
                        emptyPosition = false;
                        break;
                    }
                }
                int minDistanceThief = 1000000;
                Iterator<OrdinaryThief> itb = this.thieves.iterator();
                //checks the minimum distance
                while(itb.hasNext())
                {
                    OrdinaryThief tb = itb.next();
                    int absDistance = Math.abs(tb.getPosition() - ta.getPosition());
                    if(ta == thief)
                    {
                        absDistance = Math.abs(tb.getPosition() - position);
                    }
                    if(absDistance < minDistanceThief)
                    {
                        minDistanceThief = absDistance;
                    }
                }
                //Checks if distance is not valid
                if(minDistanceThief > this.distanceBetweenThieves)
                {
                    distaceIsValid = false;
                    break;
                }
            }
            //Checks if the distance is valid and if it's true, calculate new position
            if(distaceIsValid == true)
            {
                //check if pos is empty
                if(position >= this.room.getDistance())
                {
                    newPosition = this.room.getDistance();
                    break;
                }
                else if(emptyPosition)
                {
                    newPosition = position;
                    break;
                }
            }
        }

        boolean canICrawl;
        if (newPosition != this.room.getDistance()){
            canICrawl = true;
        }else{
            canICrawl = false;
        }
        if(canICrawl == true)
        {
            this.crawlingQueue.popPush();
        }
        else
        {
            this.crawlingQueue.read();
        }
        this.notifyAll();
        return newPosition;
    }


    /**
     * The thieves wait for the last one to arrive. The last thieve to arrive wakes everybody.
     * @throws Exception Exception
     */
    public synchronized void reverseDirection(OrdinaryThief thief) throws Exception
    {
        this.OTwaitingToReverse++;
        if(this.OTwaitingToReverse < this.assaultPartySize)
        {
            while(this.OTwaitingToReverse != 0)
            {
                this.wait();
            }
        }
        else
        {
            this.assaultPartystate = SharedAssaultParty.CRAWLING_OUT;
            this.OTwaitingToReverse = 0;
            this.notifyAll();
        }
        this.crawlingQueue.write(thief.getID());
        if(this.crawlingQueue.size() > this.assaultPartySize)
        {
            throw new Exception("CrawlingQueue is bigger than partysize");
        }
    }

    public synchronized int crawlOut(OrdinaryThief thief) throws Exception
    {
        if(this.crawlingQueue.size() == 0)
        {
            throw new Exception("Queue's size is 0!");
        }
        while(this.crawlingQueue.peek() != thief.getID())
        {
            this.wait();
        }
        
        int newPosition = thief.getPosition();
        for(int i = thief.getDisplacement(); i > 0; i--)
        {
            int position = newPosition - i;
            boolean emptyPosition = true;
            boolean distaceIsValid = true;
            Iterator<OrdinaryThief> ita = this.thieves.iterator();
            //while loop to check if the pos is valid
            while(ita.hasNext())
            {
                //Search all thieves
                OrdinaryThief ta = ita.next();
                if(ta != thief) //Is not the same
                {
                    //Checks if some ordinary thief is in the thief's position
                    if(ta.getPosition() == position)
                    {
                        emptyPosition = false;
                        break;
                    }
                }
                int minDistanceThief = 1000000;
                Iterator<OrdinaryThief> itb = this.thieves.iterator();
                //checks the minimum distance
                while(itb.hasNext())
                {
                    OrdinaryThief tb = itb.next();
                    int absDistance = Math.abs(tb.getPosition() - ta.getPosition());
                    if(ta == thief)
                    {
                        absDistance = Math.abs(tb.getPosition() - position);
                    }

                    if(absDistance < minDistanceThief)
                    {
                        minDistanceThief = absDistance;
                    }
                }
                //Checks if distance is not valid
                if(minDistanceThief > this.distanceBetweenThieves)
                {
                    distaceIsValid = false;
                    break;
                }
            }
            //Checks if the distance is valid and if it's true, calculate new position
            if(distaceIsValid == true)
            {
                //check if pos is empty
                if(position <= 0)
                {
                    newPosition = 0;
                    break;
                }
                else if(emptyPosition)
                {
                    newPosition = position;
                    break;
                }
            }
        }
        boolean canICrawl;
        if (newPosition != 0){
            canICrawl = true;
        }else{
            canICrawl = false;
        }
        if(canICrawl == true)
        {
            this.crawlingQueue.popPush();
        }
        else
        {
            this.crawlingQueue.read();
        }
        this.notifyAll();
        return newPosition;
    }
}
