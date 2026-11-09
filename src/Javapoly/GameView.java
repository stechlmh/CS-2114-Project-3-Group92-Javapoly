package Javapoly;
// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 * 
 *  @author ?????
 *  @version Nov 5, 2026
 */
public class GameView
{
    //~ Fields ................................................................

    //~ Constructors ..........................................................

    //~Public  Methods ........................................................
    public boolean triggerPurchase(Player player, Object obj, int price) {
        boolean answer = false;
        if(player.getMoney() < price) {
            //to be implemented, allow chance to mortgage properties, trade with
            //players, or sell houses, etc.
        }
        //ask if player wants to purchase, if they do then answer = true
        return answer;
    }
}
