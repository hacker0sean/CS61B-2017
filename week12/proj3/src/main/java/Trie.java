import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Trie {
    private class Node{
        boolean exist;
        Map<Character, Node> links;

        public Node(){
            links = new TreeMap<>();
            exist = false;
        }
    }

    private Node root = new Node();

    public void put(String key){
        put(root, key, 0);
    }

    private int trasfer(int lowercase){
        return lowercase - 32;
    }

    private Node put(Node x, String key, int d){
        if (x == null)
            x = new Node();
        if (d == key.length()){
            x.exist = true;
            return x;
        }
        else{
            char m = key.charAt(d);
            x.links.put(m, put(x.links.get(m), key, d + 1));
            return x;
        }
    }

    public ArrayList<String> find(String prefix){
        ArrayList<String> x = new ArrayList<>();
        find(0, root, prefix, x);
        return x;
    }

    private void find(int d, Node x, String prefix, ArrayList<String> result){
        if (x == null)
            return ;
        if (d == prefix.length()){
            findAllChild(prefix, x, result);
            return ;
        }
        else{
            char m = prefix.charAt(d);

//            if (m != ' ') {
//                String low_to_upper;
//                if (d + 1 < prefix.length()) {
//                    low_to_upper = prefix.substring(0, d) + (char)trasfer((int) m) + prefix.substring(d + 1, prefix.length());
//                }
//                else
//                    low_to_upper = prefix.substring(0, d) + (char)trasfer((int) m);
//                find(d + 1, x.links.get((char)trasfer((int) m)), low_to_upper, result);
//            }
            find(d + 1, x.links.get(m), prefix, result);
        }
    }

    private void findAllChild(String prefix, Node x, ArrayList<String> result){
        if (x.exist)
            result.add(prefix);
        for (Character ch : x.links.keySet()){
                findAllChild(prefix + ch, x.links.get(ch), result);
        }
    }
}
