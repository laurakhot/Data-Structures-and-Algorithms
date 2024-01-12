package problems;

import datastructures.LinkedIntList;
// Checkstyle will complain that this is an unused import until you use it in your code.
import datastructures.LinkedIntList.ListNode;

/**
 * See the spec on the website for example behavior.
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not call any methods on the `LinkedIntList` objects.
 * - do not construct new `ListNode` objects for `reverse3` or `firstToLast`
 *      (though you may have as many `ListNode` variables as you like).
 * - do not construct any external data structures such as arrays, queues, lists, etc.
 * - do not mutate the `data` field of any node; instead, change the list only by modifying
 *      links between nodes.
 */

public class LinkedIntListProblems {

    /**
     * Reverses the 3 elements in the `LinkedIntList` (assume there are exactly 3 elements).
     */
    public static void reverse3(LinkedIntList list) {
        ListNode temp = list.front;
        list.front = temp.next.next; // object in list
        list.front.next = temp.next;
        list.front.next.next = temp;
        temp.next = null;
    }

    /**
     * Moves the first element of the input list to the back of the list.
     */
    public static void firstToLast(LinkedIntList list) {
        if (list.front != null && list.front.next != null) {
            ListNode temp = list.front;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = list.front;
            list.front = list.front.next;
            temp.next.next = null;
        }

    }

    /**
     * Returns a list consisting of the integers of a followed by the integers
     * of n. Does not modify items of A or B.
     */
    public static LinkedIntList concatenate(LinkedIntList a, LinkedIntList b) {
        if (a.front == null) {
            return b;
        } else if (b.front == null) {
            return a;
        }
        LinkedIntList combined = new LinkedIntList();
        combined.front = new ListNode(a.front.data);
        ListNode temp = combined.front;
        ListNode tempA = a.front;
        while (tempA.next != null) {
            temp.next = new ListNode(tempA.next.data);
            temp = temp.next;
            tempA = tempA.next;
        }
        ListNode tempB = b.front;
        while (tempB != null) {
            temp.next = new ListNode(tempB.data);
            temp = temp.next;
            tempB = tempB.next;
        }
        return combined;
    }
}


