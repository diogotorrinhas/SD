package clientSide.entities;

/**
 *	Master Thief states.
 */
public class MasterStates {
    /**
     *	Planning the heist state.
     */
    public static final int PLANNING_THE_HEIST = 0;

    /**
     *	Deciding what to do state.
     */
    public static final int DECIDING_WHAT_TO_DO = 1;

    /**
     *	Assembling a group state.
     */
    public static final int ASSEMBLING_A_GROUP = 2;

    /**
     *	Waiting for group arrival state.
     */
    public static final int WAITING_FOR_GROUP_ARRIVAL = 3;

    /**
     *	Presenting the report state.
     */
    public static final int PRESENTING_THE_REPORT = 4;

    /**
     *  Instantiation is not possible.
     */
    private MasterStates(){}
}
