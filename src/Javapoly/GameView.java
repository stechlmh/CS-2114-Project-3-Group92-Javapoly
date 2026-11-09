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
    public static boolean triggerPurchase(Player player, Object obj, int price) 
    {
        boolean answer = false;
        //to be implemented, allow chance to mortgage properties, trade with
        //players, or sell houses, etc.
        if(player.getMoney() < price) {
            //not able to purchase
        }
        //ask if player wants to purchase, if they do then answer = true
        return answer;
    }

    public static boolean triggerYesNo(Player lander, String string)
    {
        boolean answer = false;
        //to be implemented, asks the player yes/no question, yes = true
        return answer;
        
    }

    public static int triggerPickFromOptions(Player lander, String string,
        Object[] options)
    {
        int choice = options.length - 1;
        // to be implemented, asks the player to pick from a set of options, 
        //returns int 0 for first item, int 1 for second, etc
        return choice;
    }

    public static void triggerErrorPopup(Player lander, String string)
    {
        // to be implemented, triggers popup to tell player something went wrong, press enter to continue
    }

    public static void triggerPayment(Player payer, Player reciever, int money)
    {
        // to be implemented, gives money from the payer to the reciever 
    }

    public static void triggerAuction(Player auctioneer, Object obj, int basePrice)
    {
        // to be implemented, auction event, auctioneer doesn't have a chance to purchase
    }
}
