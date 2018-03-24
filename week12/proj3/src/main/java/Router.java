import java.util.*;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a LinkedList of <code>Long</code>s representing the shortest path from st to dest,
     * where the longs are node IDs.
     */
    public static LinkedList<Long> shortestPath(GraphDB g, double stlon, double stlat, double destlon, double destlat) {
        Long first = g.closest(stlon, stlat);
        Long last = g.closest(destlon, destlat);
        GraphDB.Node firstNode = g.nodes.get(first);
        GraphDB.Node lastNode = g.nodes.get(last);
        LinkedList<Long> route_List = new LinkedList<>();
        route_List.add(last);
        PriorityQueue<GraphDB.Node> nodePriorityQueue = new PriorityQueue<>();

        nodePriorityQueue.add(firstNode);
        GraphDB.Node temp = null;
        Map<Long, Double> distTo = new HashMap<>();
        Map<Long, Long> edgeTo = new HashMap();
        distTo.put(first, 0.0);
        while (true){
            temp = nodePriorityQueue.poll();
            for (Long i : g.adjacent(temp.getId())){
                GraphDB.Node x = g.nodes.get(i);
                double Pri = distTo.get(temp.getId()) + g.distance(i, temp.getId());
                    if (Pri < x.getPriority()){
                        distTo.put(i, distTo.get(temp.getId()) + g.distance(temp.getId(), i));
                        edgeTo.put(i, temp.getId());
                        x.setPriority(Pri);
                        if (nodePriorityQueue.contains(x)){
                            nodePriorityQueue.remove(x);
                            nodePriorityQueue.add(x);
                        }
                        else {
                            nodePriorityQueue.add(x);

                        }
                    }
            }
            if (temp.getId() == last){
                break;
            }
        }
        Long edge = edgeTo.get(last);
        while (!edge.equals(first)){
            route_List.addFirst(edge);
            edge = edgeTo.get(edge);
        }
        route_List.addFirst(first);
        return route_List;
    }
}
