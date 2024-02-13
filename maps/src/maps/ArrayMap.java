package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    SimpleEntry<K, V>[] entries;
    public int size;

    // You may add extra fields or helper methods though!

    /**
     * Constructs a new ArrayMap with default initial capacity.
     */
    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Constructs a new ArrayMap with the given initial capacity (i.e., the initial
     * size of the internal array).
     *
     * @param initialCapacity the initial capacity of the ArrayMap. Must be > 0.
     */
    public ArrayMap(int initialCapacity) {
        if (initialCapacity > 0) {
            this.entries = this.createArrayOfEntries(initialCapacity);
            size = 0;
        }
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    @Override
    public V get(Object key) {
        for (int i = 0; i < size; i++) {
            if (entries[i].getKey().equals(key)) {
                return entries[i].getValue();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        for (int i = 0; i < size; i++) {
            if (entries[i].getKey().equals(key)) {
                V oldVal = entries[i].getValue();
                entries[i].setValue(value);
                return oldVal;
            }
        }
        if (size == entries.length) {
            SimpleEntry<K, V>[] newArray = createArrayOfEntries(size * 2);
            System.arraycopy(entries, 0, newArray, 0, entries.length);
            entries = newArray;
            entries[size] = new SimpleEntry<>(key, value);
        } else {
            entries[size] = new SimpleEntry<>(key, value);
        }
        size++;
        return null;
    }

    @Override
    public V remove(Object key) {
        for (int i = 0; i < size; i++) {
            if (entries[i].getKey().equals(key)) {
                V result = entries[i].getValue();
                if (size-1 != i) {
                    entries[i] = entries[size-1];
                    entries[size-1] = null;
                } else {
                    entries[i] = null;
                }
                size--;
                return result;
            }
        }
        return null;
    }

    @Override
    public void clear() {
       for (int i = 0; i < size; i++) {
           entries[i] = null;
       }
       size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (int i = 0; i < size; i++) {
            if (entries[i].getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: You may or may not need to change this method, depending on whether you
        // add any parameters to the ArrayMapIterator constructor.
        return new ArrayMapIterator<>(this.entries, this.size);
    }

    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;
        // You may add more fields and constructor parameters
        private int currIndex;
        private int size;

        public ArrayMapIterator(SimpleEntry<K, V>[] entries, int size) {
            this.entries = entries;
            this.currIndex = 0;
            this.size = size;
        }

        @Override
        public boolean hasNext() {
            return currIndex < size;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int index = currIndex;
            currIndex++;
            return entries[index];
        }
    }
}
