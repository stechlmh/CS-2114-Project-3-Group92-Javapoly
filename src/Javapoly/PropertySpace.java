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
    /**
     * Set this property belongs to
     */
    PropertySpace[] set;
    /**
     * Type of property set this property belongs to
     *      - "Color"
     *      - "Railroad"
     *      - "Utility"
     */
    String propertyType;
    //~ Constructors ..........................................................
    @SuppressWarnings("javadoc")
    public PropertySpace(String name, int index, String effect, int price, int[] rent, PropertySpace[] set, String propertyType)
    {
        super(name, index, effect);
        if (price < 0)
        {
            throw new IllegalArgumentException("Cost cannot be negative");
        }
        this.price = price;
        this.rent = rent;
        this.houses = 0;
        purchased = false;
        owner = null;
        this.set = set;
        this.propertyType = propertyType;
    }

    /**
     * Creates a simple property space for MVP with purchase cost and base rent.
     *
     * @param name property name
     * @param index board index
     * @param cost purchase cost
     * @param baseRent rent amount
     */
    public PropertySpace(String name, int index, int cost, int baseRent)
    {
        this(name, index, "Property", cost, new int[] { baseRent },
            new PropertySpace[0], "Color");
        if (baseRent < 0)
        {
            throw new IllegalArgumentException("Rent cannot be negative");
        }
    }

    //~Public  Methods ........................................................
    @SuppressWarnings("javadoc")
    public int getPrice() {
        return price;
    }

    @SuppressWarnings("javadoc")
    public int getCost() {
        return price;
    }

    @SuppressWarnings("javadoc")
    public void setPrice(int price) {
        this.price = price;
    }
    
    @SuppressWarnings("javadoc")
    public int[] getRentArray() {
        return rent;
    }

    @SuppressWarnings("javadoc")
    public int getRent() {
        if (rent == null || rent.length == 0) {
            return 0;
        }
        int idx = Math.max(0, Math.min(houses, rent.length - 1));
        return rent[idx];
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
    
    public void triggerEffect(Player lander) {
        if (lander == null) {
            return;
        }
    }

}
