package db;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Test;
import edu.princeton.cs.introcs.In;

public class Table {
    String title;
    column typecol;
    ArrayList<datacol> datcol;
    int typelength;
    int datalength;
    Map<ColEntry, ArrayList<DateEntry>> datrow;

    /**
     * Constructor
     */
    public Table(String ttl) {
        title = ttl;
        typelength = 0;
        datalength = 0;
        datcol = new ArrayList<>();
        typecol = new column();
    }

    public Table(String ttl, column b) {
        title = ttl;
        typecol = b;
        typelength = b.length;
        datalength = 0;
        datcol = new ArrayList<>();
    }

    public Table(In in, String title) {
        this(title);
        String colline = in.readLine();
        typecol = new column(colline);
        typelength = typecol.length;
        while (true) {
            try {
                String dataline = in.readLine();
                datacol newcol = new datacol(dataline, typecol);
                datcol.add(newcol);
                datalength += 1;
            } catch (Exception e) {
                break;
            }
        }
    }

    /**
     * Method
     */
    public void generateDataRow() {
        datrow = new HashMap<>();
        ArrayList<DateEntry> arrayrow = null;
        for (int m = 0; m < typelength; m += 1) {
            arrayrow = new ArrayList<DateEntry>();
            for (int i = 0; i < datalength; i += 1) {
                for (int j = 0; j < typelength; j += 1) {
                    if (m == j) {
                        arrayrow.add(datcol.get(i).getEntry(j));
                    }
                }
            }
            ColEntry x = typecol.getEntry(m);
            datrow.put(x, arrayrow);
        }
    }

    public static void AddDateCol(Table a, datacol b) {
        a.datcol.add(b);
        a.datalength += 1;
    }

    public static datacol GetDateCol(Table a, int i) throws RuntimeException {
        return a.datcol.get(i);
    }

    public static void InsertDataCol(Table a, String[] data) {
        if (data.length != a.typelength)
            throw new RuntimeException("ERROR: The number of variables does not match.");
        try {
            a.datcol.add(datacol.DataColgenerator(a.typecol, data));
            a.datalength += 1;
        } catch (RuntimeException e) {
            throw new RuntimeException("ERROR: Type dismatch!");
        }
    }

    public static Table merge1(Table a, Table b, int x, int y) {
        column newcol = new column();
        newcol.addItem(a.typecol.getEntry(x));
        newcol.addList(a.typecol.NDdeleteItem(x));
        newcol.addList(b.typecol.NDdeleteItem(y));
        Table temp = new Table("temp", newcol);
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < a.datalength; i += 1) {
            for (int j = 0; j < b.datalength; j += 1) {
                if (a.datcol.get(i).getEntry(x).equals(b.datcol.get(j).getEntry(y)))
                    map.put(i, j);
            }
        }
        for (Integer i : map.keySet()) {
            datacol data = new datacol();
            data.addItem(a.datcol.get((int) i).getEntry(x));
            data.addList(a.datcol.get((int) i).NDdeleteItem(x));
            data.addList(b.datcol.get((int) map.get(i)).NDdeleteItem(y));
            AddDateCol(temp, data);
        }
        return temp;
    }


    public static Table merge2(Table a, Table b) {
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

    public static Table join(Table a, Table b) {
        boolean exist = false;
        int x = 0, y = 0;
        for (int i = 0; i < a.typelength; i += 1)
            for (int j = 0; j < b.typelength; j += 1)
                if (a.typecol.getEntry(i).equals(b.typecol.getEntry(j))) {
                    exist = true;
                    x = i;
                    y = j;
                    break;
                }
        if (exist)
            return merge1(a, b, x, y);
        else
            return merge2(a, b);
    }

    public static Table join(ArrayList<Table> tables) {
        while (tables.size() != 1) {
            tables.set(0, join(tables.get(0), tables.get(1)));
            tables.remove(1);
        }
        return tables.get(0);
    }

    public static void printTable(Table a) {
        a.typecol.printcol();
        for (int i = 0; i < a.datcol.size(); i += 1)
            a.datcol.get(i).printcol();
    }

    public static void storeTable(Table a, BufferedWriter x) throws IOException {
        a.typecol.printcol(x);
        for (datacol m : a.datcol)
            m.printcol(x);
    }

    public static Table select(String[] exprs, ArrayList<Table> tables, String[] conds) throws RuntimeException {
        Table tableAfterJoin = tables.get(0);
        if (tables.size() > 1) {
            tableAfterJoin = join(tables);
        }
        if (exprs.length == 0)
            throw new RuntimeException("Error: Empty column expressions.!");
        Table tableAftercolumn;
        tableAfterJoin.generateDataRow();
        if (exprs[0].equals("*")) {
            tableAftercolumn = tableAfterJoin;
        } else {
            String[] operand;
            tableAftercolumn = new Table("tableAfterColumn");
            for (int i = 0; i < tableAfterJoin.datalength; i += 1) {
                tableAftercolumn.datcol.add(new datacol());
            }
            tableAftercolumn.datalength = tableAfterJoin.datalength;
            for (String i : exprs) {
                i = i.trim();
                if (i.matches("\\w*")) {
                    i = i.trim();
                    if (tableAfterJoin.typecol.contain(i)) {
                        ColEntry x = tableAfterJoin.typecol.getEntryByName(i);
                        tableAftercolumn.typecol.addItem(x);
                        tableAftercolumn.typelength += 1;
                        ArrayList<DateEntry> entrys = tableAfterJoin.datrow.get(x);
                        for (int j = 0; j < entrys.size(); j += 1) {
                            tableAftercolumn.datcol.get(j).addItem(entrys.get(j));
                        }
                    } else {
                        throw new RuntimeException("Error: Can't find the column name");
                    }
                } else if (i.matches("\\w+\\s*[+*/-]\\s*\\w+\\.*\\w*\\s*as\\s*\\w+")) {
                    String[] ope = i.split("[+*/-]");
                    String operand1 = ope[0].trim();
                    ope[1] = ope[1].trim();
                    char operator = '1';
                    for (int p = 0; p < i.length(); p += 1) {
                        char m = i.charAt(p);
                        if (m == '+' || m == '-' || m == '*' || m == '/')
                            operator = m;
                    }
                    ope = ope[1].split("\\s+as\\s+");
                    String operand2 = ope[0].trim();
                    String name = ope[1].trim();
                    if (tableAfterJoin.typecol.contain(operand1)) {
                        ColEntry ope1 = tableAfterJoin.typecol.getEntryByName(operand1);
                        if (tableAfterJoin.typecol.contain(operand2)) {
                            ColEntry ope2 = tableAfterJoin.typecol.getEntryByName(operand2);
                            if (ope1.type().equals("string")) {
                                if (ope2.type().equals("string") && (operator == '+')) {
                                    tableAftercolumn.typecol.addItem(new ColEntry(name, "string"));
                                    tableAftercolumn.typelength += 1;
                                    ArrayList<DateEntry> entry1 = tableAfterJoin.datrow.get(ope1);
                                    ArrayList<DateEntry> entry2 = tableAfterJoin.datrow.get(ope2);
                                    for (int h = 0; h < entry1.size(); h += 1) {
                                        StringEntry entrynew;
                                        String x1 = ((StringEntry) (entry1.get(h))).value();
                                        String x2 = ((StringEntry) (entry2.get(h))).value();
                                        entrynew = new StringEntry(x1.substring(0, x1.length() - 1).concat(x2.substring(1)));
                                        tableAftercolumn.datcol.get(h).addItem(entrynew);
                                    }
                                } else
                                    throw new RuntimeException("Type Dismatch!");
                            } else if (ope1.type().equals("int") && ope2.type().equals("int")) {
                                if (operator != '/') {
                                    tableAftercolumn.typecol.addItem(new ColEntry(name, "int"));
                                    tableAftercolumn.typelength += 1;
                                    ArrayList<DateEntry> entrys1 = tableAfterJoin.datrow.get(ope1);
                                    ArrayList<DateEntry> entrys2 = tableAfterJoin.datrow.get(ope2);
                                    for (int h = 0; h < entrys1.size(); h += 1) {
                                        IntEntry entrynew = null;
                                        IntEntry entry1 = (IntEntry) (entrys1.get(h));
                                        IntEntry entry2 = (IntEntry) (entrys2.get(h));
                                        if (entry1.NaN || entry2.NaN)
                                            entrynew = new IntEntry();
                                        else {
                                            int x1 = ((IntEntry) (entrys1.get(h))).realvalue;
                                            int x2 = ((IntEntry) (entrys2.get(h))).realvalue;
                                            if (operator == '+') {
                                                entrynew = new IntEntry(((Integer) (x1 + x2)).toString());
                                            } else if (operator == '-') {
                                                entrynew = new IntEntry(((Integer) (x1 - x2)).toString());
                                            } else if (operator == '*') {
                                                entrynew = new IntEntry(((Integer) (x1 * x2)).toString());
                                            }
                                        }
                                        tableAftercolumn.datcol.get(h).addItem(entrynew);
                                    }
                                } else if (operator == '/') {
                                    tableAftercolumn.typecol.addItem(new ColEntry(name, "float"));
                                    tableAftercolumn.typelength += 1;
                                    ArrayList<DateEntry> entrys1 = tableAfterJoin.datrow.get(ope1);
                                    ArrayList<DateEntry> entrys2 = tableAfterJoin.datrow.get(ope2);
                                    for (int h = 0; h < entrys1.size(); h += 1) {
                                        FloatEntry entrynew = null;
                                        IntEntry entry1 = (IntEntry) (entrys1.get(h));
                                        IntEntry entry2 = (IntEntry) (entrys2.get(h));
                                        if (entry1.NaN || entry2.NaN)
                                            entrynew = new FloatEntry();
                                        else {
                                            int x1 = ((IntEntry) (entrys1.get(h))).realvalue;
                                            int x2 = ((IntEntry) (entrys2.get(h))).realvalue;
                                            if (x2 == 0)
                                                entrynew = new FloatEntry();
                                            else
                                                entrynew = new FloatEntry(((Float) (float) (x1 * 1.0 / x2)).toString());
                                        }
                                        tableAftercolumn.datcol.get(h).addItem(entrynew);
                                    }
                                }
                            } else if (ope1.type().equals("int") && ope2.type().equals("float")) {
                                tableAftercolumn.typecol.addItem(new ColEntry(name, "float"));
                                tableAftercolumn.typelength += 1;
                                ArrayList<DateEntry> entrys1 = tableAfterJoin.datrow.get(ope1);
                                ArrayList<DateEntry> entrys2 = tableAfterJoin.datrow.get(ope2);
                                for (int h = 0; h < entrys1.size(); h += 1) {
                                    FloatEntry entrynew = null;
                                    IntEntry entry1 = (IntEntry) (entrys1.get(h));
                                    FloatEntry entry2 = (FloatEntry) (entrys2.get(h));
                                    if (entry1.NaN || entry2.NaN)
                                        entrynew = new FloatEntry();
                                    else {
                                        int x1 = ((IntEntry) (entrys1.get(h))).realvalue;
                                        float x2 = ((FloatEntry) (entrys2.get(h))).realvalue;
                                        if (operator == '/') {
                                            if (x2 == 0.0)
                                                entrynew = new FloatEntry();
                                            else
                                                entrynew = new FloatEntry(((Float) (float) (x1 * 1.0 / x2)).toString());
                                        } else if (operator == '+') {
                                            entrynew = new FloatEntry(((Float) (float) (x1 + x2)).toString());
                                        } else if (operator == '-') {
                                            entrynew = new FloatEntry(((Float) (float) (x1 - x2)).toString());
                                        } else if (operator == '*') {
                                            entrynew = new FloatEntry(((Float) (float) (x1 * x2)).toString());
                                        }
                                    }
                                    tableAftercolumn.datcol.get(h).addItem(entrynew);
                                }
                            } else if (ope1.type().equals("float") && ope2.type().equals("int")) {
                                tableAftercolumn.typecol.addItem(new ColEntry(name, "float"));
                                tableAftercolumn.typelength += 1;
                                ArrayList<DateEntry> entrys1 = tableAfterJoin.datrow.get(ope1);
                                ArrayList<DateEntry> entrys2 = tableAfterJoin.datrow.get(ope2);
                                for (int h = 0; h < entrys1.size(); h += 1) {
                                    FloatEntry entrynew = null;
                                    FloatEntry entry1 = (FloatEntry) (entrys1.get(h));
                                    IntEntry entry2 = (IntEntry) (entrys2.get(h));
                                    if (entry1.NaN || entry2.NaN)
                                        entrynew = new FloatEntry();
                                    else {
                                        float x1 = ((FloatEntry) (entrys1.get(h))).realvalue;
                                        int x2 = ((IntEntry) (entrys2.get(h))).realvalue;
                                        if (operator == '/') {
                                            if (x2 == 0)
                                                entrynew = new FloatEntry();
                                            else
                                                entrynew = new FloatEntry(((Float) (float) (x1 / x2)).toString());
                                        } else if (operator == '+') {
                                            entrynew = new FloatEntry(((Float) (float) (x1 + x2)).toString());
                                        } else if (operator == '-') {
                                            entrynew = new FloatEntry(((Float) (float) (x1 - x2)).toString());
                                        } else if (operator == '*') {
                                            entrynew = new FloatEntry(((Float) (float) (x1 * x2)).toString());
                                        }
                                    }
                                    tableAftercolumn.datcol.get(h).addItem(entrynew);
                                }
                            } else if (ope1.type().equals("float") && ope2.type().equals("float")) {
                                tableAftercolumn.typecol.addItem(new ColEntry(name, "float"));
                                tableAftercolumn.typelength += 1;
                                ArrayList<DateEntry> entrys1 = tableAfterJoin.datrow.get(ope1);
                                ArrayList<DateEntry> entrys2 = tableAfterJoin.datrow.get(ope2);
                                for (int h = 0; h < entrys1.size(); h += 1) {
                                    FloatEntry entrynew = null;
                                    FloatEntry entry1 = (FloatEntry) (entrys1.get(h));
                                    FloatEntry entry2 = (FloatEntry) (entrys2.get(h));
                                    if (entry1.NaN || entry2.NaN)
                                        entrynew = new FloatEntry();
                                    else {
                                        float x1 = ((FloatEntry) (entrys1.get(h))).realvalue;
                                        float x2 = ((FloatEntry) (entrys2.get(h))).realvalue;
                                        if (operator == '/') {
                                            if (x2 == 0.0)
                                                entrynew = new FloatEntry();
                                            else
                                                entrynew = new FloatEntry(((Float) (float) (x1 / x2)).toString());
                                        } else if (operator == '+') {
                                            entrynew = new FloatEntry(((Float) (float) (x1 + x2)).toString());
                                        } else if (operator == '-') {
                                            entrynew = new FloatEntry(((Float) (float) (x1 - x2)).toString());
                                        } else if (operator == '*') {
                                            entrynew = new FloatEntry(((Float) (float) (x1 * x2)).toString());
                                        }
                                    }
                                    tableAftercolumn.datcol.get(h).addItem(entrynew);
                                }
                            }
                        } else {
                            try {
                                if (ope1.type().equals("string")) {
                                    if (operator == '+') {
                                        tableAftercolumn.typecol.addItem(new ColEntry(name, "string"));
                                        tableAftercolumn.typelength += 1;
                                        ArrayList<DateEntry> entry1 = tableAfterJoin.datrow.get(ope1);
                                        for (int h = 0; h < entry1.size(); h += 1) {
                                            StringEntry entrynew;
                                            String x1 = ((StringEntry) (entry1.get(h))).value();
                                            String x2 = operand2;
                                            entrynew = new StringEntry(x1.substring(0, x1.length() - 1).concat(x2));
                                            tableAftercolumn.datcol.get(h).addItem(entrynew);
                                        }
                                    } else
                                        throw new RuntimeException("Pattern dismatch!");
                                } else if (ope1.type().equals("int")) {
                                    if (operand2.matches("\\d*\\.\\d+")) {
                                        float fl = Float.parseFloat(operand2);
                                        tableAftercolumn.typecol.addItem(new ColEntry(name, "float"));
                                        tableAftercolumn.typelength += 1;
                                        ArrayList<DateEntry> entrys1 = tableAfterJoin.datrow.get(ope1);
                                        for (int h = 0; h < entrys1.size(); h += 1) {
                                            FloatEntry entrynew = null;
                                            IntEntry entry1 = (IntEntry) (entrys1.get(h));
                                            if (entry1.NaN)
                                                entrynew = new FloatEntry();
                                            else {
                                                int x1 = ((IntEntry) (entrys1.get(h))).realvalue;
                                                float x2 = fl;
                                                if (operator == '/') {
                                                    if (x2 == 0.0)
                                                        entrynew = new FloatEntry();
                                                    else
                                                        entrynew = new FloatEntry(((Float) (float) (x1 * 1.0 / x2)).toString());
                                                } else if (operator == '+') {
                                                    entrynew = new FloatEntry(((Float) (float) (x1 + x2)).toString());
                                                } else if (operator == '-') {
                                                    entrynew = new FloatEntry(((Float) (float) (x1 - x2)).toString());
                                                } else if (operator == '*') {
                                                    entrynew = new FloatEntry(((Float) (float) (x1 * x2)).toString());
                                                }
                                            }
                                            tableAftercolumn.datcol.get(h).addItem(entrynew);
                                        }
                                    } else {
                                        int x3 = Integer.parseInt(operand2);
                                        if (operator != '/') {
                                            tableAftercolumn.typecol.addItem(new ColEntry(name, "int"));
                                            tableAftercolumn.typelength += 1;
                                            ArrayList<DateEntry> entrys1 = tableAfterJoin.datrow.get(ope1);
                                            for (int h = 0; h < entrys1.size(); h += 1) {
                                                IntEntry entrynew = null;
                                                IntEntry entry1 = (IntEntry) (entrys1.get(h));
                                                if (entry1.NaN)
                                                    entrynew = new IntEntry();
                                                else {
                                                    int x1 = ((IntEntry) (entrys1.get(h))).realvalue;
                                                    int x2 = x3;
                                                    if (operator == '+') {
                                                        entrynew = new IntEntry(((Integer) (x1 + x2)).toString());
                                                    } else if (operator == '-') {
                                                        entrynew = new IntEntry(((Integer) (x1 - x2)).toString());
                                                    } else if (operator == '*') {
                                                        entrynew = new IntEntry(((Integer) (x1 * x2)).toString());
                                                    }
                                                }
                                                tableAftercolumn.datcol.get(h).addItem(entrynew);
                                            }
                                        } else if (operator == '/') {
                                            tableAftercolumn.typecol.addItem(new ColEntry(name, "float"));
                                            tableAftercolumn.typelength += 1;
                                            ArrayList<DateEntry> entrys1 = tableAfterJoin.datrow.get(ope1);
                                            for (int h = 0; h < entrys1.size(); h += 1) {
                                                FloatEntry entrynew = null;
                                                IntEntry entry1 = (IntEntry) (entrys1.get(h));
                                                if (entry1.NaN)
                                                    entrynew = new FloatEntry();
                                                else {
                                                    int x1 = ((IntEntry) (entrys1.get(h))).realvalue;
                                                    int x2 = x3;
                                                    if (x2 == 0)
                                                        entrynew = new FloatEntry();
                                                    else
                                                        entrynew = new FloatEntry(((Float) (float) (x1 * 1.0 / x2)).toString());
                                                }
                                                tableAftercolumn.datcol.get(h).addItem(entrynew);
                                            }
                                        }
                                    }
                                } else if (ope1.type().equals("float")) {
                                    float fl = Float.parseFloat(operand2);
                                    tableAftercolumn.typecol.addItem(new ColEntry(name, "float"));
                                    tableAftercolumn.typelength += 1;
                                    ArrayList<DateEntry> entrys1 = tableAfterJoin.datrow.get(ope1);
                                    for (int h = 0; h < entrys1.size(); h += 1) {
                                        FloatEntry entrynew = null;
                                        FloatEntry entry1 = (FloatEntry) (entrys1.get(h));
                                        if (entry1.NaN)
                                            entrynew = new FloatEntry();
                                        else {
                                            float x1 = ((FloatEntry) (entrys1.get(h))).realvalue;
                                            float x2 = fl;
                                            if (operator == '/') {
                                                if (x2 == 0.0)
                                                    entrynew = new FloatEntry();
                                                else
                                                    entrynew = new FloatEntry(((Float) (float) (x1 / x2)).toString());
                                            } else if (operator == '+') {
                                                entrynew = new FloatEntry(((Float) (float) (x1 + x2)).toString());
                                            } else if (operator == '-') {
                                                entrynew = new FloatEntry(((Float) (float) (x1 - x2)).toString());
                                            } else if (operator == '*') {
                                                entrynew = new FloatEntry(((Float) (float) (x1 * x2)).toString());
                                            }
                                        }
                                        tableAftercolumn.datcol.get(h).addItem(entrynew);
                                    }
                                }
                            } catch (RuntimeException e) {
                                throw new RuntimeException("Error: Pattern dismatch!");
                            }
                        }
                    } else {
                        throw new RuntimeException("Error: Can't find the column name");
                    }
                } else {
                    throw new RuntimeException("Error: Pattern dismatch!");
                }
            }
        }
        printTable(tableAftercolumn);
        return tableAfterJoin;
    }

    public static class test {

        @Test
        public void testMerge1() {
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
        public void testMerge2() {
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

        @Test
        public void arrayRowTest() {
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
            a.generateDataRow();
            b.generateDataRow();
        }
    }
}