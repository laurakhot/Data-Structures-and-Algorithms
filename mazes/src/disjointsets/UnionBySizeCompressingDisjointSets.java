package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
// import java.util.List;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    ArrayList<Integer> pointers;
    private int size;
    private HashMap<T, Integer> ids;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        this.pointers = new ArrayList<>();
        this.size = 0;
        this.ids = new HashMap<>();

    }

    @Override
    public void makeSet(T item) {
        pointers.add(size, -1);
        ids.put(item, size);
        size++;
    }

    @Override
    public int findSet(T item) {
        if (!ids.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int index = ids.get(item);
        // finding the root
        while (pointers.get(index) >= 0) {
            index = pointers.get(index);
        }
        // path compression
        int rootIndex = index; // what will return
        index = ids.get(item);
        while (index != rootIndex) {
            int val = pointers.get(index);
            pointers.set(index, rootIndex);
            index = val;
        }
        return rootIndex;
    }

    @Override
    public boolean union(T item1, T item2) {
        if (!ids.containsKey(item1) || !ids.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int root1 = findSet(item1);
        int root2 = findSet(item2);
        // in same set
        if (root1 == root2) {
            return false;
        }
        // union by size
        if (pointers.get(root1) * -1 > pointers.get(root2) * -1) {
            int add = pointers.get(root2);
            pointers.set(root2, root1);
            pointers.set(root1, pointers.get(root1) + add);
        } else {
            int add = pointers.get(root1);
            pointers.set(root1, root2);
            pointers.set(root2, pointers.get(root2) + add);
        }
        return true;
    }
}
