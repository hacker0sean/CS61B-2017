package db;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class DataRow {
    ArrayList<DateEntry> col;
    int length;

    public DataRow() {
        length = 0;
        col = new ArrayList<>();
    }

    public DataRow(String DataLine, TypeColumn typecol) throws RuntimeException{
        this();
        String delims = ",";
        String[] DataString = DataLine.split(delims);
        if (DataString.length != typecol.length)
            throw new RuntimeException("Error : Invalid Table!");
        for (int i = 0; i < DataString.length; i += 1) {
            String type = typecol.getEntry(i).type();
            if (type.equals("int"))
                col.add(new IntEntry(DataString[i]));
            else if (type.equals("float"))
                col.add(new FloatEntry(DataString[i]));
            else if (type.equals("string"))
                col.add(new StringEntry(DataString[i]));
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

    public DataRow clone2() {
        ArrayList<DateEntry> temp = (ArrayList<DateEntry>) col.clone();
        DataRow temp2 = new DataRow();
        temp2.addList(temp);
        return temp2;
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
                System.out.print(col.get(i).toString());
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

    public static DataRow DataColgenerator(TypeColumn colm, String[] data) throws RuntimeException{
        DataRow newcol = new DataRow();
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
            DataRow tst = new DataRow();
            tst.addList(test);
            tst.printcol();

        }
    }
}
