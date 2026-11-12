package Javapoly;

/**
 * Node in the circular singly linked board holding a Space and a reference
 * to the next BoardNode.
 *
 * @author Pratham Singh
 * @version Nov 11, 2026
 */
public class BoardNode
{
    private Space space;
    private BoardNode next;

    /**
     * Creates a board node holding the given space.
     *
     * @param space the space at this node
     */
    public BoardNode(Space space)
    {
        this(space, null);
    }

    /**
     * Creates a board node holding the given space and next reference.
     *
     * @param space the space at this node
     * @param next the next node in the circular board
     */
    public BoardNode(Space space, BoardNode next)
    {
        if (space == null)
        {
            throw new IllegalArgumentException("Space cannot be null");
        }
        this.space = space;
        this.next = next;
    }

    /**
     * Gets the space stored at this node.
     *
     * @return the space at this node
     */
    public Space getSpace()
    {
        return space;
    }

    /**
     * Sets the space stored at this node.
     *
     * @param space the space to set
     */
    public void setSpace(Space space)
    {
        if (space == null)
        {
            throw new IllegalArgumentException("Space cannot be null");
        }
        this.space = space;
    }

    /**
     * Gets the next node in the board.
     *
     * @return the next node
     */
    public BoardNode getNext()
    {
        return next;
    }

    /**
     * Sets the next node in the board.
     *
     * @param next the next node to set
     */
    public void setNext(BoardNode next)
    {
        this.next = next;
    }
}

