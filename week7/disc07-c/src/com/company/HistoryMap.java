package com.company;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringStack;

import java.util.HashMap;
import java.util.Stack;

public class HistoryMap<K, V> extends HashMap<K, V> {
    Stack<Operation> entrystack = new Stack<Operation>();
    class Operation { /* Helper class */
        /* Place fields/variables here */
        boolean shouldRemove;
        K key;
        V value;

        /* Place the constructor here */
        public Operation(boolean shouldRemove, K key, V value){
            this.shouldRemove = shouldRemove;
            this.key = key;
            this.value = value;
        }
    }
    @Override
/** Remember that in a HashMap, a null value is valid */
    public V put(K key, V value) {
        entrystack.push(new Operation(!containsKey(key), key, super.get(key)));
        return super.put(key, value);
    }
    @Override
    public V remove(Object key) {
        entrystack.push(new Operation(false, (K)key, super.get(key)));
        return super.remove(key);
    }


    public void undo() {
        if (entrystack.isEmpty()) {
            return;
        }
        Operation temp = entrystack.pop();
        if (temp.shouldRemove) {
            super.remove(temp.key);
        } else {
            super.put(temp.key, temp.value);
        }
    }

    public static void main(String[] args) {
        HistoryMap<String, Integer> h = new HistoryMap<String, Integer>();
        h.put("party", 1);
        h.put("parrot", 2);
        h.put("conga", 4);
        h.put("parrot", 3);
        System.out.println(h);
        h.undo();
        h.undo();
        System.out.println(h); // Output: {parrot=2, party=1}
        h.remove("party");
        h.undo();
        System.out.println(h); // Output: {parrot=2, party=1}
    }
}