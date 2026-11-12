package Javapoly;

import student.TestCase;

/**
 * Tests the BoardState and BoardNode classes.
 *
 * @author Pratham Singh
 * @version Nov 11, 2026
 */
public class BoardStateTest extends TestCase
{
    private BoardState board;
    private Space goSpace;
    private Space prop1;
    private Space chanceSpace;
    private Space prop2;
    private Player player;

    /**
     * Sets up a board, spaces, and a player for testing.
     */
    public void setUp()
    {
        board = new BoardState();
        goSpace = new Space("GO", 0, "GO");
        prop1 = new Space("Mediterranean Ave", 1, "Property");
        chanceSpace = new Space("Chance", 2, "Event Card");
        prop2 = new Space("Baltic Ave", 3, "Property");
        player = new Player("Alice", 1500, null);
    }

    /**
     * Tests adding spaces and checking board size.
     */
    public void testAddSpaceAndGetSize()
    {
        assertEquals(0, board.getSize());
        board.addSpace(goSpace);
        assertEquals(1, board.getSize());
        board.addSpace(prop1);
        assertEquals(2, board.getSize());
        board.addSpace(chanceSpace);
        assertEquals(3, board.getSize());
    }

    /**
     * Tests that adding a null space is rejected.
     */
    public void testAddSpaceNull()
    {
        Exception thrown = null;
        try
        {
            board.addSpace(null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(0, board.getSize());
    }

    /**
     * Tests getStartSpace on empty and populated boards.
     */
    public void testGetStartSpace()
    {
        assertNull(board.getStartSpace());
        assertNull(board.getStartNode());

        board.addSpace(goSpace);
        board.addSpace(prop1);
        assertEquals(goSpace, board.getStartSpace());
        assertEquals("GO", board.getStartSpace().getName());
        assertNotNull(board.getStartNode());
    }

    /**
     * Tests moving a player normally and wrapping around the circular board.
     */
    public void testMovePlayerAndWrapAround()
    {
        board.addSpace(goSpace);
        board.addSpace(prop1);
        board.addSpace(chanceSpace);
        board.addSpace(prop2);

        player.setPosition(goSpace);
        Space landed = board.movePlayer(player, 2);
        assertEquals(chanceSpace, landed);
        assertEquals(chanceSpace, player.getPosition());

        Space wrapped = board.movePlayer(player, 4);
        assertEquals(chanceSpace, wrapped);
        assertEquals(chanceSpace, player.getPosition());

        Player unsetPlayer = new Player("Bob", 1500, null);
        Space fromStart = board.movePlayer(unsetPlayer, 1);
        assertEquals(prop1, fromStart);
        assertEquals(prop1, unsetPlayer.getPosition());

        Space unlistedSpace = new Space("Other", 99, "None");
        Player offBoardPlayer = new Player("Charlie", 1500, unlistedSpace);
        Space recovered = board.movePlayer(offBoardPlayer, 1);
        assertEquals(prop1, recovered);
    }

    /**
     * Tests movePlayer with invalid arguments and empty board.
     */
    public void testMovePlayerBadInput()
    {
        board.addSpace(goSpace);
        board.addSpace(prop1);

        Exception nullPlayer = null;
        try
        {
            board.movePlayer(null, 3);
        }
        catch (Exception e)
        {
            nullPlayer = e;
        }
        assertTrue(nullPlayer instanceof IllegalArgumentException);

        Exception zeroSteps = null;
        try
        {
            board.movePlayer(player, 0);
        }
        catch (Exception e)
        {
            zeroSteps = e;
        }
        assertTrue(zeroSteps instanceof IllegalArgumentException);

        Exception sevenSteps = null;
        try
        {
            board.movePlayer(player, 7);
        }
        catch (Exception e)
        {
            sevenSteps = e;
        }
        assertTrue(sevenSteps instanceof IllegalArgumentException);

        BoardState emptyBoard = new BoardState();
        Exception emptyBoardEx = null;
        try
        {
            emptyBoard.movePlayer(player, 2);
        }
        catch (Exception e)
        {
            emptyBoardEx = e;
        }
        assertTrue(emptyBoardEx instanceof IllegalStateException);
    }

    /**
     * Tests BoardNode methods and validation.
     */
    public void testBoardNode()
    {
        BoardNode node1 = new BoardNode(goSpace);
        assertEquals(goSpace, node1.getSpace());
        assertEquals("GO", goSpace.getName());
        assertNull(node1.getNext());

        BoardNode node2 = new BoardNode(prop1, node1);
        assertEquals(prop1, node2.getSpace());
        assertEquals(node1, node2.getNext());

        node1.setSpace(chanceSpace);
        assertEquals(chanceSpace, node1.getSpace());

        node1.setNext(node2);
        assertEquals(node2, node1.getNext());

        Exception nullConstructor = null;
        try
        {
            new BoardNode(null);
        }
        catch (Exception e)
        {
            nullConstructor = e;
        }
        assertTrue(nullConstructor instanceof IllegalArgumentException);

        Exception nullSet = null;
        try
        {
            node1.setSpace(null);
        }
        catch (Exception e)
        {
            nullSet = e;
        }
        assertTrue(nullSet instanceof IllegalArgumentException);

        LinkedChain<String> emptyChain = new LinkedChain<String>();
        emptyChain.addToEnd("First");
        assertTrue(emptyChain.remove("First"));
    }
}
