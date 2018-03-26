

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.server.ExportException;
import java.util.*;

public class Boggle {
    private static boolean getPara = false;
    private static String Para;
    private int bwidth;
    private int bheight;
    private int bk;
    private String bpath;
    private boolean brandom;
    private Tries tries;
    private Character[][] boards;
    private PriorityQueue<String> results;
    private Node[][] nodes;

    private static void setGetPara(String i) {
        getPara = true;
        Para = i;
    }

    private static void checkValid(int k) {
        if (k < 0)
            throw new IllegalArgumentException();
    }

    private void generateDict(String path) throws RuntimeException, IOException {
        tries = new Tries();
        List<String> words = Files.readAllLines(Paths.get(path));
        for (String i : words) {
            tries.put(i);
        }
    }

    private void ReadBoards(Scanner sc) throws IllegalArgumentException {
        ArrayList<String> lines = new ArrayList<>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }
        int length1 = lines.size();
        int length2 = lines.get(0).length();
//        if (length1 != length2) {
//            throw new IllegalArgumentException();
//        }
        boards = new Character[length1][length1];
        for (int i = 0; i < length1; i++) {
            for (int j = 0; j < length2; j++) {
                Character ch = lines.get(i).charAt(j);
                boards[i][j] = ch;
            }
        }
//        for (int i = 0; i < length1; i++){
//            for (int j = 0; j < length2; j++){
//                System.out.print(boards[i][j].toString() + " ");
//            }
//            System.out.println();
//        }
        bwidth = length1;
        bheight = length2;
    }

    public Comparator<String> generatorStringComparator() {
        return new StringComparator();
    }

    private class StringComparator implements Comparator<String> {

        @Override
        public int compare(String t1, String t2) {
            return t2.length() - t1.length();
        }
    }

    public Boggle(int width, int height, int k, String path, boolean random, Scanner sc) throws RuntimeException, IOException {
        this.bwidth = width;
        this.bheight = height;
        this.bk = k;
        this.bpath = path;
        this.brandom = random;
        this.results = new PriorityQueue<>(generatorStringComparator());
        generateDict(bpath);
        if (brandom == true) {
            Random ran = new Random();
            boards = new Character[bwidth][bheight];
            for (int i = 0; i < bwidth; i++) {
                for (int j = 0; j < bheight; j++) {
                    int n = ran.nextInt(26);
                    boards[i][j] = (char) (n + 97);
                }
            }
        } else {
         //   Debug();
            ReadBoards(sc);
        }
    }

    public class Node {
        int x;
        int y;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    private void securityAdd(Stack<Node> adjnodes, int i, int j){
        if (i >= 0 && i < bwidth && j >= 0 && j < bheight){
            adjnodes.add(nodes[i][j]);
        }
    }

    private Stack<Node> FindAdj(Node h) {
        int i = h.x;
        int j = h.y;
        Stack<Node> adjnodes = new Stack<>();
        securityAdd(adjnodes, i - 1, j - 1);
        securityAdd(adjnodes, i - 1, j);
        securityAdd(adjnodes, i - 1, j + 1);
        securityAdd(adjnodes, i, j - 1);
        securityAdd(adjnodes, i, j + 1);
        securityAdd(adjnodes, i + 1, j - 1);
        securityAdd(adjnodes, i + 1, j);
        securityAdd(adjnodes, i + 1, j + 1);
        return adjnodes;
    }

//    private List<String> FindStrings(int i, int j) {
//        Stack<Stack<Node>> StacksOfNodes = new Stack<>();
//        List<String> strings = new ArrayList<>();
//        Node tempNode = new Node(i, j);
//        String temp = boards[i][j].toString();
//        Node newnode = null;
//        LinkedList<Node> path = new LinkedList<>();
//        path.add(nodes[i][j]);
//        Stack<Node> astack = null;
//        Boolean contin = false;
//
//        while (true) {
//            if (contin == true){
//                contin = false;
//            }
//            else {
//                if (tries.keysThatMatch(temp)){
//                    strings.add(temp);
//                }
//                astack = FindAdj(tempNode);
//                StacksOfNodes.push(astack);
//            }
//            if (tries.keyWithPrefix(temp).isEmpty()) {
//                if (StacksOfNodes.isEmpty()) {
//                    break;
//                }
//                StacksOfNodes.pop();
//                if (StacksOfNodes.isEmpty()) {
//                    return strings;
//                }
//                astack = StacksOfNodes.peek();
//                temp = temp.substring(0,temp.length() - 1);
//                path.removeLast();
//                contin = true;
//                continue;
//            }
//                do {
//                    if (astack.empty()){
//                        StacksOfNodes.pop();
//                        if (StacksOfNodes.isEmpty()) {
//                            return strings;
//                        }
//                        astack = StacksOfNodes.peek();
//                        temp = temp.substring(0, temp.length() - 1);
//                        path.removeLast();
//                        contin = true;
//                        break;
//                    }
//                    newnode = astack.pop();
//                } while (path.contains(newnode));
//                if (contin == false) {
//                    path.add(newnode);
//                    tempNode = newnode;
//                    temp = temp + boards[newnode.x][newnode.y].toString();
//                }
//        }
//        return strings;
//    }

    private List<String> FindStrings(int i, int j) {
        Stack<Stack<Node>> StacksOfNodes = new Stack<>();
        List<String> strings = new ArrayList<>();
        Node tempNode = new Node(i, j);
        String temp = boards[i][j].toString();
        Node newnode = null;
        LinkedList<Node> path = new LinkedList<>();
        path.add(nodes[i][j]);
        Stack<Node> astack = null;
        Boolean contin = false;
        if (tries.keyWithPrefix(temp).isEmpty())
            return strings;
        astack = FindAdj(tempNode);
        StacksOfNodes.push(astack);
        String newtemp = temp;
        while (StacksOfNodes.size() > 0) {
            if (contin == false) {
                if (tries.keysThatMatch(temp)) {
                    strings.add(temp);
                }
                astack = FindAdj(tempNode);
                StacksOfNodes.push(astack);
            } else{
                contin = false;
            }
            do {
                if (astack.empty()){
                    StacksOfNodes.pop();
                    if (StacksOfNodes.isEmpty()) {
                        return strings;
                    }
                    astack = StacksOfNodes.peek();
                    temp = temp.substring(0,temp.length() - 1);
                    path.removeLast();
                    contin = true;
                    break;
                }
                newnode = astack.pop();
                newtemp = temp + boards[newnode.x][newnode.y].toString();
            } while (path.contains(newnode) || tries.keyWithPrefix(newtemp).isEmpty());
            if (contin == false) {
                path.add(newnode);
                tempNode = newnode;
                temp = temp + boards[newnode.x][newnode.y].toString();
            }
        }
        return strings;
    }

    public void play() {
        nodes = new Node[bwidth][bheight];
        for (int i = 0; i < bwidth; i++) {
            for (int j = 0; j < bheight; j++) {
                nodes[i][j] = new Node(i, j);
            }
        }
        for (int i = 0; i < bwidth; i++) {
            for (int j = 0; j < bheight; j++) {
                List<String> StringsFromIJ = FindStrings(i, j);
                for (String m : StringsFromIJ){
                    if (!results.contains(m))
                        results.add(m);
                }
            }
        }
        while (bk > 0){
            if (results.isEmpty())
                break;
            System.out.println(results.poll());
            bk--;
        }
    }

    public void Debug() throws IOException {
        String FileNameForDebug = "testBoggle2";
        List<String> lines = Files.readAllLines(Paths.get(FileNameForDebug));
        int length1 = lines.size();
        int length2 = lines.get(0).length();
        boards = new Character[length1][length1];
        for (int i = 0; i < length1; i++) {
            for (int j = 0; j < length2; j++) {
                Character ch = lines.get(i).charAt(j);
                boards[i][j] = ch;
            }
        }
        for (int i = 0; i < length1; i++){
            for (int j = 0; j < length2; j++){
                System.out.print(boards[i][j].toString() + " ");
            }
            System.out.println();
        }
        bwidth = length1;
        bheight = length2;
    }

    public static void main(String[] args) {
        try {
            int width = 4;
            int height = 4;
            int k = 0;
            String path = "words";
            boolean random = false;

            for (String i : args) {
                if (!getPara) {
                    if (i.equals("-k") || i.equals("-n") || i.equals("-m") || i.equals("-d")) {
                        setGetPara(i);
                    } else if (i.equals("-r")) {
                        random = true;
                    } else {
                        throw new IllegalArgumentException();
                    }
                } else {
                    if (Para.equals("-k")) {
                        k = Integer.parseInt(i);
                        checkValid(k);
                    } else if (Para.equals("-n")) {
                        width = Integer.parseInt(i);
                        checkValid(width);
                    } else if (Para.equals("-m")) {
                        height = Integer.parseInt(i);
                        checkValid(height);
                    } else if (Para.equals("-d")) {
                        path = i;
                    }
                    getPara = false;
                }
            }
            Scanner sc = new Scanner(System.in);
            Boggle boggle = new Boggle(width, height, k, path, random, sc);
            boggle.play();
        } catch (Exception e) {
            StackTraceElement[] message = e.getStackTrace();
            for (int i = 0; i < message.length; i++) {
                System.out.println(message[i]);
            }
            throw new IllegalArgumentException();
        }
    }
}
