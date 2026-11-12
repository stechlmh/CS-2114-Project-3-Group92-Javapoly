package Javapoly;

/**
 * Circular singly linked board of Space objects supporting traversal and
 * player movement.
 *
 * @author Pratham Singh
 * @version Nov 11, 2026
 */
public class BoardState
{
    private BoardNode start;
    private int size;

    /**
     * Creates an empty board.
     */
    public BoardState()
    {
        start = null;
        size = 0;
    }

    /**
     * Appends a space to the board; the last node's next points back to start.
     *
     * @param space the space to add
     * @throws IllegalArgumentException if space is null
     */
    public void addSpace(Space space)
    {
        if (space == null)
        {
            throw new IllegalArgumentException("Space cannot be null");
        }
        BoardNode newNode = new BoardNode(space);
        if (start == null)
        {
            start = newNode;
            start.setNext(start);
        }
        else
        {
            BoardNode current = start;
            while (current.getNext() != start)
            {
                current = current.getNext();
            }
            current.setNext(newNode);
            newNode.setNext(start);
        }
        size++;
    }

    /**
     * Traverses next links steps times from the player's current position and
     * updates the player's position.
     *
     * @param p the player to move
     * @param steps the number of steps to move (1 to 6)
     * @return the space the player lands on
     * @throws IllegalArgumentException if p is null or steps is outside 1-6
     * @throws IllegalStateException if the board is empty
     */
    public Space movePlayer(Player p, int steps)
    {
        if (p == null)
        {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (steps < 1 || steps > 6)
        {
            throw new IllegalArgumentException("Steps must be between 1 and 6");
        }
        if (start == null)
        {
            throw new IllegalStateException("Board is empty");
        }

        BoardNode current = findNode(p.getPosition());
        if (current == null)
        {
            current = start;
        }

        for (int i = 0; i < steps; i++)
        {
            current = current.getNext();
        }

        Space landingSpace = current.getSpace();
        p.setPosition(landingSpace);
        return landingSpace;
    }

    /**
     * Returns the first space on the board, or null when empty.
     *
     * @return the starting space, or null if empty
     */
    public Space getStartSpace()
    {
        if (start == null)
        {
            return null;
        }
        return start.getSpace();
    }

    /**
     * Returns the number of spaces on the board.
     *
     * @return the board size
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Gets the starting node of the circular board.
     *
     * @return the start node, or null if empty
     */
    public BoardNode getStartNode()
    {
        return start;
    }

    private BoardNode findNode(Space target)
    {
        if (target == null)
        {
            return null;
        }
        BoardNode current = start;
        for (int i = 0; i < size; i++)
        {
            if (current.getSpace().equals(target))
            {
                return current;
            }
            current = current.getNext();
        }
        return null;
    }
}
