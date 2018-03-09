package db;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class datacol {
    ArrayList<DateEntry> col;
    int length;

    public datacol() {
        length = 0;
        col = new ArrayList<>();
    }

    public datacol(String line, column col2) {
        this();
        String delims = ",";
        String[] entrys = line.split(delims);
        for (int i = 0; i < entrys.length; i += 1) {
            String type = col2.getEntry(i).type();
            if (type.equals("int"))
                col.add(new IntEntry(entrys[i]));
            else if (type.equals("float"))
                col.add(new FloatEntry(entrys[i]));
            else if (type.equals("string"))
                col.add(new StringEntry(entrys[i]));
            length += 1;
        }
    }

    public void addItem(DateEntry x) {
        col.add(x);
        length += 1;
    }

    public ArrayList<DateEntry> NDdeleteItem(int x) {
        ArrayList<DateEntry> temp = (ArrayList<DateEntry>) col.clone();
        temp.remove(x);
        return temp;
    }

    public ArrayList<DateEntry> clone() {
        ArrayList<DateEntry> temp = (ArrayList<DateEntry>) col.clone();
        return temp;
    }

    public void deleteItem(int x) {
        col.remove(x);
        length -= 1;
    }

    public void addList(ArrayList<DateEntry> x) {
        for (DateEntry i : x)
            this.addItem(i);
    }

    public DateEntry getEntry(int i) {
        return col.get(i);
    }

    public void printcol() {
        for (int i = 0; i < length; i++) {
            if(col.get(i).NaN)
                System.out.print("NaN");
            else if(col.get(i).NOVALUE)
                System.out.print("NOVALUE");
            else {
                System.out.print(col.get(i).value().toString());
            }
            if (i != length - 1) {
                System.out.print(",");
            }
        }
        System.out.println();
    }

    public void printcol(BufferedWriter x) throws IOException {
        for (int i = 0; i < length; i++) {
            x.write((col.get(i).value().toString()));
            if (i != length - 1) {
                x.write(",");
            }
        }
        x.write("\n");
    }

    public static datacol DataColgenerator(column colm, String[] data) throws RuntimeException{
        datacol newcol = new datacol();
        for (int i = 0; i < colm.length; i++)
        {
            if (colm.getEntry(i).type().equals("int")) {
                newcol.col.add(new IntEntry(data[i]));
            } else if (colm.getEntry(i).type().equals("string")) {
                newcol.col.add(new StringEntry(data[i]));
            } else if (colm.getEntry(i).type().equals("float")) {
                newcol.col.add(new FloatEntry(data[i]));
            }
            newcol.length += 1;
        }
        return newcol;
    }

    public static class test {

        @Test
        public void test() {
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
