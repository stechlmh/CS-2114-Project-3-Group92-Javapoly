package Javapoly;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;
import student.TestCase;

/**
 * Tests GameController and GameView across normal turns and bad inputs.
 *
 * @author Pratham Singh
 * @version Nov 11, 2026
 */
public class GameControllerTest extends TestCase
{
    /**
     * Tests startGame with invalid input followed by valid player count, and
     * null input handling.
     */
    public void testStartGameValidation()
    {
        String input = "\nhello\n1\n5\n2\n";
        GameState state = new GameState();
        GameView view = new GameView(new Scanner(input));
        GameController controller = new GameController(state, view, new Random(1));

        controller.startGame();
        assertEquals(2, state.getPlayers().length);
        assertEquals("Player 1", state.getCurrentPlayer().getName());

        GameView emptyView = new GameView(new Scanner(""));
        GameController emptyController = new GameController(new GameState(), emptyView, new Random(1));
        emptyController.startGame();

        GameView nullScannerView = new GameView(null);
        assertNull(nullScannerView.getInput("Prompt:"));
    }

    /**
     * Tests handleTurn buying, declining, unaffordable property, self-owned
     * property, rent payment, and card drawing.
     */
    public void testHandleTurnBranches()
    {
        String input = "2\n\nmaybe\ny\nroll\nn\nbadcmd\nroll\nyes\nroll\nno\n";
        GameState state = new GameState();
        GameView view = new GameView(new Scanner(input));
        FixedRandom fixedDie = new FixedRandom(new int[] { 0, 2, 4, 0 });
        GameController controller = new GameController(state, view, fixedDie);

        controller.runGame();
        assertEquals(2, state.getPlayers()[0].getOwnedProperties().size());

        GameState sNo = new GameState();
        sNo.startGame(2);
        GameController cNo = new GameController(sNo, new GameView(new Scanner("no\n")), new FixedRandom(new int[] { 0 }));
        cNo.handleTurn();

        GameState s2 = new GameState();
        s2.startGame(2);
        Player p1 = s2.getPlayers()[0];
        Player p2 = s2.getPlayers()[1];

        p1.setMoney(10);
        GameView v2 = new GameView(new Scanner(""));
        GameController c2 = new GameController(s2, v2, new FixedRandom(new int[] { 0 }));
        c2.handleTurn();

        p1.setPosition(s2.getBoard().getStartSpace());
        p1.setMoney(1500);
        GameController cBuyNull = new GameController(s2, new GameView(new Scanner("")), new FixedRandom(new int[] { 0 }));
        cBuyNull.handleTurn();

        p1.setPosition(s2.getBoard().getStartSpace());
        PropertySpace med = (PropertySpace)s2.getBoard().getStartNode().getNext().getSpace();
        med.setOwner(p1);
        med.setPurchased(true);
        cBuyNull.handleTurn();

        s2.nextTurn();
        p2.setPosition(s2.getBoard().getStartSpace());
        p2.setMoney(1500);
        cBuyNull.handleTurn();
        assertEquals(1498, p2.getMoney());

        Space oriental = s2.getBoard().getSpaceByName("Oriental Avenue");
        p2.setPosition(oriental);
        GameController cChance = new GameController(s2, new GameView(new Scanner("")), new FixedRandom(new int[] { 0 }));
        cChance.handleTurn();

        while (!s2.getChanceDeck().isEmpty())
        {
            s2.getChanceDeck().dequeue();
        }
        p2.setPosition(oriental);
        cChance.handleTurn();

        s2.getChanceDeck().enqueue(new EventCard("Big fine", -5000));
        p2.setPosition(oriental);
        p2.setMoney(10);
        cChance.handleTurn();
        assertTrue(p2.isBankrupt());

        s2 = new GameState();
        s2.startGame(2);
        s2.getCurrentPlayer().setPosition(null);
        GameController cNullPos = new GameController(s2, new GameView(new Scanner("")), new FixedRandom(new int[] { 0 }));
        cNullPos.updateView();

        GameState sSub = new GameState()
        {
            @Override
            public boolean buyProperty(PropertySpace p)
            {
                return false;
            }
        };
        sSub.startGame(2);
        GameController cFalseBuy = new GameController(sSub, new GameView(new Scanner("y\n")), new FixedRandom(new int[] { 0 }));
        cFalseBuy.handleTurn();

        GameState customBoardState = new GameState();
        customBoardState.startGame(2);
        customBoardState.getBoard().addSpace(new Space("AltChance", 40, "CHANCE"));
        customBoardState.getBoard().addSpace(new Space("AltComm", 41, "COMMUNITY"));
        customBoardState.getBoard().addSpace(new Space("FreeParking", 42, "FREE"));
        Player cp = customBoardState.getCurrentPlayer();
        Space boardWalk = customBoardState.getBoard().getSpaceByName("Boardwalk");
        cp.setPosition(boardWalk);
        GameController cAlt = new GameController(customBoardState, new GameView(new Scanner("")), new FixedRandom(new int[] { 0, 0, 0 }));
        cAlt.handleTurn();
        cAlt.handleTurn();
        cAlt.handleTurn();
    }

    /**
     * Tests running until a player goes bankrupt and a winner is declared,
     * as well as default constructor and main.
     */
    public void testRunGameToWinnerAndMain()
    {
        GameState state = new GameState();
        FixedRandom fixedDie = new FixedRandom(new int[] { 0, 0 });

        GameView winView = new GameView(new Scanner("2\nroll\ny\nroll\n"));
        GameController winController = new GameController(state, winView, fixedDie)
        {
            @Override
            public void startGame()
            {
                super.startGame();
                state.getPlayers()[1].setMoney(1);
            }
        };
        winController.runGame();
        assertTrue(state.isGameOver());
        assertEquals(state.getPlayers()[0], state.getWinner());

        InputStream originalIn = System.in;
        try
        {
            System.setIn(new ByteArrayInputStream("2\nquit\n".getBytes()));
            GameController.main(new String[0]);
        }
        finally
        {
            System.setIn(originalIn);
        }
    }

    /**
     * Tests GameController jail turn handling and GameView display methods.
     */
    public void testJailTurnsAndViewMethods()
    {
        GameState state = new GameState();
        state.startGame(2);
        Player p1 = state.getCurrentPlayer();

        p1.setPosition(state.getBoard().getSpaceByName("Marvin Gardens"));
        GameController goToJailCtrl = new GameController(state,
            new GameView(new Scanner("")), new FixedRandom(new int[] { 0 }));
        goToJailCtrl.handleTurn();
        assertTrue(p1.isInJail());

        GameController stayJailCtrl = new GameController(state,
            new GameView(new Scanner("maybe\nn\nno\n\n")),
            new FixedRandom(new int[] { 0 }));
        stayJailCtrl.handleTurn();
        assertTrue(p1.isInJail());
        assertEquals(1, p1.getJailTurns());

        stayJailCtrl.handleTurn();
        assertTrue(p1.isInJail());
        assertEquals(2, p1.getJailTurns());

        stayJailCtrl.handleTurn();
        assertFalse(p1.isInJail());

        state.sendToJail(p1);
        p1.setPosition(state.getBoard().getStartSpace());
        GameController payBailCtrl = new GameController(state,
            new GameView(new Scanner("y\nn\nyes\nn\n")),
            new FixedRandom(new int[] { 0 }));
        payBailCtrl.handleTurn();
        assertFalse(p1.isInJail());

        state.sendToJail(p1);
        p1.setPosition(state.getBoard().getStartSpace());
        payBailCtrl.handleTurn();
        assertFalse(p1.isInJail());

        state.sendToJail(p1);
        p1.setMoney(10);
        GameController brokeBailCtrl = new GameController(state,
            new GameView(new Scanner("y\n")),
            new FixedRandom(new int[] { 0 }));
        brokeBailCtrl.handleTurn();
        assertTrue(p1.isInJail());

        p1.setGetOutOfJailCards(1);
        GameController brokeWithCardCtrl = new GameController(state,
            new GameView(new Scanner("y\n")),
            new FixedRandom(new int[] { 0 }));
        brokeWithCardCtrl.handleTurn();
        assertFalse(p1.isInJail());

        GameView view = new GameView(new Scanner("y\nyes\nn\n"));
        view.showPlayerStatus("Player 1", 1500, "GO");
        view.showDiceResult(4, "Income Tax");
        view.showPropertyInfo("Baltic Ave", 60, 4, "Unowned");
        view.showCardEffect("Dividend", 50);
        view.showCardEffect("Fine", -15);
        view.showAllPlayers(new String[] { "Player 1: $1500" });
        view.showAllPlayers(null);
        view.showWinner("Player 1");
        assertTrue(view.promptBuyProperty("Baltic Ave", 60));
        assertTrue(view.promptBuyProperty("Baltic Ave", 60));
        assertFalse(view.promptBuyProperty("Baltic Ave", 60));
        assertFalse(view.promptBuyProperty("Baltic Ave", 60));
    }

    private static class FixedRandom extends Random
    {
        private int[] values;
        private int index;

        FixedRandom(int[] values)
        {
            this.values = values;
            this.index = 0;
        }

        @Override
        public int nextInt(int bound)
        {
            int val = values[index % values.length];
            index++;
            return val;
        }
    }
}
