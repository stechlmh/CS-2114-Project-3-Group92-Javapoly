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
        if (count > 4) {
            throw new IllegalArgumentException("Count cannot be greater than 4");
        }
        else if (count < 2) {
            throw new IllegalArgumentException("Count cannot be less than 2");
        }

        chanceDeck = initializeDeck("chance");
        communityDeck = initializeDeck("community");

        board = new BoardState();
        initializeBoard();

        turnOrder = new Player[count];
        for (int i = 0; i < count; i++) {
            turnOrder[i] = new Player("Player " + (i + 1), 1500, board.getStartSpace());
        }
        currentTurnIndex = 0;
        activePlayers = count;
    }

    // ~Public Methods ........................................................

    /**
     * Gets the current player
     * 
     * @return current player
     */
    public Player getCurrentPlayer() {
        if (turnOrder == null || turnOrder.length == 0) {
            throw new IllegalStateException("Game has not started");
        }
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
        Player current = getCurrentPlayer();
        if (current.isBankrupt()) {
            throw new IllegalStateException("Bankrupt player cannot move");
        }
        return board.movePlayer(current, steps);
    }

    // ----------------------------------------------------------
    /**
     * Active player buys property
     * 
     * @param p property to buy
     * @return true if player was able to, false if not
     */
    public boolean buyProperty(PropertySpace p) {
        return buyProperty(getCurrentPlayer(), p);
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
        if (player == null || property == null) {
            return false;
        }
        if (player != getCurrentPlayer() || player.isBankrupt()) {
            return false;
        }
        if (property.getOwner() != null || property.getPurchased()) {
            return false;
        }
        if (property.getCost() > player.getMoney()) {
            return false;
        }

        player.setMoney(player.getMoney() - property.getCost());
        property.setOwner(player);
        property.setPurchased(true);
        player.getOwnedProperties().add(property);
        return true;
    }

    // ----------------------------------------------------------
    /**
     * Active player pays rent to the player who owns the property
     * 
     * @param p property landed on
     */
    public void payRent(PropertySpace p) {
        payRent(getCurrentPlayer(), p);
    }

    // ----------------------------------------------------------
    /**
     * Player pays rent to the player who owns the property
     * 
     * @param renter
     * @param property
     */
    public void payRent(Player renter, PropertySpace property) {
        if (renter == null || property == null) {
            throw new IllegalArgumentException("Renter and property cannot be null");
        }
        if (renter.isBankrupt()) {
            return;
        }
        Player owner = property.getOwner();
        if (owner == null || owner == renter || owner.isBankrupt()) {
            return;
        }
        int rentAmount = property.getRent();
        if (renter.getMoney() >= rentAmount) {
            renter.setMoney(renter.getMoney() - rentAmount);
            owner.setMoney(owner.getMoney() + rentAmount);
        }
        else {
            owner.setMoney(owner.getMoney() + Math.max(0, renter.getMoney()));
            renter.setMoney(0);
            bankruptPlayer(renter);
        }
    }

    // ----------------------------------------------------------
    /**
     * Draws a card from the specified deck, applies its signed money effect
     * to the current player, and enqueues the card back to the rear.
     * 
     * @param deck
     * @return card drawn
     */
    public EventCard drawCard(String deck) {
        Player current = getCurrentPlayer();
        if (deck == null) {
            throw new IllegalArgumentException("Deck cannot be null");
        }
        Queue<EventCard> targetDeck;
        if ("chance".equalsIgnoreCase(deck)) {
            targetDeck = chanceDeck;
        }
        else if ("community".equalsIgnoreCase(deck)
            || "community chest".equalsIgnoreCase(deck)) {
            targetDeck = communityDeck;
        }
        else {
            throw new IllegalArgumentException("Unknown deck: " + deck);
        }

        if (targetDeck == null || targetDeck.isEmpty()) {
            throw new IllegalStateException("Event deck is empty");
        }

        EventCard card = targetDeck.dequeue();
        targetDeck.enqueue(card);

        int newBalance = current.getMoney() + card.getAmount();
        if (newBalance < 0) {
            current.setMoney(0);
            bankruptPlayer(current);
        }
        else {
            current.setMoney(newBalance);
        }
        return card;
    }

    // ----------------------------------------------------------
    /**
     * Goes to the next players turn
     */
    public void nextTurn() {
        if (turnOrder == null || activePlayers <= 1) {
            return;
        }
        int count = turnOrder.length;
        for (int i = 0; i < count; i++) {
            currentTurnIndex = (currentTurnIndex + 1) % count;
            if (!turnOrder[currentTurnIndex].isBankrupt()) {
                return;
            }
        }
    }

    // ----------------------------------------------------------
    /**
     * Checks if the game is over
     * 
     * @return true if game is over, false if not
     */
    public boolean isGameOver() {
        return activePlayers <= 1;
    }

    // ----------------------------------------------------------
    /**
     * Gets the player that won the game
     * 
     * @return player that won, or null if not over
     */
    public Player getWinner() {
        if (turnOrder == null || !isGameOver()) {
            return null;
        }
        for (Player p : turnOrder) {
            if (!p.isBankrupt()) {
                return p;
            }
        }
        return null;
    }

    // ----------------------------------------------------------
    /**
     * Returns the specified player's balance
     * 
     * @param player
     * @return balance of player
     */
    public int getPlayerBalance(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        return player.getMoney();
    }

    /**
     * Gets the board state.
     *
     * @return the board
     */
    public BoardState getBoard() {
        return board;
    }

    /**
     * Gets the Chance card queue.
     *
     * @return the chance deck
     */
    public Queue<EventCard> getChanceDeck() {
        return chanceDeck;
    }

    /**
     * Gets the Community Chest card queue.
     *
     * @return the community deck
     */
    public Queue<EventCard> getCommunityDeck() {
        return communityDeck;
    }

    /**
     * Gets the turn order array.
     *
     * @return the players in turn order
     */
    public Player[] getPlayers() {
        return turnOrder;
    }

    private void bankruptPlayer(Player player) {
        if (player.isBankrupt()) {
            return;
        }
        player.setBankrupt(true);
        for (PropertySpace prop : player.getOwnedProperties()) {
            prop.setOwner(null);
            prop.setPurchased(false);
            prop.setHouses(0);
        }
        player.getOwnedProperties().clear();
        activePlayers--;
    }

    private void initializeBoard() {
        board.addSpace(new Space("GO", 0, "GO"));
        board.addSpace(new PropertySpace("Mediterranean Ave", 1, 60, 20));
        board.addSpace(new CardSpace("Community Chest", 2, "COMMUNITY", communityDeck));
        board.addSpace(new PropertySpace("Baltic Ave", 3, 60, 40));
        board.addSpace(new PropertySpace("Oriental Ave", 4, 100, 50));
        board.addSpace(new CardSpace("Chance", 5, "CHANCE", chanceDeck));
        board.addSpace(new PropertySpace("Vermont Ave", 6, 100, 60));
        board.addSpace(new PropertySpace("Boardwalk", 7, 400, 200));
    }

    //Helper method to initialize card decks
    private Queue<EventCard> initializeDeck(String deckType) {
        Queue<EventCard> deck = new Queue<EventCard>();
        if ("chance".equalsIgnoreCase(deckType)) {
            deck.enqueue(new EventCard("Bank pays you dividend of $50.", 50));
            deck.enqueue(new EventCard("Speeding fine $15.", -15));
            deck.enqueue(new EventCard("Your building loan matures. Collect $150.", 150));
            deck.enqueue(new EventCard("Pay poor tax of $15.", -15));
        }
        else {
            deck.enqueue(new EventCard("Bank error in your favor. Collect $200.", 200));
            deck.enqueue(new EventCard("Doctor's fees. Pay $50.", -50));
            deck.enqueue(new EventCard("From sale of stock you get $50.", 50));
            deck.enqueue(new EventCard("Pay hospital fees of $100.", -100));
        }
        return deck;
    }

    //Check if given player is bankrupt
    @SuppressWarnings("unused")
    private boolean checkBankrupt(Player player) {
        if (player.getMoney() <= 0 && player.getOwnedProperties().size() == 0) {
            return true;
        }
        return false;
    }

}
