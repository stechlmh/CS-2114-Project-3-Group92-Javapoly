package Javapoly;

import java.util.Random;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 * 
 *  @author ?????
 *  @version Nov 5, 2026
 */
public class GameController
{
    //~ Fields ................................................................
    private GameState gameState;
    private GameView gameView;
    private Random random;


    //~ Constructors ..........................................................

    public GameController() {
        gameState = new GameState();
        gameView = new GameView();
        random = new Random();
    }
    
    //~Public  Methods ........................................................

    /**
     * Runs turns until one active player remains
     */
    public void runGame() {
        
    }
    
    // ----------------------------------------------------------
    /**
     * Prompt for count, retry on invalid input.
     */
    public void startGame() {
        
    }
    
    // ----------------------------------------------------------
    /**
     * Roll, move, resolve space and offer purchase.
     */
    public void handleTurn() {
        
    }
    
    // ----------------------------------------------------------
    /**
     * Return random 1–6.
     */
    public int rollDie() {
        
    }
    
    // ----------------------------------------------------------
    /**
     * Pass current status/results to GameView.
     */
    public void updateView() {
        
    }


}
