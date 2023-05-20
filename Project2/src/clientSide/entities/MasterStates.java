package clientSide.entities;

public class MasterStates {
    /**
     *	The Master thief is PLANNING_THE_HEIST.
     */
    public static final int PLANNING_THE_HEIST = 0;

    /**
     *	The Master thief is DECIDING_WHAT_TO_DO.
     */
    public static final int DECIDING_WHAT_TO_DO = 1;

    /**
     *	The Master thief is ASSEMBLING_A_GROUP.
     */
    public static final int ASSEMBLING_A_GROUP = 2;

    /**
     *	The Master thief is WAITING_FOR_GROUP_ARRIVAL.
     */
    public static final int WAITING_FOR_GROUP_ARRIVAL = 3;

    /**
     *	The Master thief is PRESENTING_THE_REPORT.
     */
    public static final int PRESENTING_THE_REPORT = 4;

    /**
     *  Instantiation is not possible.
     */
    private MasterStates(){}
}
