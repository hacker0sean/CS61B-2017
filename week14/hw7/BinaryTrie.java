import java.io.Serializable;
import java.util.*;

public class BinaryTrie implements Serializable {
    Map<Character, Integer> frequencyTable;
    public HashSet<Node> nodes;
    public Node root;

    public class Node implements Serializable, Comparable<Node>{
        public Node left;
        public Node right;
        public boolean valid;
        public Character character;
        public int frequency;
        public boolean traverse;

        public Node(char character, int frequency){
            left = null;
            right = null;
            valid = true;
            this.frequency = frequency;
            this.character = character;
        }

        public Node(Node left, Node right){
            this.left = left;
            this.right = right;
            this.valid = false;
            this.frequency = left.frequency + right.frequency;
            this.character = null;
        }

        @Override
        public int compareTo(Node node) {
            return this.frequency - node.frequency;
        }
    }

    public BinaryTrie(Map<Character, Integer> frequencyTable) throws IllegalArgumentException{
        this.frequencyTable = frequencyTable;
        HashSet<Node> nodes_temp = new HashSet<>();
        for (Character i : frequencyTable.keySet()){
            nodes_temp.add(new Node(i, frequencyTable.get(i)));
        }
        this.nodes = nodes_temp;
        PriorityQueue<Node> characterPriorityQueue = new PriorityQueue<>();
        characterPriorityQueue.addAll(nodes);
        if (characterPriorityQueue.size() == 0){
            root = null;
            return;
        }
        if (characterPriorityQueue.size() == 1){
            root.left = characterPriorityQueue.poll();
            return;
        }
        while (characterPriorityQueue.size() > 1){
            Node x1 = characterPriorityQueue.poll();
            Node x2 = characterPriorityQueue.poll();
            characterPriorityQueue.add(new Node(x1, x2));
        }
        root = characterPriorityQueue.poll();
    }

    public Match longestPrefixMatch(BitSequence querySequence){
        int i = 0;
        Node temp = root;
        if (temp == null){
            return null;
        }
        while (!temp.valid){
            int left_Or_right = querySequence.bitAt(i);
            if (left_Or_right == 0){
                temp = temp.left;
            } else {
                temp = temp.right;
            }
            i++;
        }
        BitSequence resultSequence = new BitSequence(querySequence.firstNBits(i));
        return new Match(resultSequence, temp.character);
    }

    public Map<Character, BitSequence> buildLookupTable(){
        HashMap<Character, BitSequence> expected = new HashMap<Character, BitSequence>();
        LinkedList<Integer> track = new LinkedList<>();
        track.add(-1);
        Node temp = root;
        Stack<Node> node = new Stack<>();
        node.push(root);
        while (!track.isEmpty()){
            if (temp.valid){
                expected.put(temp.character, new BitSequence(track.toString().replaceAll("[^0|1]", "").substring(1)));
                temp.traverse = true;
                track.removeLast();
                temp = node.pop();
            }
            else if (temp.left != null && !temp.left.traverse){
                track.addLast(0);
                node.push(temp);
                temp = temp.left;
            }
            else if (temp.right != null && !temp.right.traverse){
                track.addLast(1);
                node.push(temp);
                temp = temp.right;
            }
            else{
                temp.traverse = true;
                temp = node.pop();
                track.removeLast();
                if (track.getLast().equals(-1) && temp.right.traverse){
                    break;
                }
            }
        }
        return expected;
    }
}
