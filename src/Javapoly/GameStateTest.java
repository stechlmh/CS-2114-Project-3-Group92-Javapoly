package Javapoly;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import student.TestCase;

/**
 * Tests GameState, Player, PropertySpace, CardSpace, Space, Event, and EventCard.
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
    public void testStartGameAndGetCurrentPlayer() throws Exception
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

        Field turnOrderField = GameState.class.getDeclaredField("turnOrder");
        turnOrderField.setAccessible(true);
        turnOrderField.set(state, new Player[0]);
        Exception emptyTurnOrder = null;
        try
        {
            state.getCurrentPlayer();
        }
        catch (Exception e)
        {
            emptyTurnOrder = e;
        }
        assertTrue(emptyTurnOrder instanceof IllegalStateException);

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
        assertNotNull(state.getBoard());
        assertNotNull(state.getCommunityDeck());
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

        state.getCurrentPlayer().setBankrupt(true);
        Exception bankruptMove = null;
        try
        {
            state.moveCurrentPlayer(2);
        }
        catch (Exception e)
        {
            bankruptMove = e;
        }
        assertTrue(bankruptMove instanceof IllegalStateException);
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

        PropertySpace purchasedNoOwner = new PropertySpace("Test Prop", 2, 50, 10);
        purchasedNoOwner.setPurchased(true);
        assertFalse(state.buyProperty(purchasedNoOwner));

        PropertySpace expensive = new PropertySpace("Boardwalk", 7, 2000, 200);
        assertFalse(state.buyProperty(expensive));
        assertNull(expensive.getOwner());

        Player p2 = state.getPlayers()[1];
        PropertySpace otherProp = new PropertySpace("Oriental Ave", 4, 100, 50);
        assertFalse(state.buyProperty(p2, otherProp));
        assertFalse(state.buyProperty(null, otherProp));
        assertFalse(state.buyProperty(p1, null));

        p1.setBankrupt(true);
        assertFalse(state.buyProperty(p1, otherProp));
    }

    /**
     * Tests paying rent and bankruptcy when unable to afford rent.
     */
    public void testPayRentAndBankruptcy() throws Exception
    {
        state.startGame(2);
        Player p1 = state.getPlayers()[0];
        Player p2 = state.getPlayers()[1];

        PropertySpace prop = new PropertySpace("Baltic Ave", 3, 60, 40);
        state.payRent(p1, prop);
        state.buyProperty(prop);

        state.payRent(p1, prop);

        Exception nullRenter = null;
        try
        {
            state.payRent(null, prop);
        }
        catch (Exception e)
        {
            nullRenter = e;
        }
        assertTrue(nullRenter instanceof IllegalArgumentException);

        Exception nullProp = null;
        try
        {
            state.payRent(p1, null);
        }
        catch (Exception e)
        {
            nullProp = e;
        }
        assertTrue(nullProp instanceof IllegalArgumentException);

        state.nextTurn();
        assertEquals(p2, state.getCurrentPlayer());
        state.payRent(prop);
        assertEquals(1460, p2.getMoney());
        assertEquals(1480, p1.getMoney());

        p1.setBankrupt(true);
        state.payRent(p2, prop);
        p1.setBankrupt(false);

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

        state.payRent(p2, prop);

        Method bankruptMethod = GameState.class.getDeclaredMethod("bankruptPlayer", Player.class);
        bankruptMethod.setAccessible(true);
        bankruptMethod.invoke(state, p2);

        Method checkBankruptMethod = GameState.class.getDeclaredMethod("checkBankrupt", Player.class);
        checkBankruptMethod.setAccessible(true);
        assertTrue((Boolean)checkBankruptMethod.invoke(state, p2));
        assertFalse((Boolean)checkBankruptMethod.invoke(state, p1));
        Player brokeWithProp = new Player("Broke", 0, null);
        brokeWithProp.getOwnedProperties().add(prop);
        assertFalse((Boolean)checkBankruptMethod.invoke(state, brokeWithProp));
    }

    /**
     * Tests drawing event cards, cycling the queue, and empty deck error.
     */
    public void testDrawCard() throws Exception
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

        EventCard commChestCard = state.drawCard("community chest");
        assertNotNull(commChestCard);

        p1.setMoney(5);
        while (state.getChanceDeck().peek().getAmount() >= 0)
        {
            state.drawCard("chance");
        }
        state.drawCard("chance");
        assertTrue(p1.isBankrupt());

        Exception nullDeck = null;
        try
        {
            state.drawCard(null);
        }
        catch (Exception e)
        {
            nullDeck = e;
        }
        assertTrue(nullDeck instanceof IllegalArgumentException);

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

        Field chanceField = GameState.class.getDeclaredField("chanceDeck");
        chanceField.setAccessible(true);
        chanceField.set(state, null);
        Exception nullTargetDeck = null;
        try
        {
            state.drawCard("chance");
        }
        catch (Exception e)
        {
            nullTargetDeck = e;
        }
        assertTrue(nullTargetDeck instanceof IllegalStateException);
    }

    /**
     * Tests nextTurn, isGameOver, getWinner, and getPlayerBalance edge cases.
     */
    public void testNextTurnAndWinnerEdgeCases() throws Exception
    {
        state.nextTurn();
        assertNull(state.getWinner());

        state.startGame(3);
        Player p1 = state.getPlayers()[0];
        Player p2 = state.getPlayers()[1];
        Player p3 = state.getPlayers()[2];

        assertFalse(state.isGameOver());
        assertNull(state.getWinner());

        p2.setBankrupt(true);
        state.nextTurn();
        assertEquals(p3, state.getCurrentPlayer());

        p1.setBankrupt(true);
        p3.setBankrupt(true);
        state.nextTurn();

        Field activeField = GameState.class.getDeclaredField("activePlayers");
        activeField.setAccessible(true);
        activeField.set(state, 1);
        state.nextTurn();
        assertNull(state.getWinner());

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
     * Tests Player, Space, PropertySpace, CardSpace, Event, and EventCard methods.
     */
    public void testModelEntitiesCoverage()
    {
        Player defPlayer = new Player();
        assertEquals("Player", defPlayer.getName());
        Player namedPlayer = new Player("Bob");
        assertEquals("Bob", namedPlayer.getName());
        namedPlayer.setName("Bobby");
        assertEquals("Bobby", namedPlayer.getName());

        Space s = new Space("Start", 0, "GO");
        s.setName("NewStart");
        assertEquals("NewStart", s.getName());
        s.setIndex(10);
        assertEquals(10, s.getIndex());
        s.setEffect("Bonus");
        assertEquals("Bonus", s.getEffect());
        s.triggerEffect(namedPlayer);

        Event ev = new Event();
        assertNotNull(ev);

        EventCard card = new EventCard("Win prize", 100);
        assertEquals("Win prize", card.getDescription());
        assertEquals(100, card.getAmount());

        Exception nullDesc = null;
        try
        {
            new EventCard(null, 10);
        }
        catch (Exception e)
        {
            nullDesc = e;
        }
        assertTrue(nullDesc instanceof IllegalArgumentException);

        Exception emptyDesc = null;
        try
        {
            new EventCard("   ", 10);
        }
        catch (Exception e)
        {
            emptyDesc = e;
        }
        assertTrue(emptyDesc instanceof IllegalArgumentException);

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

        Exception badRent = null;
        try
        {
            new PropertySpace("BadRent", 1, 50, -5);
        }
        catch (Exception e)
        {
            badRent = e;
        }
        assertTrue(badRent instanceof IllegalArgumentException);

        PropertySpace prop = new PropertySpace("Park Place", 6, 350, 35);
        assertEquals(350, prop.getPrice());
        prop.setPrice(400);
        assertEquals(400, prop.getCost());
        assertNotNull(prop.getRentArray());
        assertEquals(0, prop.getHouses());
        prop.setRent(null);
        assertEquals(0, prop.getRent());
        prop.setRent(new int[0]);
        assertEquals(0, prop.getRent());
        prop.setRent(new int[] { 50, 100 });
        prop.setHouses(1);
        assertEquals(100, prop.getRent());
        prop.triggerEffect(null);
        prop.triggerEffect(namedPlayer);

        Queue<EventCard> deck = new Queue<EventCard>();
        CardSpace cs = new CardSpace("Chance", 5, "CHANCE", null);
        assertNull(cs.getCardSet());
        cs.triggerEffect(namedPlayer);
        cs.setCardSet(deck);
        assertEquals(deck, cs.getCardSet());
        cs.triggerEffect(namedPlayer);
        deck.enqueue(new EventCard("Gain 25", 25));
        int before = namedPlayer.getMoney();
        cs.triggerEffect(namedPlayer);
        assertEquals(before + 25, namedPlayer.getMoney());
    }
}
