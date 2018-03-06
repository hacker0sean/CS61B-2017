package db;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;

public class Table{
    String title;
    column typecol;
    ArrayList<datacol> datcol;
    int typelength;
    int datalength;

    /** Constructor */
    public Table(String ttl){
        title = ttl;
        typelength = 0;
        datalength = 0;
        datcol = new ArrayList<>();
        typecol = new column();
    }

    public Table(String ttl, column b){
        title = ttl;
        typecol = b;
        typelength = b.length;
        datalength = 0;
        datcol = new ArrayList<>();
    }

    /** Method */
    public static void AddDateCol(Table a, datacol b){
        a.datcol.add(b);
        a.datalength  += 1;
    }
    public static datacol GetDateCol(Table a, int i){
        return a.datcol.get(i);
    }

    public static Table merge1(Table a, Table b, int x, int y){
        column newcol = new column();
        newcol.addItem(a.typecol.getEntry(x));
        newcol.addList(a.typecol.NDdeleteItem(x));
        newcol.addList(b.typecol.NDdeleteItem(y));
        Table temp = new Table("temp", newcol);
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < a.datalength; i += 1){
            for (int j = 0; j < b.datalength; j += 1){
                if (a.datcol.get(i).getEntry(x).equals(b.datcol.get(j).getEntry(y)))
                    map.put(i, j);
            }
        }
        for (Integer i : map.keySet()){
            datacol data = new datacol();
            data.addItem(a.datcol.get((int) i).getEntry(x));
            data.addList(a.datcol.get((int) i).NDdeleteItem(x));
            data.addList(b.datcol.get((int) map.get(i)).NDdeleteItem(y));
            AddDateCol(temp, data);
        }
        return temp;
    }


    public static Table merge2(Table a, Table b){
        column newcol = new column();
        newcol.addList(a.typecol.clone());
        newcol.addList(b.typecol.clone());
        Table temp = new Table("temp", newcol);
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < a.datalength; i += 1) {
            for (int j = 0; j < b.datalength; j += 1) {
                datacol data = new datacol();
                data.addList(a.datcol.get((int) i).clone());
                data.addList(b.datcol.get((int) j).clone());
                AddDateCol(temp, data);
            }
        }
        return temp;
    }

    public static Table join(Table a, Table b){
        boolean exist = false;
        int x = 0, y = 0;
        for(int i = 0; i < a.typelength ; i += 1)
            for(int j = 0; j < b.typelength; j += 1)
                if (a.typecol.getEntry(i).equals(b.typecol.getEntry(j))){
                    exist = true;
                    x = i;
                    y = j;
                    break;
        }
        if(exist)
            return merge1(a, b, x, y);
        else
            return merge2(a, b);
    }

    public static void printTable(Table a){
        a.typecol.printcol();
        for (datacol x : a.datcol)
            x.printcol();
        System.out.println();
    }

    public static class test{

        @Test
        public void testMerge1(){
            ColEntry entry1 = new ColEntry("x", "int");
            ColEntry entry2 = new ColEntry("y", "int");
            ColEntry entry3 = new ColEntry("x", "int");
            ColEntry entry4 = new ColEntry("z", "int");
            ArrayList<ColEntry> column1 = new ArrayList<>();
            ArrayList<ColEntry> column2 = new ArrayList<>();
            column1.add(entry1);
            column1.add(entry2);
            column2.add(entry3);
            column2.add(entry4);
            column truecolumn1 = new column();
            column truecolumn2 = new column();
            truecolumn1.addList(column1);
            truecolumn2.addList(column2);
            Table a = new Table("a", truecolumn1);
            Table b = new Table("b", truecolumn2);

            IntEntry dentry1 = new IntEntry("2");
            IntEntry dentry2 = new IntEntry("5");
            IntEntry dentry3 = new IntEntry("8");
            IntEntry dentry4 = new IntEntry("3");
            IntEntry dentry5 = new IntEntry("13");
            IntEntry dentry6 = new IntEntry("7");
            IntEntry dentry7 = new IntEntry("2");
            IntEntry dentry8 = new IntEntry("4");
            IntEntry dentry9 = new IntEntry("8");
            IntEntry dentry10 = new IntEntry("9");
            IntEntry dentry11 = new IntEntry("10");
            IntEntry dentry12 = new IntEntry("1");
            ArrayList<DateEntry> ddentry1 = new ArrayList<>();
            ArrayList<DateEntry> ddentry2 = new ArrayList<>();
            ArrayList<DateEntry> ddentry3 = new ArrayList<>();
            ArrayList<DateEntry> ddentry4 = new ArrayList<>();
            ArrayList<DateEntry> ddentry5 = new ArrayList<>();
            ArrayList<DateEntry> ddentry6 = new ArrayList<>();
            ddentry1.add(dentry1);
            ddentry1.add(dentry2);
            ddentry2.add(dentry3);
            ddentry2.add(dentry4);
            ddentry3.add(dentry5);
            ddentry3.add(dentry6);
            ddentry4.add(dentry7);
            ddentry4.add(dentry8);
            ddentry5.add(dentry9);
            ddentry5.add(dentry10);
            ddentry6.add(dentry11);
            ddentry6.add(dentry12);
            datacol qentry1 = new datacol();
            datacol qentry2 = new datacol();
            datacol qentry3 = new datacol();
            datacol qentry4 = new datacol();
            datacol qentry5 = new datacol();
            datacol qentry6 = new datacol();
            qentry1.addList(ddentry1);
            qentry2.addList(ddentry2);
            qentry3.addList(ddentry3);
            qentry4.addList(ddentry4);
            qentry5.addList(ddentry5);
            qentry6.addList(ddentry6);
            AddDateCol(a, qentry1);
            AddDateCol(a, qentry2);
            AddDateCol(a, qentry3);
            AddDateCol(b, qentry4);
            AddDateCol(b, qentry5);
            AddDateCol(b, qentry6);
            printTable(a);
            printTable(b);
            printTable(join(a, b));
        }

        @Test
        public void testMerge2(){
            ColEntry entry1 = new ColEntry("x", "int");
            ColEntry entry2 = new ColEntry("y", "int");
            ColEntry entry3 = new ColEntry("k", "int");
            ColEntry entry4 = new ColEntry("z", "int");
            ArrayList<ColEntry> column1 = new ArrayList<>();
            ArrayList<ColEntry> column2 = new ArrayList<>();
            column1.add(entry1);
            column1.add(entry2);
            column2.add(entry3);
            column2.add(entry4);
            column truecolumn1 = new column();
            column truecolumn2 = new column();
            truecolumn1.addList(column1);
            truecolumn2.addList(column2);
            Table a = new Table("a", truecolumn1);
            Table b = new Table("b", truecolumn2);

            IntEntry dentry1 = new IntEntry("2");
            IntEntry dentry2 = new IntEntry("5");
            IntEntry dentry3 = new IntEntry("8");
            IntEntry dentry4 = new IntEntry("3");
            IntEntry dentry5 = new IntEntry("13");
            IntEntry dentry6 = new IntEntry("7");
            IntEntry dentry7 = new IntEntry("2");
            IntEntry dentry8 = new IntEntry("4");
            IntEntry dentry9 = new IntEntry("8");
            IntEntry dentry10 = new IntEntry("9");
            IntEntry dentry11 = new IntEntry("10");
            IntEntry dentry12 = new IntEntry("1");
            ArrayList<DateEntry> ddentry1 = new ArrayList<>();
            ArrayList<DateEntry> ddentry2 = new ArrayList<>();
            ArrayList<DateEntry> ddentry3 = new ArrayList<>();
            ArrayList<DateEntry> ddentry4 = new ArrayList<>();
            ArrayList<DateEntry> ddentry5 = new ArrayList<>();
            ArrayList<DateEntry> ddentry6 = new ArrayList<>();
            ddentry1.add(dentry1);
            ddentry1.add(dentry2);
            ddentry2.add(dentry3);
            ddentry2.add(dentry4);
            ddentry3.add(dentry5);
            ddentry3.add(dentry6);
            ddentry4.add(dentry7);
            ddentry4.add(dentry8);
            ddentry5.add(dentry9);
            ddentry5.add(dentry10);
            ddentry6.add(dentry11);
            ddentry6.add(dentry12);
            datacol qentry1 = new datacol();
            datacol qentry2 = new datacol();
            datacol qentry3 = new datacol();
            datacol qentry4 = new datacol();
            datacol qentry5 = new datacol();
            datacol qentry6 = new datacol();
            qentry1.addList(ddentry1);
            qentry2.addList(ddentry2);
            qentry3.addList(ddentry3);
            qentry4.addList(ddentry4);
            qentry5.addList(ddentry5);
            qentry6.addList(ddentry6);
            AddDateCol(a, qentry1);
            AddDateCol(a, qentry2);
            AddDateCol(a, qentry3);
            AddDateCol(b, qentry4);
            AddDateCol(b, qentry5);
            AddDateCol(b, qentry6);
            printTable(a);
            printTable(b);
            printTable(join(a, b));
        }
    }
}