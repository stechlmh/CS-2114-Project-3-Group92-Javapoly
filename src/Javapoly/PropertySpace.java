package Javapoly;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 * 
 *  @author maple
 *  @version Nov 5, 2026
 */
public class PropertySpace extends Space
{
    //~ Fields ................................................................
    @SuppressWarnings("javadoc")
    int price;
    /**
     * set of rent values under different conditions (uses (int houses + 1) as the index)
     *      - Base Value
     *      - Property Set
     *      - 1 House
     *      - 2 Houses
     *      - 3 Houses
     *      - 4 Houses
     *      - Hotel
     */
    int[] rent;
    /**
     * -1 Houses = Base Value
     * 0 Houses = Property Set
     * 5 Houses = 1 Hotel;
     */
    int houses;
    @SuppressWarnings("javadoc")
    boolean purchased;
    @SuppressWarnings("javadoc")
    Player owner;
    
    //~ Constructors ..........................................................
    @SuppressWarnings("javadoc")
    PropertySpace(String name, int index, String effect, int price, int[] rent)
    {
        super(name, index, effect);
        this.price = price;
        this.rent = rent;
        this.houses = 0;
        purchased = false;
        owner = null;
    }
    
    //~Public  Methods ........................................................
    @SuppressWarnings("javadoc")
    public int getPrice() {
        return price;
    }
    @SuppressWarnings("javadoc")
    public void setPrice(int price) {
        this.price = price;
    }
    
    @SuppressWarnings("javadoc")
    public int[] getRent() {
        return rent;
    }
    @SuppressWarnings("javadoc")
    public void setRent(int[] rent) {
        this.rent = rent;
    }
    
    @SuppressWarnings("javadoc")
    public int getHouses() {
        return houses;
    }
    @SuppressWarnings("javadoc")
    public void setHouses(int houses) {
        this.houses = houses;
    }
    
    @SuppressWarnings("javadoc")
    public boolean getPurchased() {
        return purchased;
    }
    @SuppressWarnings("javadoc")
    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }
    
    @SuppressWarnings("javadoc")
    public Player getOwner() {
        return owner;
    }
    @SuppressWarnings("javadoc")
    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
