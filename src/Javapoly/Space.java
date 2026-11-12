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
public class Space
{    
    //~ Fields ................................................................
    @SuppressWarnings("javadoc")
    String name;
    @SuppressWarnings("javadoc")
    int index;
    /**
     * What happens when a player lands on the space
     *      - "Property"
     *      - "Event Card"
     *      - "Tax"
     *      - "Go To Jail"
     *      - "GO"
     *      - "Free Parking"
     */
    String effect;
    
    //~ Constructors ..........................................................
    @SuppressWarnings("javadoc")
    Space(String name, int index, String effect){
        this.name = name;
        this.index = index;
        this.effect = effect;
    }
    
    //~Public  Methods ........................................................
    @SuppressWarnings("javadoc")
    public String getName() {
        return name;
    }
    @SuppressWarnings("javadoc")
    public void setName(String name) {
        this.name = name;
    }
    
    @SuppressWarnings("javadoc")
    public int getIndex() {
        return index;
    }
    @SuppressWarnings("javadoc")
    public void setIndex(int index) {
        this.index = index;
    }
    
    @SuppressWarnings("javadoc")
    public String getEffect() {
        return effect;
    }
    @SuppressWarnings("javadoc")
    public void setEffect(String effect) {
        this.effect = effect;
    }
    
    // ----------------------------------------------------------
    /**
     * Will vary depending on subclass effect
     */
    public void triggerEffect(Player lander) {
        System.out.println(lander.getName() + " " + effect);
    }
}
