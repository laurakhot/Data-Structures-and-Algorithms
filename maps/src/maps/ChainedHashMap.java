package maps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    private static double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 1;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 10;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 5;
    private int givenChainCapacity;
    private int size;
    //private ArrayList<AbstractIterableMap<K, V>[]> chainedMap;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    AbstractIterableMap<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!

    /**
     * Constructs a new ChainedHashMap with default resizing load factor threshold,
     * default initial chain count, and default initial chain capacity.
     */
    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
        size = 0;
    }

    /**
     * Constructs a new ChainedHashMap with the given parameters.
     *
     * @param resizingLoadFactorThreshold the load factor threshold for resizing. When the load factor
     *                                    exceeds this value, the hash table resizes. Must be > 0.
     * @param initialChainCount the initial number of chains for your hash table. Must be > 0.
     * @param chainInitialCapacity the initial capacity of each ArrayMap chain created by the map.
     *                             Must be > 0.
     */
    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = resizingLoadFactorThreshold;
        this.chains = createArrayOfChains(initialChainCount);
        givenChainCapacity = chainInitialCapacity;
        size = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    @Override
    public V get(Object key) {
        AbstractIterableMap<K, V> arr = chains[hash(key)];
        if (arr[0].getKey().equals(key)) {
            return arr[0].getValue();
        } else {
            for (int i = 1; i < arr.size(); i++) {
                if (arr[i].getKey().equals(key)) {
                    return arr[i].getValue();
                }
            }
        }
        return null;
    }


    @Override
    public V put(K key, V value) {
        if (loadFactor()) {
            // resize
            int newSize = DEFAULT_INITIAL_CHAIN_COUNT * 2;
            AbstractIterableMap<K, V>[] newArr = createArrayOfChains(newSize);
            for (int i = 0; i < chains.length; i++) {
                if (chains[i] != null) {
                    // array map iteraor to get element out, then rehash
                    // helper method for put(key, value, newChains)
                    // save old map in reference
                    // reassign chain to larger map
                    // for every entry call put  and rehash
                    AbstractIterableMap<K, V> toLoop = chains[i]; // chain that looping over
                    SimpleEntry<>(key, value) entry = new SimpleEntry<>(toLoop[i].getKey(), toLoop[i].getValue());
                    AbstractIterableMap<K, V> chain = createChain(givenChainCapacity); // chain that adding to
                    if (chain.size() == 1) {
                        int index = hash(toLoop[0].getkey());
                        newArr[index] = chain;
                        chain[0] = entry;
                    } else {
                        for (int j = 0; j < chain.size(); j++) {
                            int index = hash(chain[j].getkey());

                        }
                    }
                }
            }
        }
        // adding
        int index = hash(key);
        if (chains[index] == null && !loadFactor()) {
            AbstractIterableMap<K, V> arrayMap = createChain(givenChainCapacity);
            chains[index] = arrayMap;
            arrayMap[0] = new SimpleEntry<>(key, value);
            size++;
            return null;
        } else {
            // replace -> at front
            AbstractIterableMap<K, V> arr = chains[index];
            if (arr[0].getKey().equals(key)) {
                V oldVal = arr[0].getValue();
                arr[0].setValue(value);
                return oldVal;
            }
            // replace -> entire chain
            int i = 1;
            while (currEntry.hasNext()) {
                if (arr[i].getKey().equals(key)) {
                V oldVal = arr[i].getValue();
                arr[i].setValue(value);
                return oldVal;
                } else {
                    currEntry = currEntry.next;
                    i++;
                }
            }
        }
        return null;
    }

    private boolean loadFactor() {
        return (double) size / DEFAULT_INITIAL_CHAIN_COUNT > DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD;
    }

    private int hash(Object key) {
        int hashCode = key.hashCode();
        return hashCode % givenChainCapacity;
    }

    @Override
    public V remove(Object key) {
        AbstractIterableMap<K, V> arr = chains[hash(key)];
        if (arr[0].getKey().equals(key)) {
            arr[0] = null;
            size--;
            return arr[0].getValue();
        } else {
            int full = 0;
            while(currEntry.hasNext()) {
                full++;
                currEntry = currEntry.next;
            }
            for (int i = 0; i < arr.size(); i++) {
                if (arr[i].getKey().equals(key)) {
                    V result = arr[i].getValue();
                    if (i != full - 1) {
                        arr[i] = arr[full - 1];
                        arr[full-1] = null;
                    } else {
                        arr[i] = null;
                    }
                    return result;
                }
            }
            return null;
        }
    }

    @Override
    public void clear() {
        //int num = DEFAULT_INITIAL_CHAIN_COUNT;
        // AbstractIterableMap<K, V>[num];
        chains = new ChainedHashMap();
    }

    @Override
    public boolean containsKey(Object key) {
        AbstractIterableMap<K, V> arr = chains[hash(key)];
        if (arr[0].getKey().equals(key)) {
            return true;
        }
        for (int i = 1; i < arr.size(); i++) {
            if (arr[i].getKey().equals(key)) {
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
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ChainedHashMapIterator<>(this.chains);
    }

    // // TODO: after you implement the iterator, remove this toString implementation
    // // Doing so will give you a better string representation for assertion errors the debugger.
    // @Override
    // public String toString() {
    //     return super.toString();
    // }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private int currEntry;

        private int currChain;

        private AbstractIterableMap<K, V>[] chains;
        // You may add more fields and constructor parameters
        private int size;

        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.chains = chains;
            this.currChain = 0;
            AbstractIterableMap<K, V> arr = chains[0];
            this.currEntry = 0;
            this.size = size;
        }

        @Override
        public boolean hasNext() {
            if (chains[currChain] != null) {
                AbstractIterableMap<K, V> chain = chains[currChain];
                if (currEntry < chain.size() - 1) {
                    if (chain[currEntry + 1] != null) {
                        currEntry += 1;
                        return true;
                    }
                } else {
                    currEntry = 0;
                    currChain += 1;
                    while (chains[currChain] == null & currChain <= chains.length()) {
                        currChain += 1;
                    }
                    return true;
                }
            }
            return false;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (hasNext()) {
                hasNext();

                if (currEntry )
                AbstractIterableMap<K, V> chain = chains[currChain];

                return chain[currEntry + 1];
            }
        }
    }
}

// ask about -> size, .hasNext, indexing into abstractiterablemap, chains.length() for hasNext
