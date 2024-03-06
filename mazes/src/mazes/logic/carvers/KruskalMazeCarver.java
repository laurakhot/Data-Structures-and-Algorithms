package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTree;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        // Room -> vertex
        // Wall -> edge
        // Collection<EdgeWithData<Room, Wall>> edges
        //  public EdgeWithData(V from, V to, double weight, T data) {
        // System.out.println("HERE");
        ArrayList<EdgeWithData<Room, Wall>> edges = new ArrayList<>();
        for (Wall wall : walls) {
            EdgeWithData<Room, Wall> temp = new EdgeWithData<>(wall.getRoom1(),
                wall.getRoom2(), rand.nextInt(), wall);
            edges.add(temp);
        }
        MinimumSpanningTree<Room, EdgeWithData<Room, Wall>> tree =
            this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(edges));
        Set<Wall> result = new HashSet<>();
            for (EdgeWithData<Room, Wall> edge : tree.edges()) {
            result.add(edge.data());
        }
            return result;
    }
}
