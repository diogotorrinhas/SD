package heist.commInfra;

/**
 * Interface to represent FIFO structures.
 */
public interface Queue<T>
{
    /**
     * Add a new element to the FIFO.
     */
    public void push(T e);
    
    /**
     * Remove the first element from the FIFO and returns it.
     * @return Element removed from the FIFO.
     */
    public T pop();
    
    /**
     * Get the first element on the FIFO without removing it.
     * @return The first element on the FIFO.
     */
    public T peek();

    /**
     * Clear the FIFO.
     */
    public void clear();

    /**
     * Removes a specific element from the FIFO.
     * @param e Element to be removed from the FIFO.
     * @return True if was able to remove the element, false otherwise.
     */
    public boolean remove(T e);

    /**
     * Check if the FIFO contains an element.
     * @param e Element that will be searched.
     * @return True if the FIFO contains the element.
     */
    public boolean contains(T e);

    /**
     * Get the size of the FIFO.
     * @return Size of the FIFO.
     */
    public int size();


    /**
     * Check if the FIFO is empty.
     * @return True if the FIFO is empty.
     */
    public default boolean isEmpty() 
    {
        return this.size() == 0;
    }
    
    /**
     * Move first element to the end of this FIFO.
     */
    public default void popPush()
    {
        this.push(this.pop());
    }
}
