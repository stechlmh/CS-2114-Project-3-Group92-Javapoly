package Javapoly;

import java.util.Scanner;

// -------------------------------------------------------------------------
/**
 * Console view for displaying prompts, gameplay messages, and errors.
 * 
 * @author Thomas
 * @version Nov 11, 2026
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

    //~Public  Methods ........................................................
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
}
