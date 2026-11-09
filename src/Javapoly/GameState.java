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
    private int currentTurnIndex;
    private int activePlayers;

    // ----------------------------------------------------------
    /**
     * Intializes fields with count number of players
     * 
     * @param count
     */
    // ~ Constructors ..........................................................

    public void startGame(int count) {
        //Check if count is within 2-4 inclusive
        if (count > 4) {
            throw new IllegalArgumentException("Count cannot be greater than 4");
        }
        else if (count < 2) {
            throw new IllegalArgumentException("Count cannot be less than 2");
        }
        
        //Initialize players and turn order
        activePlayers = count;
        for (int i = 0; i < 4; i++) {
            turnOrder[i] = new Player();
        }
        currentTurnIndex = 0; 
        
        //Initialize decks
        chanceDeck = initializeDeck("chance");
        communityDeck = initializeDeck("community");
        

    }

    // ~Public Methods ........................................................


    /**
     * Gets the current player
     * 
     * @return current player
     */
    public Player getCurrentPlayer() {
        return turnOrder[currentTurnIndex];
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
     * @param player 
     * @param property 
     * 
     * @return true if player was able to, false if not
     */
    public boolean buyProperty(Player player, PropertySpace property) {
        if (property.getPrice() > player.getMoney()) {
            return false;
        }
        
        player.setMoney(player.getMoney() - property.getPrice());
        
        return true;
    }


    // ----------------------------------------------------------
    /**
     * Player pays rent to the player who owns the property
     * 
     * @param p
     */
    public void payRent(Player renter, PropertySpace property) {
        
        property.getOwner().setMoney(property.getPrice());
        
        renter.setMoney(renter.getMoney() - property.getPrice());
    }


    // ----------------------------------------------------------
    /**
     * Draws a card from the specified deck
     * 
     * @param deck
     * @return card drawn
     */
    public EventCard drawCard(String deck) {
        if (deck == "chance") {
            return chanceDeck.pop();
        }
        return communityDeck.pop();
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
        int nonbankruptPlayers = 0;
        for (Player player : turnOrder) {
            if (!player.isBankrupt()) {
                nonbankruptPlayers++;
            }
        }

        if (nonbankruptPlayers == 1) {
            return true;
        }
        return false;
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
    //Helper method to initialize card decks
    private Queue<EventCard> initializeDeck(String deckType) {
        
    }
    
    //Check if given player is bankrupt
    private bool checkBankrupt(Player player) {
        if (player.getMoney() <= 0 && player.getOwnedProperties().size() == 0) {
            return true;
        }
        return false;
    }

}
