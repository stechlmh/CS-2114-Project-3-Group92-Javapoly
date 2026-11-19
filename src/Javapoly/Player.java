package Javapoly;

import java.util.ArrayList;
import java.util.List;

// -------------------------------------------------------------------------
/**
 * Stores player name, balance, board position, owned properties, and
 * bankruptcy status.
 * 
 * @author Pratham Singh
 * @version Nov 11, 2026
 */
public class Player
{
    //~ Fields ................................................................
    private String name;
    private int money;
    private Space position;
    private List<PropertySpace> ownedProperties;
    private boolean isBankrupt;
    private boolean inJail;
    private int jailTurns;
    private int getOutOfJailCards;

    //~ Constructors ..........................................................
    /**
     * Creates a default player with $1500 starting money.
     */
    public Player()
    {
        this("Player", 1500, null);
    }

    /**
     * Creates a player with the given name and $1500 starting money.
     *
     * @param name the player's name
     */
    public Player(String name)
    {
        this(name, 1500, null);
    }

    /**
     * Creates a player with the given name, starting balance, and position.
     *
     * @param name the player's name
     * @param money the starting balance
     * @param position the starting board space
     */
    public Player(String name, int money, Space position)
    {
        this.name = name;
        this.money = money;
        this.position = position;
        this.ownedProperties = new ArrayList<PropertySpace>();
        this.isBankrupt = false;
        this.inJail = false;
        this.jailTurns = 0;
        this.getOutOfJailCards = 0;
    }

    //~Public  Methods ........................................................
    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the player's name.
     *
     * @param name the new name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the player's balance without modifying it.
     *
     * @return the current balance
     */
    public int getMoney()
    {
        return money;
    }

    /**
     * Sets the player's balance.
     *
     * @param money the new balance
     */
    public void setMoney(int money)
    {
        this.money = money;
    }

    /**
     * Returns the player's current location on the board.
     *
     * @return the current space
     */
    public Space getPosition()
    {
        return position;
    }

    /**
     * Sets the player's current location on the board.
     *
     * @param position the new space
     */
    public void setPosition(Space position)
    {
        this.position = position;
    }

    /**
     * Returns the list of properties owned by this player.
     *
     * @return the owned properties list
     */
    public List<PropertySpace> getOwnedProperties()
    {
        return ownedProperties;
    }

    /**
     * Returns whether the player has been eliminated.
     *
     * @return true if bankrupt, false otherwise
     */
    public boolean isBankrupt()
    {
        return isBankrupt;
    }

    /**
     * Sets the player's bankruptcy status.
     *
     * @param isBankrupt the new bankruptcy status
     */
    public void setBankrupt(boolean isBankrupt)
    {
        this.isBankrupt = isBankrupt;
    }

    /**
     * Returns whether the player is currently in jail.
     *
     * @return true if in jail, false otherwise
     */
    public boolean isInJail()
    {
        return inJail;
    }

    /**
     * Sets whether the player is in jail and resets jail turns when released.
     *
     * @param inJail true if in jail, false otherwise
     */
    public void setInJail(boolean inJail)
    {
        this.inJail = inJail;
        if (!inJail)
        {
            this.jailTurns = 0;
        }
    }

    /**
     * Returns the number of turns spent in jail during the current stay.
     *
     * @return the jail turn count
     */
    public int getJailTurns()
    {
        return jailTurns;
    }

    /**
     * Increments the number of turns spent in jail by one.
     */
    public void incrementJailTurns()
    {
        this.jailTurns++;
    }

    /**
     * Returns the number of Get Out of Jail Free cards held by the player.
     *
     * @return held card count
     */
    public int getGetOutOfJailCards()
    {
        return getOutOfJailCards;
    }

    /**
     * Sets the number of Get Out of Jail Free cards held by the player.
     *
     * @param getOutOfJailCards the new card count
     * @throws IllegalArgumentException if getOutOfJailCards is negative
     */
    public void setGetOutOfJailCards(int getOutOfJailCards)
    {
        if (getOutOfJailCards < 0)
        {
            throw new IllegalArgumentException("Card count cannot be negative");
        }
        this.getOutOfJailCards = getOutOfJailCards;
    }
}
