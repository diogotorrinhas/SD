package heist.utils;

import java.util.Random;

/**
 * Range is a class used to represent a integer value between a min and a max values.
 */
public class Range
{
    
    /**
     * Random generator used to generate number in range.
     */
    private static final Random random = new Random();
    
    /**
     * Min value in range.
     */
    public int min;
    
    /**
     * Max value in range.
     */
    public int max;
    
    /**
     * Range constructor.
     * @param min Min value.
     * @param max Max value.
     */
    public Range(int min, int max)
    {
        this.min = min;
        this.max = max;
    }
    
    /**
     * Generate integer value between min and max values.
     * @return Value between min and max.
     */
    public int generateInRange()
    {
        return min + random.nextInt(max - min + 1);
    }
}
