package graphs.minspantrees;

import disjointsets.DisjointSets;
// import disjointsets.QuickFindDisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        // return new QuickFindDisjointSets<>();
        // /*
        // Disable the line above and enable the one below after you've finished implementing
        // your `UnionBySizeCompressingDisjointSets`.
        //  */
        return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));

        if (graph.allVertices().isEmpty()) {
            return new MinimumSpanningTree.Success<>();
        }

        DisjointSets<V> disjointSets = createDisjointSets(); // individual sets of vertices
        for (V vertex: graph.allVertices()) {
            disjointSets.makeSet(vertex);
        }

        HashSet<E> temp = new HashSet<>();

        for (E edge : edges) {
            V u = edge.from();
            V v = edge.to();
            int uMST = disjointSets.findSet(v);
            int vMST = disjointSets.findSet(u);
            if (uMST != vMST) {
                temp.add(edge);
                disjointSets.union(u, v);
            }
        }
        if (temp.size() == graph.allVertices().size() - 1) {
            return new MinimumSpanningTree.Success<>(temp);
        }
        return new MinimumSpanningTree.Failure<>(); //if MST not connected then MST doesn't exit
    }
}
