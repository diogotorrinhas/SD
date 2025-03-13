package heist.commInfra;

import java.util.Arrays;
import java.util.Iterator;

/**
 *    Parametric FIFO derived from a parametric memory.
 *    Errors are reported.
 *
 *    @param <R> data type of stored objects
 */

public class MemFIFO<R> extends MemObject<R> implements Iterable<R>
{
    /**
     *   Pointer to the first empty location.
     */

    private int inPnt;

    /**
     *   Pointer to the first occupied location.
     */

    private int outPnt;

    /**
     *   Signaling FIFO empty state.
     */

    private boolean empty;

    /**
     *   FIFO instantiation.
     *   The instantiation only takes place if the memory exists.
     *   Otherwise, an error is reported.
     *
     *     @param storage memory to be used
     *     @throws MemException when the memory does not exist
     */

    public MemFIFO (R [] storage) throws MemException
    {
        super (storage);
        inPnt = outPnt = 0;
        empty = true;
    }

    /**
     *   FIFO insertion.
     *   A parametric object is written into it.
     *   If the FIFO is full, an error is reported.
     *
     *    @param val parametric object to be written
     *    @throws MemException when the FIFO is full
     */

    @Override
    public void write (R val) throws MemException
    {
        if ((inPnt != outPnt) || empty)
        { mem[inPnt] = val;
            inPnt = (inPnt + 1) % mem.length;
            empty = false;
        }
        else throw new MemException ("Fifo full!");
    }

    /**
     *   FIFO retrieval.
     *   A parametric object is read from it.
     *   If the FIFO is empty, an error is reported.
     *
     *    @return first parametric object that was written
     *    @throws MemException when the FIFO is empty
     */

    @Override
    public R read () throws MemException
    {
        R val;

        if (!empty)
        { val = mem[outPnt];
            outPnt = (outPnt + 1) % mem.length;
            empty = (inPnt == outPnt);
        }
        else throw new MemException ("Fifo empty!");
        return val;
    }

    /**
     * Move the first element to the end of the FIFO (pop-push operation).
     * If the FIFO is empty, an error is reported.
     * @throws MemException
     */
    public void popPush() throws MemException{
        if (!empty){
            R val = read();
            write(val);
        }else{
            throw new MemException("FIFO is empty");
        }
    }

    /**
     * FIFO peek.
     * An object is read from the FIFO without removing it.
     * If the FIFO is empty, an error is reported.
     * @return first object in the FIFO
     * @throws MemException when the FIFO is empty
     */
    public R peek() throws MemException{
        if(!empty){
            return mem[outPnt];
        }else{
            throw new MemException("FIFO is empty!");
        }
    }

    /**
     *   Check if the FIFO contains a specified element.
     *
     *    @param val parametric object to be checked
     *    @return true, if the FIFO contains the specified element -
     *            false, otherwise
     */
    public boolean contains(R val) {
        for (int i = outPnt; i != inPnt; i = (i + 1) % mem.length) {
            if (mem[i].equals(val)) {
                return true;
            }
        }
        return false;
    }

    /**
     *   Remove a specified element from the FIFO.
     *
     *    @param val parametric object to be removed
     *    @return true, if the element was removed successfully -
     *            false, otherwise
     */
    public boolean remove(R val) {
        //System.out.println("SIZE BEFORE->"+ this.size());
        if (empty) {
            // If the array is empty, return false
            return false;
        } else if (mem[outPnt].equals(val)) {
            // If the first element matches the input value, remove it
            mem[outPnt] = null;
            outPnt = (outPnt + 1) % mem.length;
            empty = (inPnt == outPnt);
            //System.out.println("SIZE AFTER->"+ this.size());
            return true;
        } else {
            // Otherwise, iterate through the array and remove the element if found
            int i = (outPnt + 1) % mem.length;
            while (i != inPnt) {
                if (mem[i].equals(val)) {
                    while (i != outPnt) {
                        int prevIndex = (i - 1 + mem.length) % mem.length;
                        mem[i] = mem[prevIndex];
                        i = prevIndex;
                    }
                    mem[outPnt] = null;
                    outPnt = (outPnt + 1) % mem.length;
                    empty = (inPnt == outPnt);
                    //System.out.println("SIZE AFTER->"+ this.size());
                    return true;
                }
                i = (i + 1) % mem.length;
            }
            return false;
        }
    }

    /**
     * Clear the FIFO -> empty FIFO.
     */
    public void clear(){
        //Arrays.fill(mem,null);
//        for(int i = 0; i < mem.length; i++){
//            mem[i] = null;
//        }
        for (int i = outPnt; i != inPnt; i = (i + 1) % mem.length) {
            mem[i] = null;
        }
        inPnt = outPnt = 0;
        empty = true;
    }

    /**
     * Returns the current size of the FIFO.
     * @return the size of the FIFO
     */
    public int size() {
        if (empty) {
            return 0;
        } else if (inPnt > outPnt) {
            return inPnt - outPnt;
        } else {
            return inPnt + mem.length - outPnt;
        }
    }

    /**
     *   Test FIFO current full status.
     *
     *    @return true, if FIFO is full -
     *            false, otherwise
     */

    public boolean full ()
    {
        return !((inPnt != outPnt) || empty);
    }

    /**
     * Check if the FIFO is empty.
     * @return true, if the FIFO is empty - false otherwise.
     */
    public boolean empty(){
        return empty;
    }

    public int getOutPnt(){
        return this.outPnt;
    }

    public int getInPnt(){
        return this.inPnt;
    }

    public Iterator<R> iterator(){
        return new FIFOIterator(this);
    }
}