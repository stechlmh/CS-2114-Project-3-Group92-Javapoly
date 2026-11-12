package Javapoly;

import java.util.Random;

// -------------------------------------------------------------------------
/**
 * Coordinates player turns, dice rolls, transactions, and game progression
 * between GameState and GameView.
 * 
 * @author kvasallo
 * @version Nov 11, 2026
 */
public class GameController
{
    //~ Fields ................................................................
    private GameState gameState;
    private GameView gameView;
    private Random random;

    //~ Constructors ..........................................................
    /**
     * Creates a default controller with a new GameState, GameView, and Random.
     */
    public GameController()
    {
        this(new GameState(), new GameView(), new Random());
    }

    /**
     * Creates a controller with the given model, view, and random generator.
     *
     * @param gameState the model
     * @param gameView the view
     * @param random die generator
     */
    public GameController(GameState gameState, GameView gameView, Random random)
    {
        this.gameState = gameState;
        this.gameView = gameView;
        this.random = random;
    }

    //~Public  Methods ........................................................

    /**
     * Runs turns until one active player remains.
     */
    public void runGame()
    {
        startGame();
        while (!gameState.isGameOver())
        {
            String cmd = gameView.getInput("\n"
                + gameState.getCurrentPlayer().getName()
                + "'s turn. Press Enter or type 'roll' to roll die (or 'quit' to exit):");
            if (cmd == null || "quit".equalsIgnoreCase(cmd))
            {
                break;
            }
            if (!cmd.isEmpty() && !"roll".equalsIgnoreCase(cmd))
            {
                gameView.showError("Invalid command. Type 'roll' or 'quit'.");
                continue;
            }
            handleTurn();
            if (!gameState.isGameOver())
            {
                gameState.nextTurn();
            }
        }
        Player winner = gameState.getWinner();
        if (winner != null)
        {
            gameView.showMessage("Game Over! Winner: " + winner.getName()
                + " ($" + winner.getMoney() + ")");
        }
    }

    // ----------------------------------------------------------
    /**
     * Prompt for count, retry on invalid input.
     */
    public void startGame()
    {
        while (true)
        {
            String input = gameView.getInput("Enter number of players (2-4):");
            if (input == null)
            {
                gameView.showError("Input cannot be empty.");
                return;
            }
            if (input.isEmpty())
            {
                gameView.showError("Input cannot be empty.");
                continue;
            }
            try
            {
                int count = Integer.parseInt(input);
                gameState.startGame(count);
                gameView.showMessage("Started game with " + count + " players.");
                return;
            }
            catch (NumberFormatException e)
            {
                gameView.showError("Please enter a valid number (2-4).");
            }
            catch (IllegalArgumentException e)
            {
                gameView.showError(e.getMessage());
            }
        }
    }

    // ----------------------------------------------------------
    /**
     * Roll, move, resolve space and offer purchase.
     */
    public void handleTurn()
    {
        Player current = gameState.getCurrentPlayer();
        int roll = rollDie();
        Space landed = gameState.moveCurrentPlayer(roll);
        gameView.showMessage(current.getName() + " rolled " + roll
            + " and landed on " + landed.getName());

        if (landed instanceof PropertySpace)
        {
            PropertySpace prop = (PropertySpace)landed;
            if (prop.getOwner() == null)
            {
                if (current.getMoney() >= prop.getCost())
                {
                    while (true)
                    {
                        String choice = gameView.getInput("Buy " + prop.getName()
                            + " for $" + prop.getCost() + "? (y/n):");
                        if (choice == null || "n".equalsIgnoreCase(choice)
                            || "no".equalsIgnoreCase(choice))
                        {
                            gameView.showMessage(current.getName()
                                + " declined to buy " + prop.getName() + ".");
                            break;
                        }
                        else if ("y".equalsIgnoreCase(choice)
                            || "yes".equalsIgnoreCase(choice))
                        {
                            if (gameState.buyProperty(prop))
                            {
                                gameView.showMessage(current.getName()
                                    + " bought " + prop.getName() + " for $"
                                    + prop.getCost() + ".");
                            }
                            break;
                        }
                        else
                        {
                            gameView.showError("Invalid option. Enter y or n.");
                        }
                    }
                }
                else
                {
                    gameView.showMessage(current.getName()
                        + " cannot afford " + prop.getName() + " ($"
                        + prop.getCost() + ").");
                }
            }
            else if (prop.getOwner() != current)
            {
                Player owner = prop.getOwner();
                int rent = prop.getRent();
                gameState.payRent(prop);
                gameView.showMessage(current.getName() + " paid $" + rent
                    + " rent to " + owner.getName() + ".");
                if (current.isBankrupt())
                {
                    gameView.showMessage(current.getName() + " is bankrupt!");
                }
            }
        }
        else if (landed instanceof CardSpace
            || "CHANCE".equalsIgnoreCase(landed.getEffect())
            || "COMMUNITY".equalsIgnoreCase(landed.getEffect()))
        {
            String deckType = "CHANCE".equalsIgnoreCase(landed.getEffect())
                ? "chance" : "community";
            try
            {
                EventCard card = gameState.drawCard(deckType);
                gameView.showMessage("Card drawn: " + card.getDescription());
                if (current.isBankrupt())
                {
                    gameView.showMessage(current.getName() + " is bankrupt!");
                }
            }
            catch (IllegalStateException e)
            {
                gameView.showError(e.getMessage());
            }
        }
        updateView();
    }

    // ----------------------------------------------------------
    /**
     * Return random 1–6.
     *
     * @return random die value from 1 to 6
     */
    public int rollDie()
    {
        return random.nextInt(6) + 1;
    }

    // ----------------------------------------------------------
    /**
     * Pass current status/results to GameView.
     */
    public void updateView()
    {
        Player current = gameState.getCurrentPlayer();
        Space pos = current.getPosition();
        String spaceName = (pos != null) ? pos.getName() : "None";
        gameView.showMessage("Status -> " + current.getName() + ": $"
            + gameState.getPlayerBalance(current) + " at " + spaceName
            + " (Properties: " + current.getOwnedProperties().size() + ")");
    }

    /**
     * Entry point to run the game.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args)
    {
        GameController controller = new GameController();
        controller.runGame();
    }
}
