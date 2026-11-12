package Javapoly;

/**
 * Represents a Chance or Community Chest event card with a description and
 * signed money change effect.
 *
 * @author maple
 * @version Nov 11, 2026
 */
public class EventCard
{
    private String eventDescription;
    private int amount;

    /**
     * Creates an event card with a description and signed balance change.
     *
     * @param eventDescription text shown after drawing
     * @param amount signed balance change (positive gain, negative loss)
     */
    public EventCard(String eventDescription, int amount)
    {
        if (eventDescription == null || eventDescription.trim().isEmpty())
        {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        this.eventDescription = eventDescription;
        this.amount = amount;
    }

    /**
     * Returns the event message.
     *
     * @return the description
     */
    public String getDescription()
    {
        return eventDescription;
    }

    /**
     * Returns the signed money effect.
     *
     * @return the amount
     */
    public int getAmount()
    {
        return amount;
    }
}

