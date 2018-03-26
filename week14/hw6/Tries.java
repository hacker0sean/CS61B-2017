import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Tries {
    private class Node{
        boolean exist;
        Map<Character, Node> links;

        public Node(){
            exist = false;
            links = new TreeMap<>();
        }
    }

    private Node root = new Node();

    public void put(String key){
        put(root, key, 0);
    }

    private Node put (Node node, String key, int d){
        if (node == null){
            node = new Node();
        }

        if (d == key.length()){
            node.exist = true;
            return node;
        }
        else{
            char m = key.charAt(d);
            node.links.put(m, put(node.links.get(m), key, d + 1));
            return node;
        }
    }

    public List<String> keyWithPrefix(String pre){
        List<String> keys = new ArrayList<String>();
        keyWithPrefix(root, pre, 0, keys);
        return keys;
    }

    private void keyWithPrefix(Node node, String pre, int d, List<String> keys){
        char m = pre.charAt(d);
        if (!node.links.containsKey(m))
            return ;
        if (d == pre.length() - 1){
            collect(node.links.get(m), pre, keys);
            return ;
        }
        else{
            keyWithPrefix(node.links.get(m), pre, d + 1, keys);
        }
    }

    private void collect(Node node, String pre, List<String> keys){
        if (node.exist){
            keys.add(pre);
        }
        for (Character i : node.links.keySet()){
            collect(node.links.get(i), pre + i.toString(), keys);
        }
    }

    public boolean keysThatMatch(String key){
        int d = 0;
        Node temp = root;
        while (d != key.length()){
            Character m = key.charAt(d);
            if (!temp.links.containsKey(m))
                return false;
            else{
                temp = temp.links.get(m);
            }
            d++;
        }
        return temp.exist;
    }
}
