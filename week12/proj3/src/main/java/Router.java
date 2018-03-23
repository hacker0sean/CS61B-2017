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
        LinkedList<Long> route_List = new LinkedList<>();
        route_List.add(last);
        PriorityQueue<GraphDB.Node> nodePriorityQueue = new PriorityQueue<>();

        nodePriorityQueue.add(g.nodes.get(first));
        GraphDB.Node temp;
        Set<GraphDB.Node> set = new HashSet<>();
        set.add(g.nodes.get(first));
        while (true){
            temp = nodePriorityQueue.poll();
            for (Long i : g.adjacent(temp.getId())){
                if (!(set.contains(i))){

                }
            }
            if (isGoal(temp))
                break;
        }
        Stack<Long> stack = new Stack<>();
        while ((map.get(temp)) != null){
            stack.push(temp);
            temp = map.get(temp);
        }
        while (!stack.isEmpty()){
            route_List.add(stack.pop());
        }
        return route_List;
    }
}
