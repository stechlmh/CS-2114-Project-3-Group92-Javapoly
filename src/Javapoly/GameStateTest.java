package Javapoly;

import student.TestCase;

/**
 * Tests GameState, Player, PropertySpace, and EventCard.
 *
 * @author Pratham Singh
 * @version Nov 11, 2026
 */
public class GameStateTest extends TestCase
{
    private GameState state;

    /**
     * Sets up a new GameState before each test.
     */
    public void setUp()
    {
        state = new GameState();
    }

    /**
     * Tests startGame and getCurrentPlayer with valid and invalid counts.
     */
    public void testStartGameAndGetCurrentPlayer()
    {
        Exception notStarted = null;
        try
        {
            state.getCurrentPlayer();
        }
        catch (Exception e)
        {
            notStarted = e;
        }
        assertTrue(notStarted instanceof IllegalStateException);

        Exception tooFew = null;
        try
        {
            state.startGame(1);
        }
        catch (Exception e)
        {
            tooFew = e;
        }
        assertTrue(tooFew instanceof IllegalArgumentException);

        Exception tooMany = null;
        try
        {
            state.startGame(5);
        }
        catch (Exception e)
        {
            tooMany = e;
        }
        assertTrue(tooMany instanceof IllegalArgumentException);

        state.startGame(3);
        assertNotNull(state.getCurrentPlayer());
        assertEquals("Player 1", state.getCurrentPlayer().getName());
        assertEquals(1500, state.getPlayerBalance(state.getCurrentPlayer()));
    }

    /**
     * Tests moving the current player and bad step inputs.
     */
    public void testMoveCurrentPlayer()
    {
        state.startGame(2);
        Space landed = state.moveCurrentPlayer(4);
        assertNotNull(landed);
        assertEquals(landed, state.getCurrentPlayer().getPosition());

        Exception lowSteps = null;
        try
        {
            state.moveCurrentPlayer(0);
        }
        catch (Exception e)
        {
            lowSteps = e;
        }
        assertTrue(lowSteps instanceof IllegalArgumentException);

        Exception highSteps = null;
        try
        {
            state.moveCurrentPlayer(7);
        }
        catch (Exception e)
        {
            highSteps = e;
        }
        assertTrue(highSteps instanceof IllegalArgumentException);
    }

    /**
     * Tests buying properties and rejecting invalid purchases.
     */
    public void testBuyProperty()
    {
        state.startGame(2);
        Player p1 = state.getCurrentPlayer();
        PropertySpace prop = new PropertySpace("Baltic Ave", 3, 60, 40);

        assertTrue(state.buyProperty(prop));
        assertEquals(1440, p1.getMoney());
        assertEquals(p1, prop.getOwner());

        assertFalse(state.buyProperty(prop));
        assertEquals(1440, p1.getMoney());

        PropertySpace expensive = new PropertySpace("Boardwalk", 7, 2000, 200);
        assertFalse(state.buyProperty(expensive));
        assertNull(expensive.getOwner());

        Player p2 = state.getPlayers()[1];
        PropertySpace otherProp = new PropertySpace("Oriental Ave", 4, 100, 50);
        assertFalse(state.buyProperty(p2, otherProp));
        assertFalse(state.buyProperty(null, otherProp));
        assertFalse(state.buyProperty(p1, null));
    }

    /**
     * Tests paying rent and bankruptcy when unable to afford rent.
     */
    public void testPayRentAndBankruptcy()
    {
        state.startGame(2);
        Player p1 = state.getPlayers()[0];
        Player p2 = state.getPlayers()[1];

        PropertySpace prop = new PropertySpace("Baltic Ave", 3, 60, 40);
        state.buyProperty(prop);

        state.nextTurn();
        assertEquals(p2, state.getCurrentPlayer());
        state.payRent(prop);
        assertEquals(1460, p2.getMoney());
        assertEquals(1480, p1.getMoney());

        p2.setMoney(10);
        PropertySpace p2Prop = new PropertySpace("Vermont Ave", 6, 100, 60);
        p2Prop.setOwner(p2);
        p2Prop.setPurchased(true);
        p2.getOwnedProperties().add(p2Prop);

        state.payRent(prop);
        assertTrue(p2.isBankrupt());
        assertEquals(0, p2.getMoney());
        assertNull(p2Prop.getOwner());
        assertTrue(state.isGameOver());
        assertEquals(p1, state.getWinner());
    }

    /**
     * Tests drawing event cards, cycling the queue, and empty deck error.
     */
    public void testDrawCard()
    {
        state.startGame(2);
        Player p1 = state.getCurrentPlayer();
        int initialSize = state.getChanceDeck().size();

        EventCard card = state.drawCard("chance");
        assertNotNull(card);
        assertEquals(1500 + card.getAmount(), p1.getMoney());
        assertEquals(initialSize, state.getChanceDeck().size());

        EventCard commCard = state.drawCard("community");
        assertNotNull(commCard);

        Exception badDeck = null;
        try
        {
            state.drawCard("unknown");
        }
        catch (Exception e)
        {
            badDeck = e;
        }
        assertTrue(badDeck instanceof IllegalArgumentException);

        while (!state.getChanceDeck().isEmpty())
        {
            state.getChanceDeck().dequeue();
        }
        Exception emptyDeck = null;
        try
        {
            state.drawCard("chance");
        }
        catch (Exception e)
        {
            emptyDeck = e;
        }
        assertTrue(emptyDeck instanceof IllegalStateException);
    }

    /**
     * Tests nextTurn skipping bankrupt players and getPlayerBalance validation.
     */
    public void testNextTurnAndBalance()
    {
        state.startGame(3);
        Player p1 = state.getPlayers()[0];
        Player p2 = state.getPlayers()[1];
        Player p3 = state.getPlayers()[2];

        assertFalse(state.isGameOver());
        assertNull(state.getWinner());

        p2.setBankrupt(true);
        state.nextTurn();
        assertEquals(p3, state.getCurrentPlayer());

        Exception nullPlayer = null;
        try
        {
            state.getPlayerBalance(null);
        }
        catch (Exception e)
        {
            nullPlayer = e;
        }
        assertTrue(nullPlayer instanceof IllegalArgumentException);
        assertEquals(1500, state.getPlayerBalance(p1));
    }

    /**
     * Tests PropertySpace and EventCard constructor validation and getters.
     */
    public void testPropertyAndEventCardValidation()
    {
        EventCard card = new EventCard("Win prize", 100);
        assertEquals("Win prize", card.getDescription());
        assertEquals(100, card.getAmount());

        Exception badCard = null;
        try
        {
            new EventCard("", 10);
        }
        catch (Exception e)
        {
            badCard = e;
        }
        assertTrue(badCard instanceof IllegalArgumentException);

        Exception badCost = null;
        try
        {
            new PropertySpace("Bad", 1, -10, 10);
        }
        catch (Exception e)
        {
            badCost = e;
        }
        assertTrue(badCost instanceof IllegalArgumentException);
    }
}

