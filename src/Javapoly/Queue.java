package Javapoly;

import java.util.NoSuchElementException;

/**
 * Singly linked FIFO queue for EventCards and other elements.
 *
 * @author Pratham Singh
 * @version Nov 11, 2026
 * @param <T> the type of elements stored in the queue
 */
public class Queue<T>
{
    private Node<T> front;
    private Node<T> rear;
    private int size;

    /**
     * Creates an empty queue.
     */
    public Queue()
    {
        front = null;
        rear = null;
        size = 0;
    }

    /**
     * Adds a non-null item to the rear of the queue.
     *
     * @param item the item to add
     * @throws IllegalArgumentException if item is null
     */
    public void enqueue(T item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException("Cannot enqueue null item");
        }
        Node<T> newNode = new Node<T>(item);
        if (isEmpty())
        {
            front = newNode;
        }
        else
        {
            rear.next = newNode;
        }
        rear = newNode;
        size++;
    }

    /**
     * Removes and returns the item at the front of the queue.
     *
     * @return the front item
     * @throws NoSuchElementException if the queue is empty
     */
    public T dequeue()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException("Queue is empty");
        }
        T data = front.data;
        front = front.next;
        if (front == null)
        {
            rear = null;
        }
        size--;
        return data;
    }

    /**
     * Removes and returns the item at the front of the queue (alias for
     * dequeue).
     *
     * @return the front item
     * @throws NoSuchElementException if the queue is empty
     */
    public T pop()
    {
        return dequeue();
    }

    /**
     * Returns the item at the front of the queue without removing it.
     *
     * @return the front item
     * @throws NoSuchElementException if the queue is empty
     */
    public T peek()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException("Queue is empty");
        }
        return front.data;
    }

    /**
     * Checks whether the queue is empty.
     *
     * @return true if the queue has no entries, false otherwise
     */
    public boolean isEmpty()
    {
        return front == null;
    }

    /**
     * Returns the number of entries in the queue.
     *
     * @return the queue entry count
     */
    public int size()
    {
        return size;
    }

    private static class Node<E>
    {
        private E data;
        private Node<E> next;

        private Node(E data)
        {
            this.data = data;
            this.next = null;
        }
    }
}
