package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;
    public int size;
    public HashMap<T, Integer> hash;

    // public int currIndex;
    List<PriorityNode<T>> items;


    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        size = 0;
        items.add(null);
        hash = new HashMap<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
        hash.put(items.get(b).getItem(), b);
        hash.put(items.get(a).getItem(), a);
    }

    @Override
    public boolean contains(T item) {
        return hash.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return items.get(1).getItem();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(T item, double priority) {
        if (item == null || hash.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        PriorityNode<T> node = new PriorityNode<>(item, priority);
        items.add(node);
        size++;
        hash.put(item, size);
        percolateUp(size);
    }
    @Override
    public T removeMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        T result = items.get(1).getItem(); // Store the item at the root
        if (size == 1) {
            items.remove(1);
            hash.remove(result);
            size--;
        }
        if (size > 1) { // Check if there are elements to remove
            items.set(1, items.get(size)); // Replace the root with the last element
            items.remove(size); // Remove the last element
            hash.remove(result); // Remove the item from the hash map
            size--; // Decrease the size
            percolateDown(1);
        }
        return result; // Return the removed item
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!hash.containsKey(item)) {
            throw new NoSuchElementException();
        }
        int tempIndex = hash.get(item);
        items.get(hash.get(item)).setPriority(priority);
        PriorityNode<T> node = items.get(tempIndex);

        percolateUp(tempIndex);
        percolateDown(tempIndex);
    }
    public void percolateUp(int index) {
        int currIndex = index;
        while (currIndex > 1) {
            int parentIndex = currIndex / 2;
            PriorityNode<T> parent = items.get(parentIndex);
            PriorityNode<T> node = items.get(currIndex);
            if (parent.getPriority() > node.getPriority()) {
                swap(currIndex, parentIndex);
                currIndex = parentIndex;
            } else {
                break;
            }
        }
    }

    public void percolateDown(int index) {
        int currIndex = index; // Start from the root
        while (currIndex * 2 <= size) { // While the current node has at least one child
            int leftIndex = currIndex * 2;
            int rightIndex = currIndex * 2 + 1;
            int minChildIndex = leftIndex; // Assume left child is the minimum

            // Check if the right child exists and has smaller priority
            if (rightIndex <= size && items.get(rightIndex).getPriority() < items.get(leftIndex).getPriority()) {
                minChildIndex = rightIndex;
            }

            // Check if the current node's priority is greater than the minimum child's priority
            if (items.get(currIndex).getPriority() > items.get(minChildIndex).getPriority()) {
                swap(currIndex, minChildIndex); // Swap the current node with the minimum child
                currIndex = minChildIndex; // Update the current index to the position of the minimum child
            } else {
                break; // Break the loop if the current node is smaller than its children
            }
        }
    }
}

