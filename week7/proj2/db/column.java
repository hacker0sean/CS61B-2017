package db;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class column {

    // Instance Variables:
    ArrayList<ColEntry> col;
    int length;

    // Constructor:
    public column(){
        col = new ArrayList<>();
        length = 0;
    }

    //method:
    public void addItem(ColEntry x){
        for (int i = 0; i < col.size(); i += 1){
            if ((col.get(i).name() == x.name()) && (col.get(i).type() == x.type())){
                System.out.println("You can't add the same item twice");
                return ;
            }
        }
        col.add(x);
        length += 1;
    }

    public void addList(ArrayList<ColEntry> x){
        for (ColEntry i : x)
            this.addItem(i);
    }

    public void deleteItem(int i){
        col.remove(i);
    }

    public ArrayList<ColEntry> NDdeleteItem(int i){
        ArrayList<ColEntry> temp = (ArrayList<ColEntry>) col.clone();
        temp.remove(i);
        return temp;
    }

    public String getName(int i){
        return col.get(i).name();
    }

    public String getType(int i){
        return col.get(i).type();
    }

    public ColEntry getEntry(int i){
        return col.get(i);
    }

    public ArrayList<ColEntry> clone(){
        ArrayList<ColEntry> temp = (ArrayList<ColEntry>) col.clone();
        return temp;
    }

    public void printcol(){
        for(int i = 0; i < length; i += 1){
            System.out.print(col.get(i).name() + " " + col.get(i).type());
            if (i != length - 1)
                System.out.print(",");
        }
        System.out.println();
    }

    public static class test{

        @Test
        public void test(){
            ColEntry a = new ColEntry("x", "int");
            ColEntry b = new ColEntry("x", "int");
            ColEntry c = new ColEntry("y", "double");
            ArrayList<ColEntry> d = new ArrayList<>();
            d.add(a);
            d.add(b);
            d.add(c);
            d.remove(1);
            column e = new column();
            e.addList(d);
            assertEquals(c, e.getEntry(1));
            assertEquals(a, e.getEntry(0));
            assertEquals("x", e.getName(0));
            e.printcol();
            e.deleteItem(0);
            assertEquals(c, e.getEntry(0));
        }
    }
}
