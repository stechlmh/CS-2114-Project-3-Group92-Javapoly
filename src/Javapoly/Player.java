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
}
