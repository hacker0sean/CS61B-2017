package db;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class datacol {
    ArrayList<DateEntry> col;
    int length;

    public datacol(){
        length = 0;
        col = new ArrayList<>();
    }

    public void addItem(DateEntry x){
        col.add(x);
        length += 1;
    }

    public ArrayList<DateEntry> NDdeleteItem(int x){
        ArrayList<DateEntry> temp = (ArrayList<DateEntry>) col.clone();
        temp.remove(x);
        return temp;
    }

    public ArrayList<DateEntry> clone(){
        ArrayList<DateEntry> temp = (ArrayList<DateEntry>) col.clone();
        return temp;
    }

    public void deleteItem(int x){
        col.remove(x);
        length -= 1;
    }

    public void addList(ArrayList<DateEntry> x){
        for (DateEntry i : x)
            this.addItem(i);
    }

    public DateEntry getEntry(int i){
        return col.get(i);
    }

    public void printcol(){
        for (int i = 0; i < length; i++){
            System.out.print(col.get(i).value());
            if (i != length - 1){
                System.out.print(",");
            }
        }
        System.out.println();
    }

    public static class test{

        @Test
        public void test(){
            IntEntry a = new IntEntry("1");
            FloatEntry b = new FloatEntry("2.2459");
            StringEntry c = new StringEntry("c");
            ArrayList<DateEntry> test = new ArrayList<>();
            test.add(a);
            test.add(b);
            test.add(c);
            datacol tst = new datacol();
            tst.addList(test);
            tst.printcol();

        }
    }
}
