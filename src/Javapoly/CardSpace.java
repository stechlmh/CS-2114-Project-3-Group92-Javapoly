package Javapoly;

// -------------------------------------------------------------------------
/**
 *  Card space on the board that draws from a Queue of EventCards.
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
    public CardSpace(String name, int index, String effect, Queue<EventCard> cardSet)
    {
        super(name, index, effect);
        this.cardSet = cardSet;
    }

    //~Public  Methods ........................................................
    @SuppressWarnings("javadoc")
    public Queue<EventCard> getCardSet()
    {
        return cardSet;
    }

    @SuppressWarnings("javadoc")
    public void setCardSet(Queue<EventCard> cardSet)
    {
        this.cardSet = cardSet;
    }

    @Override
    public void triggerEffect(Player lander)
    {
        if (cardSet != null && !cardSet.isEmpty())
        {
            EventCard card = cardSet.dequeue();
            lander.setMoney(lander.getMoney() + card.getAmount());
            cardSet.enqueue(card);
        }
    }
}
