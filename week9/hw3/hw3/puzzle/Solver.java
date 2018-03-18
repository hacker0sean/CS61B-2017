package hw3.puzzle;
import edu.princeton.cs.algs4.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Solver{
    private MinPQ<WorldState> worldStateMinPQ;
    private int step;
    private int minMove;
    private Map<WorldState, Integer> map;
    private ArrayList<WorldState> pathArray;
    private Map<WorldState, WorldState> map2;
    private Map<WorldState, Integer> DistanceCache;
    private class WorldCompareClass implements Comparator<WorldState>{
        @Override
        public int compare(WorldState o1, WorldState o2){
            int o1Dis;
            int o2Dis;
            if (!(DistanceCache.containsKey(o1))){
                DistanceCache.put(o1, o1.estimatedDistanceToGoal());
            }
            if (!(DistanceCache.containsKey(o2))){
                DistanceCache.put(o2, o2.estimatedDistanceToGoal());
            }
            o1Dis = DistanceCache.get(o1);
            o2Dis = DistanceCache.get(o2);
            return map.get(o1) + o1Dis - map.get(o2) - o2Dis;
        }
    }

    public Comparator<WorldState> Worldcomparator(){
        return new WorldCompareClass();
    }

    /**Constructor which solves the puzzle, computing
     everything necessary for moves() and solution() to
     not have to solve the problem again. Solves the
     puzzle using the A* algorithm. Assumes a solution exists.*/
    public Solver(WorldState initial){
        DistanceCache = new HashMap<>();
        worldStateMinPQ = new MinPQ<WorldState>(50, Worldcomparator());
        map = new HashMap<>();
        pathArray = new ArrayList<>();
        map2 = new HashMap<>();
        step = 0;
        map.put(initial, step);
        worldStateMinPQ.insert(initial);
        map2.put(initial, null);
        Stack<WorldState> stack = new Stack<>();
        WorldState temp;
        while (true){
            temp = worldStateMinPQ.delMin();
            step = map.get(temp) + 1;
            for (WorldState i : temp.neighbors()){
                if (!(map.containsKey(i))){
                    map.put(i, step);
                    worldStateMinPQ.insert(i);
                    map2.put(i, temp);
                }
            }
            if (temp.isGoal())
                break;
        }
        minMove = map.get(temp);
        while ((map2.get(temp)) != null){
            stack.push(temp);
            temp = map2.get(temp);
        }
        while (!stack.isEmpty()){
            pathArray.add(stack.pop());
        }
    }

    /**Returns the minimum number of moves to solve the puzzle starting
     at the initial WorldState.*/
    public int moves() {
        return minMove;
    }

    /**Returns a sequence of WorldStates from the initial WorldState
     to the solution.*/
    public Iterable<WorldState> solution() {
        return pathArray;
    }
}