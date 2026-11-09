package Javapoly;

// -------------------------------------------------------------------------
/**
 * Main logic and process of the game
 * 
 * @author kvasallo
 * @version Nov 9, 2026
 */
public class GameState {
    // ~ Fields ................................................................
    private Queue<EventCard> chanceDeck;
    private Queue<EventCard> communityDeck;
    private BoardState board;
    private Player[] turnOrder;
    private int currentTurnIdnex;
    private int activePlayers;

    // ----------------------------------------------------------
    /**
     * Intializes fields with count number of players
     * 
     * @param count
     */
    // ~ Constructors ..........................................................

    public void startGame(int count) {

    }

    // ~Public Methods ........................................................


    /**
     * Gets the current player
     * 
     * @return current player
     */
    public Player getCurrentPlayer() {

    }


    // ----------------------------------------------------------
    /**
     * Moves the player to the space steps spaces away
     * 
     * @param steps
     * @return the space the player lands on
     */
    public Space moveCurrentPlayer(int steps) {

    }


    // ----------------------------------------------------------
    /**
     * Player buys property
     * 
     * @param p
     * @return true if player was able to, false if not
     */
    public boolean buyProperty(PropertySpace p) {

    }


    // ----------------------------------------------------------
    /**
     * Player pays rent to the player who owns the property
     * 
     * @param p
     */
    public void payRent(PropertySpace p) {

    }


    // ----------------------------------------------------------
    /**
     * Draws a card from the specified deck
     * 
     * @param deck
     * @return card drawn
     */
    public EventCard drawCard(String deck) {

    }


    // ----------------------------------------------------------
    /**
     * Goes to the next players turn
     */
    public void nextTurn() {

    }


    // ----------------------------------------------------------
    /**
     * Checks if the game is over
     * 
     * @return true if game is over, false if not
     */
    public boolean isGameOver() {

    }


    // ----------------------------------------------------------
    /**
     * Gets the players that won the game
     * 
     * @return player that won
     */
    public Player getWinner() {

    }


    // ----------------------------------------------------------
    /**
     * Returns the specified player's balance
     * 
     * @param p
     * @return balance of player
     */
    public int getPlayerBalance(Player p) {

    }

}
