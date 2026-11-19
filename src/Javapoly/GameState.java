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
        Space landed = board.movePlayer(current, steps);
        if ("GO_TO_JAIL".equalsIgnoreCase(landed.getEffect())) {
            sendToJail(current);
            return current.getPosition();
        }
        return landed;
    }

    /**
     * Sends the specified player directly to the Jail space and marks them
     * as in jail.
     *
     * @param player the player to send to jail
     * @throws IllegalArgumentException if player is null
     */
    public void sendToJail(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (board != null) {
            Space jailSpace = board.getSpaceByName("Jail / Just Visiting");
            if (jailSpace != null) {
                player.setPosition(jailSpace);
            }
        }
        player.setInJail(false);
        player.setInJail(true);
    }

    /**
     * Resolves a jail turn for the given player by paying $50 bail, using a
     * held Get Out of Jail Free card, or waiting up to 3 turns.
     *
     * @param player the player in jail
     * @param payBail true if the player chooses to pay $50 bail
     * @return true if the player is released from jail, false if still in jail
     * @throws IllegalArgumentException if player is null or cannot afford bail
     * @throws IllegalStateException if player is not currently in jail
     */
    public boolean handleJailTurn(Player player, boolean payBail) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (!player.isInJail()) {
            throw new IllegalStateException("Player is not in jail");
        }
        if (payBail) {
            if (player.getMoney() < 50) {
                throw new IllegalArgumentException("Insufficient funds to pay $50 bail");
            }
            player.setMoney(player.getMoney() - 50);
            player.setInJail(false);
            return true;
        }
        if (player.getGetOutOfJailCards() > 0) {
            player.setGetOutOfJailCards(player.getGetOutOfJailCards() - 1);
            player.setInJail(false);
            return true;
        }
        player.incrementJailTurns();
        if (player.getJailTurns() >= 3) {
            player.setInJail(false);
            return true;
        }
        return false;
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

        String descLower = card.getDescription().toLowerCase();
        if (descLower.contains("go to jail")) {
            sendToJail(current);
        }
        else if (descLower.contains("get out of jail free")) {
            current.setGetOutOfJailCards(current.getGetOutOfJailCards() + 1);
        }

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

        // Bottom Row (0–9)
       board.addSpace(new Space("GO", 0, "GO"));
       board.addSpace(new PropertySpace("Mediterranean Avenue", 1, 60, 2));
       board.addSpace(new CardSpace("Community Chest", 2, "COMMUNITY", communityDeck));
       board.addSpace(new PropertySpace("Baltic Avenue", 3, 60, 4));
       board.addSpace(new Space("Income Tax", 4, "TAX"));
       board.addSpace(new PropertySpace("Reading Railroad", 5, 200, 25));
       board.addSpace(new PropertySpace("Oriental Avenue", 6, 100, 6));
       board.addSpace(new CardSpace("Chance", 7, "CHANCE", chanceDeck));
       board.addSpace(new PropertySpace("Vermont Avenue", 8, 100, 6));
       board.addSpace(new PropertySpace("Connecticut Avenue", 9, 120, 8));

       // Left Row (10–19)
       board.addSpace(new Space("Jail / Just Visiting", 10, "JAIL"));
       board.addSpace(new PropertySpace("St. Charles Place", 11, 140, 10));
       board.addSpace(new PropertySpace("Electric Company", 12, 150, 0));
       board.addSpace(new PropertySpace("States Avenue", 13, 140, 10));
       board.addSpace(new PropertySpace("Virginia Avenue", 14, 160, 12));
       board.addSpace(new PropertySpace("Pennsylvania Railroad", 15, 200, 25));
       board.addSpace(new PropertySpace("St. James Place", 16, 180, 14));
       board.addSpace(new CardSpace("Community Chest", 17, "COMMUNITY", communityDeck));
       board.addSpace(new PropertySpace("Tennessee Avenue", 18, 180, 14));
       board.addSpace(new PropertySpace("New York Avenue", 19, 200, 16));

       // Top Row (20–29)
       board.addSpace(new Space("Free Parking", 20, "FREE_PARKING"));
       board.addSpace(new PropertySpace("Kentucky Avenue", 21, 220, 18));
       board.addSpace(new CardSpace("Chance", 22, "CHANCE", chanceDeck));
       board.addSpace(new PropertySpace("Indiana Avenue", 23, 220, 18));
       board.addSpace(new PropertySpace("Illinois Avenue", 24, 240, 20));
       board.addSpace(new PropertySpace("B&O Railroad", 25, 200, 25));
       board.addSpace(new PropertySpace("Atlantic Avenue", 26, 260, 22));
       board.addSpace(new PropertySpace("Ventnor Avenue", 27, 260, 22));
       board.addSpace(new PropertySpace("Water Works", 28, 150, 0));
       board.addSpace(new PropertySpace("Marvin Gardens", 29, 280, 24));

       // Right Row (30–39)
       board.addSpace(new Space("Go To Jail", 30, "GO_TO_JAIL"));
       board.addSpace(new PropertySpace("Pacific Avenue", 31, 300, 26));
       board.addSpace(new PropertySpace("North Carolina Avenue", 32, 300, 26));
       board.addSpace(new CardSpace("Community Chest", 33, "COMMUNITY", communityDeck));
       board.addSpace(new PropertySpace("Pennsylvania Avenue", 34, 320, 28));
       board.addSpace(new PropertySpace("Short Line", 35, 200, 25));
       board.addSpace(new CardSpace("Chance", 36, "CHANCE", chanceDeck));
       board.addSpace(new PropertySpace("Park Place", 37, 350, 35));
       board.addSpace(new Space("Luxury Tax", 38, "TAX"));
       board.addSpace(new PropertySpace("Boardwalk", 39, 400, 50));

    }

    //Helper method to initialize card decks
    private Queue<EventCard> initializeDeck(String deckType) {
        Queue<EventCard> deck = new Queue<EventCard>();
        if ("chance".equalsIgnoreCase(deckType)) {
            deck.enqueue(new EventCard("Bank pays you dividend of $50.", 50));
            deck.enqueue(new EventCard("Speeding fine $15.", -15));
            deck.enqueue(new EventCard("Your building loan matures. Collect $150.", 150));
            deck.enqueue(new EventCard("Pay poor tax of $15.", -15));
            deck.enqueue(new EventCard("Go to Jail. Go directly to Jail.", 0));
        }
        else {
            deck.enqueue(new EventCard("Bank error in your favor. Collect $200.", 200));
            deck.enqueue(new EventCard("Doctor's fees. Pay $50.", -50));
            deck.enqueue(new EventCard("From sale of stock you get $50.", 50));
            deck.enqueue(new EventCard("Pay hospital fees of $100.", -100));
            deck.enqueue(new EventCard("Get Out of Jail Free.", 0));
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
