package Javapoly;

import java.util.Scanner;

// -------------------------------------------------------------------------
/**
 * Console view for displaying prompts, gameplay messages, player status,
 * dice results, property info, card effects, and errors.
 * Contains no game rules or model access.
 *
 * @author Thomas
 * @version Nov 12, 2026
 */
public class GameView
{
    //~ Fields ................................................................
    private Scanner scanner;

    //~ Constructors ..........................................................
    /**
     * Creates a view reading from standard input.
     */
    public GameView()
    {
        this(new Scanner(System.in));
    }

    /**
     * Creates a view reading from the provided scanner.
     *
     * @param scanner input source
     */
    public GameView(Scanner scanner)
    {
        this.scanner = scanner;
    }

    //~ Public Methods ........................................................
    /**
     * Displays a prompt and reads one line of input.
     *
     * @param prompt the prompt to display
     * @return the user input, or null if no input remains
     */
    public String getInput(String prompt)
    {
        System.out.println(prompt);
        if (scanner != null && scanner.hasNextLine())
        {
            return scanner.nextLine().trim();
        }
        return null;
    }

    /**
     * Displays a gameplay message.
     *
     * @param message the message to display
     */
    public void showMessage(String message)
    {
        System.out.println(message);
    }

    /**
     * Displays an input or game error message.
     *
     * @param message the error message to display
     */
    public void showError(String message)
    {
        System.out.println("Error: " + message);
    }

    /**
     * Shows the current player's status.
     *
     * @param name player name
     * @param money current balance
     * @param position current space name
     */
    public void showPlayerStatus(String name, int money, String position)
    {
        System.out.println(name + " | $" + money + " | Position: " + position);
    }

    /**
     * Shows the result of a dice roll and the space landed on.
     *
     * @param roll dice value
     * @param spaceName space landed on
     */
    public void showDiceResult(int roll, String spaceName)
    {
        System.out.println("Rolled " + roll + ". Landed on " + spaceName + ".");
    }

    /**
     * Shows details of a property the current player landed on.
     *
     * @param name property name
     * @param cost purchase price
     * @param rent rent amount
     * @param ownerName owner name, or "Unowned" if none
     */
    public void showPropertyInfo(String name, int cost, int rent, String ownerName)
    {
        System.out.println(name + " | Cost: $" + cost + " | Rent: $" + rent
            + " | Owner: " + ownerName);
    }

    /**
     * Shows a Chance or Community Chest card effect.
     *
     * @param description card text
     * @param amount signed money change
     */
    public void showCardEffect(String description, int amount)
    {
        String change = (amount >= 0) ? ("+$" + amount) : ("-$" + Math.abs(amount));
        System.out.println(description + " (" + change + ")");
    }

    /**
     * Shows a summary of all players.
     *
     * @param statusLines pre-formatted lines from the controller
     */
    public void showAllPlayers(String[] statusLines)
    {
        System.out.println("--- Players ---");
        if (statusLines != null)
        {
            for (String line : statusLines)
            {
                System.out.println(line);
            }
        }
    }

    /**
     * Announces the winner.
     *
     * @param winnerName name of the remaining player
     */
    public void showWinner(String winnerName)
    {
        System.out.println(winnerName + " wins!");
    }

    /**
     * Prompts the player to buy or decline a property.
     *
     * @param propertyName name of the property
     * @param cost purchase price
     * @return true if the player chooses to buy, false otherwise
     */
    public boolean promptBuyProperty(String propertyName, int cost)
    {
        String input = getInput("Buy " + propertyName + " for $" + cost + "? (yes/no)");
        return input != null && (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("y"));
    }
}
