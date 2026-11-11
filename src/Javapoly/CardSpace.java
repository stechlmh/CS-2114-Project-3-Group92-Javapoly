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
public class CardSpace extends Space
{
    //~ Fields ................................................................
    /**
     * which set of cards does the card space trigger
     */
    Queue<EventCard> cardSet;
    //~ Constructors ..........................................................
    @SuppressWarnings("javadoc")
    CardSpace(String name, int index, String effect, int price, int[] rent, CardSpace[] set, String propertyType)
    {
        super(name, index, effect);
        this.cardType = price;
        this.rent = rent;
        this.houses = 0;
        purchased = false;
        owner = null;
        this.set = set;
        this.propertyType = propertyType;
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
    
    public void triggerEffect(Player lander) {
        if(!purchased) {
            if(GameView.triggerPurchase(lander, this, price)) {
                purchased = true;
                owner = lander;
                boolean ownsSet = true;
                for(int i = 0; i < set.length; i += 1) {
                    if(!set[i].getOwner().equals(owner)) {
                        ownsSet = false;
                    }
                }
                if(ownsSet) {
                    for(int i = 0; i < set.length; i += 1) {
                        set[i].setHouses(0);
                    }
                }
            }
            else {
                GameView.triggerAuction(lander, this, price);
            }
        }
        if(lander == owner) {
            String allHotels = "";
            String setHouses = "";
            for(int i = 0; i < set.length; i += 1) {
                allHotels += "" + 5;
                setHouses += "" + set[i].getHouses();
            }
            if(!allHotels.equals(setHouses)) {
                if(this.houses >= 1) {
                    while(true) {
                        if(GameView.triggerYesNo(lander, "Do you want to build houses on this property set?")) {
                            while(true) {
                                int choice = GameView.triggerPickFromOptions(lander, "Which property would you like to build houses on?", set);
                                
                                if(set[choice].getHouses() != 5) {
                                    break;
                                }
                                
                                GameView.triggerErrorPopup(lander, "Sorry, but that property already has a hotel! You can't build on it anymore.");
                            }
                            String[] houseOptions = new String[5-houses];
                            for(int i = 1; i < houseOptions.length; i += 1) {
                                if(i == 1) {
                                    houseOptions[i - 1] = new String ("1 House");
                                }
                                else {
                                    houseOptions[i - 1] = new String ("" + i + " Houses");
                                }
                            }
                            houseOptions[houseOptions.length-1] = "Hotel";
                            int choice = GameView.triggerPickFromOptions(lander, "What would you like to build on this property?", houseOptions);
                            if(GameView.triggerPurchase(lander, houseOptions[choice], (((index/10)+1)*50)*(choice + 1))) {
                                houses = houses + choice + 1;
                            }
                        }
                        else {
                            break;
                        }
                        allHotels = "";
                        setHouses = "";
                        for(int i = 0; i < set.length; i += 1) {
                            allHotels += "" + 5;
                            setHouses += "" + set[i].getHouses();
                        }
                        if(allHotels.equals(setHouses)) {
                            break;
                        }
                    }
                }
            }
        }
        else {
            GameView.triggerPayment(lander, owner, rent[houses]);
        }
    }

}
