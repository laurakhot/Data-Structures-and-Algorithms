package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.ArrayHeapMinPQ;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        // return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Set<V> known = new HashSet<>();
        ExtrinsicMinPQ<V> pq = new DoubleMapMinPQ<>();
        pq.add(start, 0);
        HashMap<V, Double> distTo = new HashMap<>();
        distTo.put(start, 0.0);
        HashMap<V, E> edgeTo = new HashMap<>();
        V explore;

        // while (!pq.isEmpty()) {
        while (!pq.isEmpty()) {
            // need this? //when would pq be empty when start != end (disconnected graphs)
            explore = pq.removeMin();
            if (explore.equals(end)) {
                break;
            }
            Collection<E> outEdges = new ArrayList<>(graph.outgoingEdgesFrom(explore));

            for (E edge : outEdges) {
                double oldDist = distTo.getOrDefault(edge.to(), Double.POSITIVE_INFINITY);
                double newDist = distTo.get(edge.from()) + edge.weight();

                if (newDist < oldDist) {
                    distTo.put(edge.to(), newDist);
                    edgeTo.put(edge.to(), edge);
                    if (!pq.contains(edge.to())) {
                        pq.add(edge.to(), newDist);
                    } else {
                        pq.changePriority(edge.to(), newDist);
                    }
                }
            }
            known.add(explore);
        }
        // System.out.println("done with loop");
        return edgeTo;
    }
    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (start.equals(end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        if (!spt.containsKey(end)) {
            return new ShortestPath.Failure<>();
        }
        List<E> edges = new ArrayList<>(); //why have to build backwards?
        V current = end;
        while (!current.equals(start)) {
            E edge = spt.get(current);
            edges.add(edge);
            current = edge.from(); //when is edge.from() null??
        }
        Collections.reverse(edges);
        return new ShortestPath.Success<>(edges);
    }
}
