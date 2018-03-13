package lab8;

import org.omg.CORBA.OBJ_ADAPTER;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{
    private Entry root;

    private class Entry <K, V>{
        private K key;
        private V values;
        private Entry left;
        private Entry right;
        private Entry parent;
        private int N;

        public Entry(K key, V values, Entry left, Entry right, Entry parent, int N){
            this.key = key;
            this.values = values;
            this.left = left;
            this.right = right;
            this.parent = parent;
            this.N = N;
        }

        public K getkey(){
            return key;
        }
    }

    @Override
    public int size(){
        return size(root);
    }

    private int size(Entry x){
        if (x == null)
            return 0;
        else
            return x.N;
    }

    @Override
    public V get(K key){
        return get(root, key);
    }

    private V get(Entry x, K key){
        if (x == null)
            return null;
        if (key.compareTo((K)x.key) > 0){
            return get(x.right, key);
        }
        else if (key.compareTo((K)x.key) < 0){
            return get(x.left, key);
        }
        else{
            return (V)x.values;
        }
    }

    @Override
    public void put(K key, V value){
        root = put(root, key, value, null);
    }

    private Entry put (Entry x, K key, V value, Entry father){
        if (x == null)
            return new Entry(key, value, null, null, father, 1);
        else if (((K)x.key).compareTo(key) > 0){
            x.left = put(x.left, key, value, x);
        }
        else if (((K)x.key).compareTo(key) < 0){
            x.right = put(x.right, key, value, x);
        }
        else{}
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    @Override
    public void clear(){
        root = null;
    }

    @Override
    public boolean containsKey(K key){
        if (get(key) == null)
            return false;
        return true;
    }

    @Override
    public Set<K> keySet(){
        Set<K> result= new HashSet<>();
        for (K k : this){
            result.add(k);
        }
        return result;
    }

    public void printBST(){
        for (K k : this){
            System.out.println(k);
        }
    }

    @Override
    public  Iterator<K> iterator(){
        return new BSTIterator();
    }


    private Entry succ(Entry x){
        if (x.right == null){
            if (x.parent.left == x){
                return x.parent;
            }
            else{
                while ((x.parent != null) && x.parent.right == x)
                    x = x.parent;
                x = x.parent;
                return x;
            }
        }
        else {
            x = x.right;
            while (x.left != null) {
                x = x.left;
            }
            return x;
        }
    }

    private class BSTIterator implements Iterator<K>{
        private Entry start;

        public BSTIterator(){
            start = root;
            while (start.left != null){
                start = start.left;
            }
        }

        @Override
        public boolean hasNext(){
            return (start != null);
        }

        @Override
        public K next(){
            K result = (K)start.key;
            start = succ(start);
            return result;
        }
    }

    private Entry findByKey(Entry x, K key){
        if (x == null)
            return null;
        if (key.compareTo((K)x.key) > 0){
            return findByKey(x.right, key);
        }
        else if (key.compareTo((K)x.key) < 0){
            return findByKey(x.left, key);
        }
        else{
            return x;
        }
    }

    @Override
    public V remove(K key){
        if (!containsKey(key))
            return null;
        else{
            Entry x = findByKey(root, key);
            V result = (V) x.values;
            if ((x.left == null) && (x.right == null)){
                x = null;
            }
            else if ((x.left == null) && (x.right != null)){
                if (x.parent.left == x) {
                    x.right.parent = x.parent;
                    x.parent.left = x.right;
                }
                else {
                    x.right.parent = x.parent;
                    x.parent.right = x.right;
                }
                x = null;
            }
            else if (((x.left) != null) && (x.right == null)){
                if (x.parent.left == x) {
                    x.left.parent = x.parent;
                    x.parent.left = x.left;
                }
                else {
                    x.left.parent = x.parent;
                    x.parent.right = x.left;
                }
                x = null;
            }
            else{
                Entry suc = succ(x);
                x.key = suc.key;
                x.values = suc.values;
                if (suc.parent.left == suc){
                    suc.parent.left = null;
                }
                else{
                    suc.parent.right = null;
                }
                suc = null;
            }
            return result;
        }
    }

    @Override
    public V remove(K key, V value){
        throw new UnsupportedOperationException();
    }
}
