package clientSide.entities;

/**
 *	Ordinary Thief states.
 */
public class OrdinaryStates {
    /**
     *	Concentration Site state.
     */
    public static final int CONCENTRATION_SITE = 0;

    /**
     *	Crawl in state.
     */
    public static final int CRAWLING_INWARDS = 1;

    /**
     *	At a room state.
     */
    public static final int AT_A_ROOM = 2;

    /**
     *	Crawl out state.
     */
    public static final int CRAWLING_OUTWARDS = 3;

    /**
     *	Collection site state.
     */
    public static final int COLLECTION_SITE = 4;

    /**
     *  Instantiation is not possible.
     */
    private OrdinaryStates(){}
}
