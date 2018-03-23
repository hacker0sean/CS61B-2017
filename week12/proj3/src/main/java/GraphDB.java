import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */


    Map<Long, Node> nodes;

    public static class Node implements Comparable<Node>{
        private Set<Node> adjNodes;
        private Set<Long> adjID;
        private String name;
        private double lat;
        private double lon;
        private long id;
        private int adjNumber;
        private double priority;
        public void setPriority(double priority){
            this.priority = priority;
        }
        public Node(long id, double lat, double lon) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            name = null;
            adjID = new TreeSet<>();
            adjNumber = 0;
            priority = 0;
            adjNodes = new TreeSet<>();
        }
        public Set getAdj(){
            return adjID;
        }

        private void addAdjHelp(Node adj){
            adj.adjNodes.add(this);
            adj.adjNumber ++;
            adj.adjID.add(this.id);
        }

        public void addAdj(Node adj){
            adjNodes.add(adj);
            this.adjNumber ++;
            adjID.add(adj.id);
            addAdjHelp(adj);
        }

        public void addName(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }

        public long getId(){
            return id;
        }

        public double getLat(){
            return lat;
        }

        public double getLon(){
            return lon;
        }

        public double getPriority(){
            return priority;
        }
        @Override
        public int compareTo(Node node) {
            return (int) (this.priority - node.priority);
        }
    }
    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            nodes = new HashMap<>();
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }


    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }


    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Set<Long> removeSet = new TreeSet<>();
        for (Long i : nodes.keySet()){
            Node x = nodes.get(i);
            if (x.adjNumber == 0){
                removeSet.add(i);
            }
        }
        for (Long i: removeSet){
            nodes.remove(i);
        }
    }

    /** Returns an iterable of all vertex IDs in the graph. */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return nodes.keySet();
    }

    /** Returns ids of all vertices adjacent to v. */
    Iterable<Long> adjacent(long v) {
        Node x = nodes.get(v);
        return x.adjID;

    }

    /** Returns the Euclidean distance between vertices v and w, where Euclidean distance
     *  is defined as sqrt( (lonV - lonV)^2 + (latV - latV)^2 ). */
    double distance(long v, long w) {
        Node a = nodes.get(v);
        Node b = nodes.get(w);
        return Math.sqrt(Math.pow((a.lon - b.lon), 2) + Math.pow((a.lat - b.lat), 2));
    }

    double distance(long o, double lon, double lat){
        return Math.sqrt(Math.pow(lon(o) - lon, 2) + Math.pow(lat(o) - lat, 2));
    }
    /** Returns the vertex id closest to the given longitude and latitude. */
    long closest(double lon, double lat) {
        double mindistance = Double.MAX_VALUE;
        long minNode = Long.MAX_VALUE;
        for (Long i : nodes.keySet()){
            double temp = Math.pow((lon(i) - lon), 2) + Math.pow((lat(i) - lat), 2);
            if (temp < mindistance){
                mindistance = temp;
                minNode = i;
            }
        }
        return minNode;
    }

    /** Longitude of vertex v. */
    double lon(long v) {
        return nodes.get(v).lon;
    }

    /** Latitude of vertex v. */
    double lat(long v) {
        return nodes.get(v).lat;
    }

    public void addNode(Node c){
        nodes.put(c.id, c);
    }

}
