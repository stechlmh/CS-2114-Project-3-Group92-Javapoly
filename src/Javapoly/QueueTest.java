package Javapoly;

import java.util.NoSuchElementException;
import student.TestCase;

/**
 * Tests the Queue class.
 *
 * @author Pratham Singh
 * @version Nov 11, 2026
 */
public class QueueTest extends TestCase
{
    private Queue<String> queue;

    /**
     * Sets up an empty queue for each test.
     */
    public void setUp()
    {
        queue = new Queue<String>();
    }

    /**
     * Tests enqueue and size on normal inputs.
     */
    public void testEnqueueAndSize()
    {
        assertEquals(0, queue.size());
        queue.enqueue("A");
        assertEquals(1, queue.size());
        queue.enqueue("B");
        assertEquals(2, queue.size());
    }

    /**
     * Tests that enqueueing a null item throws IllegalArgumentException.
     */
    public void testEnqueueNull()
    {
        Exception thrown = null;
        try
        {
            queue.enqueue(null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(0, queue.size());
    }

    /**
     * Tests FIFO order for dequeue and peek leaving size unchanged.
     */
    public void testDequeueAndPeek()
    {
        queue.enqueue("A");
        queue.enqueue("B");

        assertEquals("A", queue.peek());
        assertEquals(2, queue.size());

        assertEquals("A", queue.dequeue());
        assertEquals(1, queue.size());
        assertEquals("B", queue.peek());

        assertEquals("B", queue.dequeue());
        assertEquals(0, queue.size());

        queue.enqueue("C");
        assertEquals("C", queue.pop());
        assertEquals(0, queue.size());
    }

    /**
     * Tests that dequeue and peek on an empty queue throw an exception.
     */
    public void testDequeueAndPeekEmpty()
    {
        Exception dequeueThrown = null;
        try
        {
            queue.dequeue();
        }
        catch (Exception e)
        {
            dequeueThrown = e;
        }
        assertNotNull(dequeueThrown);
        assertTrue(dequeueThrown instanceof NoSuchElementException);

        Exception peekThrown = null;
        try
        {
            queue.peek();
        }
        catch (Exception e)
        {
            peekThrown = e;
        }
        assertNotNull(peekThrown);
        assertTrue(peekThrown instanceof NoSuchElementException);
    }

    /**
     * Tests isEmpty on empty, populated, and emptied queues.
     */
    public void testIsEmpty()
    {
        assertTrue(queue.isEmpty());
        queue.enqueue("A");
        assertFalse(queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());

        queue.enqueue("B");
        assertFalse(queue.isEmpty());
        assertEquals("B", queue.peek());
    }
}
