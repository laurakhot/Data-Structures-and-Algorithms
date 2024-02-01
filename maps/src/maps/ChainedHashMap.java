package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    private static double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 0.8;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 10;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 5;
    private final int givenChainCapacity;
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
        if (!containsKey(key)) {
            return null;
        } else {
            AbstractIterableMap<K, V> arrMap = chains[hash(key, chains)];
            return arrMap.get(key);
        }
        // for (Map.Entry<K, V> entry: arrMap) {
        //     if (entry != null && entry.getKey().equals(key)) {
        //         return entry.getValue();
        //     }
        // }
        // return null;
    }

    @Override
    public V put(K key, V value) {
        // first check if containsKey cuz then don't need to resize
        if (containsKey(key)) {
            AbstractIterableMap<K, V> arrMap = chains[hash(key, chains)];
            return arrMap.put(key, value);
            // for (Map.Entry<K, V> entry : arrMap) {
            //     if (entry.getKey().equals(key)) {
            //         V result = entry.getValue();
            //         entry.setValue(value);
            //         return result;
            //     }
            // }
        } else {
            if (loadFactor()) {
                int newSize = chains.length * 2;
                AbstractIterableMap<K, V>[] newArr = createArrayOfChains(newSize);
                for (AbstractIterableMap<K, V> arrMap : chains) {
                    if (arrMap != null) {
                        for (Entry<K, V> entry : arrMap) {
                            copy(entry.getKey(), entry.getValue(), newArr);
                        }
                    }
                }
                chains = newArr;
            }
            if (chains[hash(key, chains)] == null) {
                AbstractIterableMap<K, V> chain = createChain(givenChainCapacity);
                chains[hash(key, chains)] = chain;
            }
            size++;
            return chains[hash(key, chains)].put(key, value);
        }
    }

    private void copy(K key, V value, AbstractIterableMap<K, V>[] arr) {
        int index = hash(key, arr);
        if (arr[index] == null) {
            AbstractIterableMap<K, V> chain = createChain(givenChainCapacity);
            arr[index] = chain;
        }
        arr[index].put(key, value);
    }

    private boolean loadFactor() {
        return (double) size / chains.length > DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD;
    }

    private int hash(Object key, AbstractIterableMap<K, V>[] arr) {
        int hashCode = key.hashCode();
        return Math.abs(hashCode % arr.length);
    }

    @Override
    // issue with swapping the last element with the one being removed (iterator)
    public V remove(Object key) {
        if (!containsKey(key)) {
            return null;
        } else {
            size--;
            AbstractIterableMap<K, V> arrMap = chains[hash(key, chains)];
            return arrMap.remove(key);
            // V result = arrMap.get(key);
            // if (arrMap.size() == 1) {
            //     chains[hash(key)] = null;
            // } else {
            //     arrMap.remove(key);
            // }
            // return result;
        }
        //
        // Iterator<Map.Entry<K, V>> iterator = chains[hash(key)].iterator();
        // Entry<K, V> last = null;
        // while (iterator.hasNext()) {
        //     last = iterator.next();
        // }
        // for (Map.Entry<K, V> entry: arrMap) {
        //     if (entry.getKey().equals(key)) {
        //         size--;
        //         V result = entry.getValue();
        //         entry = last;
        //         if (last != result) { // is this the proper check for having only 1 entry in the chain
        //             arrMap.remove(last.getKey()); // can do this???
        //         }
        //         return result;
        //     }
        // }
        // return null;
    }


    @Override
    public void clear() {
        chains = createArrayOfChains(DEFAULT_INITIAL_CHAIN_COUNT);
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        AbstractIterableMap<K, V> arrMap = chains[hash(key, chains)];
        if (arrMap != null) {
            return arrMap.containsKey(key);
        } else {
            return false;
        }

        // for (Map.Entry<K, V> entry: arrMap) {
        //     if (entry.getKey().equals(key)) {
        //         return true;
        //     }
        // }
        // return false;
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

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private int currChain;
        // private Entry<K, V> currEntry;
        private AbstractIterableMap<K, V>[] chains;
        // You may add more fields and constructor parameters
        private Iterator<Map.Entry<K, V>> iterator;

        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.chains = chains;
            this.currChain = 0;
            // this.currEntry = null;
            this.iterator = null;
        }

        @Override
        // find non-null, non-empty arrMap, then reassign iterator
        // dont modify any fields in hasNext()
        public boolean hasNext() {
            int nextChain = currChain;
            while (nextChain < chains.length) {
                while (chains[nextChain] == null || chains[nextChain].size() == 0) {
                    nextChain++;
                }
                iterator = chains[nextChain].iterator();
                if (iterator.hasNext()) {
                    return true;
                } else {
                    nextChain++;
                }
            }
            return false;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            while (currChain < chains.length) {
                while (chains[currChain] == null || chains[currChain].size() == 0) {
                    currChain++;
                }
                AbstractIterableMap<K, V> arrMap = chains[currChain];
                iterator = arrMap.iterator();
                if (iterator.hasNext()) {
                    return iterator.next();
                } else {
                    currChain++;
                }
            }
            throw new NoSuchElementException();
        }
        //     AbstractIterableMap<K, V> arrMap = chains[currChain];
        //     iterator = arrMap.iterator();
        //     while (!iterator.hasNext()) {
        //         while (chains[currChain] == null || chains[currChain].size() == 0) {
        //             currChain++;
        //             iterator = chains[currChain].iterator();
        //         }
        //     }
        //     return iterator.next();
    }    // }
}
