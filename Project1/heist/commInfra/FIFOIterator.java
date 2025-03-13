package heist.commInfra;
import java.util.Iterator;

public class FIFOIterator<R> implements Iterator<R> {
    private int currentIndex;
    private MemFIFO<R> fifo;

    private int counter;

    public FIFOIterator(MemFIFO<R> fifo){
        this.fifo = fifo;
        this.currentIndex = fifo.getOutPnt();
    }

    public int getCurrentIndex(){
        return this.getCurrentIndex();
    }

    public int getInPnt(){
        return this.fifo.getInPnt();
    }

    @Override
    public R next() {
        R val = fifo.mem[currentIndex];
        counter = counter + 1;
        currentIndex = (currentIndex + 1) % fifo.mem.length;
        return val;
    }

    @Override
    public boolean hasNext() {
        //System.out.println(fifo.size());
        return (counter < fifo.size());
        //return (currentIndex != this.fifo.getInPnt() || !fifo.empty());
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
