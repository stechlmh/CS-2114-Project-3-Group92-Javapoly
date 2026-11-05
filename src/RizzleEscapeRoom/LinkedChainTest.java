package RizzleEscapeRoom;

// -------------------------------------------------------------------------
/**
 *  Tests LinkedChain class
 * 
 *  @author maple
 *  @version Oct 8, 2026
 */
public class LinkedChainTest extends student.TestCase
{
    //~ Fields ................................................................
    private LinkedChain<String> chain;
    private LinkedChain<String> chainEmpty;
    private LinkedChain<String> chainSame;
    private LinkedChain<String> chainDifferent;

    /**
     * Sets up a game with two rooms, two fun facts, and 3 lives.
     */
    public void setUp()
    {
        chain = new LinkedChain<String>();
        chain.add("Apple");
        chain.add("Banana");
        chain.add("Orange");
        chainEmpty = new LinkedChain<String>();
        chainSame = new LinkedChain<String>();
        chainSame.add("Apple");
        chainSame.add("Banana");
        chainSame.add("Orange");
        chainDifferent = new LinkedChain<String>();
        chainDifferent.add("Apple");
        chainDifferent.add("Apple");
        chainDifferent.add("Banana");
    }
    
    //~ Constructors ..........................................................
    // ----------------------------------------------------------
    /**
     * Tests adding null
     */
    public void testAddNull() {
        try {
            chain.add(null);
        }
        catch (IllegalArgumentException e){
            assertEquals(e.getMessage(), "Cannot add null to chain");
        }
    }
    // ----------------------------------------------------------
    /**
     * Tests adding String
     */
    public void testAddString() {
        chain.add("Cherry");
        assertEquals(chain.toString(), "[Cherry, Orange, Banana, Apple]");
    }
    // ----------------------------------------------------------
    /**
     * Tests adding to end
     */
    public void testAddToEnd() {
        chain.addToEnd("Cherry");
        assertEquals(chain.toString(), "[Orange, Banana, Apple, Cherry]");
    }
 // ----------------------------------------------------------
    /**
     * Tests toArray
     */
    public void testToArray() {
        String[] arrayChain =  new String[3];
        chain.toArray(arrayChain);
        String[] arr =  {"Orange","Banana","Apple"};
        try {
            chain.toArray(null);
        }
        catch (IllegalArgumentException e){
            assertEquals(e.getMessage(), "Array must not be null");
        }
        try {
            chain.toArray(new String[1]);
        }
        catch (IllegalArgumentException e){
            assertEquals(e.getMessage(), "Array of length 1 is too small for 3 entries");
        }
        assertEquals(arr[0], arrayChain[0]);
        assertEquals(arr[1], arrayChain[1]);
        assertEquals(arr[2], arrayChain[2]);
    }
    // ----------------------------------------------------------
    /**
     * Tests isEmpty
     */
    public void testIsEmpty() {
        assertTrue(chainEmpty.isEmpty());
        assertFalse(chain.isEmpty());
    }
    /**
     * Tests size
     */
    public void testSize() {
        assertEquals(chain.size(), 3);
        assertEquals(chainEmpty.size(), 0);
    }
    // ----------------------------------------------------------
    /**
     * tests count
     */
    public void testCount() {
        assertEquals(chain.count("Apple"), 1);
        assertEquals(chainEmpty.count("Apple"), 0);
        assertEquals(chainDifferent.count("Apple"), 2);
    }
    /**
     * tests contains
     */
    public void testContains() {
        assertTrue(chain.contains("Apple"));
        assertFalse(chainEmpty.contains("Apple"));
        assertFalse(chain.contains("Plum"));
        assertFalse(chain.contains(null));
    }
    /**
     * tests get
     */
    public void testGet() {
        assertEquals(chain.get(0), "Orange");
        assertEquals(chain.get(2), "Apple");
        try {
            chain.get(4);
        }
        catch (IndexOutOfBoundsException e){
            assertEquals(e.getMessage(), "Index 4 out of bounds for size 3");
        }
        try {
            chain.get(-1);
        }
        catch (IndexOutOfBoundsException e){
            assertEquals(e.getMessage(), "Index -1 out of bounds for size 3");
        }
    }
    /**
     * tests clear
     */
    public void testClear() {
        chain.clear();
        assertTrue(chain.isEmpty());
        assertFalse(chainSame.isEmpty());
    }
    /**
     * tests remove
     */
    public void testRemove() {
        chain.remove();
        assertEquals(chain.toString(), "[Banana, Apple]");
        assertNull(chainEmpty.remove());
    }
    /**
    * tests remove(entry)
    */
   public void testRemoveEntry() {
       assertTrue(chain.remove("Banana"));
       assertEquals(chain.remove("Grape"), false);
       assertEquals(chainEmpty.remove("Grape"), false);
       assertEquals(chainEmpty.remove(null), false);
   }
}
