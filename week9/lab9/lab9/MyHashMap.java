package lab9;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MyHashMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private int M;
    private double loadFactor;
    private HashSet<K> keyset;
    private ArrayList<Entry<K, V>>[] st;


    @Override
    public HashSet<K> keySet(){
        return keyset;
    }

    @Override
    public int size(){
        if (keyset == null)
            return 0;
        return keyset.size();
    }

    @Override
    public V remove(K key){
        if (!(keyset.contains(key)))
            return null;
        else{
            M = M -1;
            V value = null;
            int hashcode = (key.hashCode()) % st.length;
            hashcode = hashcodeTransfer(hashcode);
            for (int i = 0; i < st[hashcode].size(); i++){
                if (st[hashcode].get(i).key().equals(key)) {
                    value = st[hashcode].get(i).value();
                    st[hashcode].remove(i);
                }
            }
            keyset.remove(key);
            return value;
        }
    }

    @Override
    public V remove(K key, V value){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(K key){
        if (keyset == null)
            return false;
        return keyset.contains(key);
    }


    public MyHashMap(){
        this(997);
    }

    public MyHashMap(int initialSize){
        this(initialSize, 2);
    }

    public MyHashMap(int initialSize, double loadFactor){
        M = initialSize;
        this.loadFactor = loadFactor;
        keyset = new HashSet<>();
        st = (ArrayList<Entry<K, V>>[])new ArrayList[M];
        for (int i = 0; i < M; i++){
            st[i] = new ArrayList<>();
        }
    }

    @Override
    public V get(K key){
        if (st == null)
            return null;
        int x = (key.hashCode()) % st.length;
        x = hashcodeTransfer(x);
        for (int i = 0; i < st[x].size(); i++){
            if (st[x].get(i).key().equals(key)){
                return st[x].get(i).value();
            }
        }
        return null;
    }

    @Override
    public void clear(){
        st = null;
        keyset = null;
        M = 0;
    }

    @Override
    public void put(K key, V value){
        M = M + 1;
        if (M / st.length >= loadFactor){
            resize();
        }
        int hashcode = (key.hashCode()) % st.length;
        hashcode = hashcodeTransfer(hashcode);
        if (keyset.contains(key)){
            for (int i = 0; i < st[hashcode].size(); i++){
                if (st[hashcode].get(i).key().equals(key))
                    st[hashcode].set(i, new Entry(key, value));
            }
        }
        else{
            st[hashcode].add((Entry<K, V>)new Entry(key, value));
        }
        keyset.add(key);
    }

    private int hashcodeTransfer(int hashcode){
        while (hashcode < 0){
            hashcode = hashcode + st.length;
        }
        return hashcode;
    }

    @Override
    public Iterator<K> iterator(){
        return keyset.iterator();
    }

    private void resize(){
        M = st.length * 4;
        ArrayList<Entry<K, V>>[] temp = st;
        st = (ArrayList<Entry<K, V>>[])new ArrayList[M];
        for (int i = 0; i < M; i++){
            st[i] = new ArrayList<>();
        }
        for (K key : keyset){
            V value = get(key);
            int hashcode = (key.hashCode()) % M;
            hashcode = hashcodeTransfer(hashcode);
            st[hashcode].add(new Entry(key, value));
        }
    }
}
