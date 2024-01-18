package deques;

/**
 * @see Deque
 */
public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back;
    Node<T> sentBack;
    Node<T> sentFront;
    // Feel free to add any additional fields you may need, though.

    public LinkedDeque() {
        size = 0;
        sentFront = new Node<>(null);
        sentBack = new Node<>(null);
        sentFront.next = sentBack;
        sentBack.prev = sentFront;
        front = sentFront;
        back = sentBack;
    }

    public void addFirst(T item) {
        if (size == 0) {
            Node<T> newNode = new Node<>(item, front, back);
            front.next = newNode;
            back.prev = newNode;
        } else {
            Node<T> temp = front.next;
            front.next = new Node<>(item, front, temp);
            temp.prev = front.next;
        }
        size += 1;
    }

    public void addLast(T item) {
        if (size == 0) {
            Node<T> newNode = new Node<>(item, front, back);
            front.next = newNode;
            back.prev = newNode;
        } else {
            Node<T> temp = back.prev;
            temp.next = new Node<>(item, temp, back);
            back.prev = temp.next;
        }
        size += 1;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T result = front.next.value;
        if (size == 1) {
            front.next = back;
            back.prev = front;
        } else {
            Node<T> first = front.next.next;
            front.next = first;
            first.prev = front;
        }
        size -= 1;
        return result;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T result = back.prev.value;
        if (size == 1) {
            front.next = back;
            back.prev = front;
        } else {
            Node<T> last = back.prev.prev;
            last.next = back;
            back.prev = last;
        }
        size -= 1;
        return result;
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        Node<T> curr = front.next;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr.value;
    }

    public int size() {
        return size;
    }
}
