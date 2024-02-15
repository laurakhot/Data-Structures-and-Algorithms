package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    public int size;
    public HashMap<T, Double> hash;

    // public int currIndex;
    List<PriorityNode<T>> items;


    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        size = 0;
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
    }

    @Override
    public void add(T item, double priority) {
        PriorityNode<T> node = new PriorityNode<>(item, priority);
        items.add(size, node);
        hash.put(item, priority);
        size++;
        int currIndex = size - 1;
        if (size > 1) {
            PriorityNode<T> parent = items.get((items.indexOf(item) - 1) / 2);
            while (parent.getPriority() > node.getPriority()) {
                if ((currIndex - 1)/ 2 >= 0){
                    swap(currIndex, (currIndex - 1) / 2);
                    currIndex = (currIndex - 1) / 2;
                    parent = items.get((currIndex - 1) / 2);
                    node = items.get(currIndex);
                }
            }
        }
    }

    @Override
    public boolean contains(T item) {
        return hash.containsKey(item);
    }

    @Override
    public T peekMin() {
        return items.get(0).getItem();
    }

    @Override
    public T removeMin() {
        if (size != 0) {
            size--;
            T result = items.get(0).getItem();
            items.set(0, items.get(size));
            items.remove(size);
            hash.remove(result);
            int currIndex = 0;
            PriorityNode<T> leftChild = items.get(1);
            PriorityNode<T> rightChild = items.get(2);
            while ((items.get(currIndex).getPriority() > leftChild.getPriority())
                    || (items.get(currIndex).getPriority() > rightChild.getPriority())) {
                if (rightChild.getPriority() > leftChild.getPriority() && currIndex * 2 + 1 < size) {
                    int leftIndex = currIndex * 2 + 1;
                    swap(currIndex, leftIndex);
                    if (currIndex * 2 + 1 < size) {
                        currIndex = leftIndex;
                        leftChild = items.get(currIndex * 2 + 1);
                    }
                } else if (rightChild.getPriority() < leftChild.getPriority() && currIndex * 2 + 2 < size) {
                    int rightIndex = currIndex * 2 + 2;
                    swap(currIndex, rightIndex);
                    if (currIndex * 2 + 2 < size) {
                        currIndex = rightIndex;
                        rightChild = items.get(currIndex * 2 + 2);
                    }
                }
            }
            return result;
        }
        return null;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (hash.containsKey(item)) {
            int tempIndex = items.indexOf(item);
            items.get(items.indexOf(item)).setPriority(priority);
            int parentIndex;
            int leftChild;
            int rightChild;

            if (((tempIndex - 1) / 2) >= 0) {
                parentIndex = (tempIndex - 1) / 2;
            }
            if (((tempIndex * 2) + 1) {
                leftChild = tempIndex * 2 + 1;
            }
            if (((tempIndex * 2) + 2) {
                rightChild = tempIndex * 2 + 2;
            }
            while (items.get(parentIndex).getPriority() > priority || priority > items.get(leftChild).getPriority()
                || priority > items.get(rightChild).getPriority()) {
                if (items.get(parentIndex).getPriority() > priority) {
                    swap(parentIndex, tempIndex);
                    tempIndex = parentIndex;
                    if (((tempIndex - 1) / 2) >= 0) {
                        parentIndex = (tempIndex - 1) / 2;
                    }
                } else if (priority > items.get(leftChild).getPriority()) {
                    swap(tempIndex, leftChild);
                    tempIndex = leftChild;
                    if (tempIndex * 2 + 1 < size) {
                        leftChild = tempIndex * 2 + 1;
                    }
                } else if (priority > items.get(rightChild).getPriority()) {
                    swap(tempIndex, rightChild;
                        tempIndex = rightChild;
                    if (tempIndex * 2 + 2 < size) {
                        rightChild = tempIndex * 2 + 2;
                    }
                }
            }
        }
    }

    @Override
    public int size() {
        return size;
    }
}
