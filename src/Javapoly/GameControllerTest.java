package Javapoly;

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
     * Tests startGame with invalid input followed by valid player count.
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
    }

    /**
     * Tests handleTurn buying a property, declining, paying rent, and drawing
     * event cards.
     */
    public void testHandleTurnAndRunGame()
    {
        String input = "2\nroll\nmaybe\ny\nroll\nn\nquit\n";
        GameState state = new GameState();
        GameView view = new GameView(new Scanner(input));
        FixedRandom fixedDie = new FixedRandom(new int[] { 0, 1 });
        GameController controller = new GameController(state, view, fixedDie);

        controller.runGame();
        assertEquals(1440, state.getPlayers()[0].getMoney());
        assertEquals(1, state.getPlayers()[0].getOwnedProperties().size());
        assertEquals(1700, state.getPlayers()[1].getMoney());
    }

    /**
     * Tests running until a player goes bankrupt and a winner is declared.
     */
    public void testRunGameToWinner()
    {
        GameState state = new GameState();
        GameView view = new GameView(new Scanner("2\n"));
        FixedRandom fixedDie = new FixedRandom(new int[] { 0, 0 });
        GameController controller = new GameController(state, view, fixedDie);

        controller.startGame();
        state.getPlayers()[1].setMoney(5);
        GameView turnView = new GameView(new Scanner("y\n"));
        GameController turnController = new GameController(state, turnView, fixedDie);

        turnController.handleTurn();
        state.nextTurn();
        turnController.handleTurn();

        assertTrue(state.isGameOver());
        assertEquals(state.getPlayers()[0], state.getWinner());
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
