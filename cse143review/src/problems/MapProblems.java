package problems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * See the spec on the website for example behavior.
 */
public class MapProblems {

    /**
     * Returns true if any string appears at least 3 times in the given list; false otherwise.
     */
    public static boolean contains3(List<String> list) {
        Map<String, Integer> counts = new HashMap<>();
        for (String word : list) {
            if (!counts.containsKey(word)) {
                counts.put(word, 1);
            } else {
                int num = counts.get(word);
                counts.put(word, num + 1);
            }
        }

        for (String word : counts.keySet()) {
            if (counts.get(word) >= 3) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a map containing the intersection of the two input maps.
     * A key-value pair exists in the output iff the same key-value pair exists in both input maps.
     */
    public static Map<String, Integer> intersect(Map<String, Integer> m1, Map<String, Integer> m2) {
        Map<String, Integer> result = new HashMap<>();
        for (String word : m1.keySet()) {
            if (m2.containsKey(word) && m1.get(word) == m2.get(word)) {
                result.put(word, m1.get(word));
            }
        }
        return result;
    }
}
