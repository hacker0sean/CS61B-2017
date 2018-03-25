package db;

import edu.princeton.cs.introcs.In;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Table {
    private String title;
    private TypeColumn typecol;
    public ArrayList<DataRow> row;
    private int typelength;
    public int datalength;
    public Map<ColEntry, ArrayList<DateEntry>> DataColumn;

    /**
     * Constructor
     */
    public Table(String name, String[] cols) {
        this(name);
        typecol = new TypeColumn(cols);
        typelength = typecol.length;
    }

    public Table(String ttl) {
        title = ttl;
        typelength = 0;
        datalength = 0;
        row = new ArrayList<>();
        typecol = new TypeColumn();
    }

    public Table(String ttl, TypeColumn b) {
        title = ttl;
        typecol = b;
        typelength = b.length;
        datalength = 0;
        row = new ArrayList<>();
    }

    public Table(In in, String title) throws RuntimeException {
        this(title);
        String typecolString = in.readLine();
        typecol = new TypeColumn(typecolString);
        typelength = typecol.length;
        while (!in.isEmpty()) {
            String dataRowString = in.readLine();
            DataRow newrow = new DataRow(dataRowString, typecol);
            row.add(newrow);
            datalength += 1;
        }
    }

    public static void AddDateCol(Table a, DataRow b) {
        a.row.add(b);
        a.datalength += 1;
    }

    public static DataRow GetDateCol(Table a, int i) throws RuntimeException {
        return a.row.get(i);
    }

    public void changeTableName(String name) {
        this.title = name;
    }

    public static void InsertDataRow(Table table, String[] data) throws RuntimeException {
        if (data.length != table.typelength)
            throw new RuntimeException("ERROR: The number of variables does not match.");
        table.row.add(DataRow.DataColgenerator(table.typecol, data));
        table.datalength += 1;
    }

    public void generateDataCol() {
        DataColumn = new HashMap<>();
        ArrayList<DateEntry> arrayrow;
        for (int m = 0; m < typelength; m += 1) {
            arrayrow = new ArrayList<DateEntry>();
            for (int i = 0; i < datalength; i += 1) {
                for (int j = 0; j < typelength; j += 1) {
                    if (m == j) {
                        arrayrow.add(row.get(i).getEntry(j));
                    }
                }
            }
            ColEntry x = typecol.getEntry(m);
            DataColumn.put(x, arrayrow);
        }
    }

    public static void printTable(Table table) {
        table.typecol.printcol();
        for (int i = 0; i < table.datalength; i += 1)
            table.row.get(i).printcol();
    }

    public static void storeTable(Table table, BufferedWriter x) throws IOException {
        table.typecol.printcol(x);
        for (DataRow row : table.row)
            row.printcol(x);
    }

    private static Table joinTable(ArrayList<Table> tables) throws RuntimeException {
        Table tableAfterJoin;
        if (tables.size() > 1)
            tableAfterJoin = join(tables);
        else {
            tableAfterJoin = tables.get(0).clone();
        }
        return tableAfterJoin;
    }

    protected Table clone() {
        Table temp = new Table("temp");
        temp.datalength = this.datalength;
        temp.typelength = this.typelength;
        for (DataRow dataRow : this.row) {
            temp.row.add(dataRow.clone2());
        }
        temp.typecol = this.typecol.clone2();
        return temp;
    }

    private static Table merge1(Table a, Table b, int x, int y) {
        TypeColumn newcol = new TypeColumn();
        newcol.addItem(a.typecol.getEntry(x));
        newcol.addList(a.typecol.NDdeleteItem(x));
        newcol.addList(b.typecol.NDdeleteItem(y));
        Table temp = new Table("temp", newcol);
        ArrayList<Integer> map1 = new ArrayList<>();
        ArrayList<Integer> map2 = new ArrayList<>();
        for (int i = 0; i < a.datalength; i += 1) {
            for (int j = 0; j < b.datalength; j += 1) {
                if (a.row.get(i).getEntry(x).equals(b.row.get(j).getEntry(y)))
                    map1.add(i);
                map2.add(j);
            }
        }
        for (int i = 0; i < map1.size(); i += 1) {
            DataRow data = new DataRow();
            data.addItem(a.row.get((int) map1.get(i)).getEntry(x));
            data.addList(a.row.get((int) map1.get(i)).NDdeleteItem(x));
            data.addList(b.row.get((int) map2.get(i)).NDdeleteItem(y));
            AddDateCol(temp, data);
        }
        return temp;
    }

    private static Table merge2(Table a, Table b) {
        TypeColumn newcol = new TypeColumn();
        newcol.addList(a.typecol.clone());
        newcol.addList(b.typecol.clone());
        Table temp = new Table("temp", newcol);
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < a.datalength; i += 1) {
            for (int j = 0; j < b.datalength; j += 1) {
                DataRow data = new DataRow();
                data.addList(a.row.get((int) i).clone());
                data.addList(b.row.get((int) j).clone());
                AddDateCol(temp, data);
            }
        }
        return temp;
    }

    private static Table join(Table a, Table b) {
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

    private static Table join(ArrayList<Table> tables) {
        while (tables.size() != 1) {
            tables.set(0, join(tables.get(0), tables.get(1)));
            tables.remove(1);
        }
        return tables.get(0);
    }

    private static Table ColumnExpressionPrep(Table tableAfterJoin, String[] ColumnExpression) throws RuntimeException {
        if (ColumnExpression.length == 0)
            throw new RuntimeException("Error: Empty TypeColumn expressions.!");
        Table tableAfterColumnExpression;
        if (ColumnExpression[0].equals("*")) {
            tableAfterColumnExpression = tableAfterJoin;
            return tableAfterColumnExpression;
        }
        tableAfterJoin.generateDataCol();
        tableAfterColumnExpression = new Table("tableAfterColumn");
        for (int i = 0; i < tableAfterJoin.datalength; i += 1) {
            tableAfterColumnExpression.row.add(new DataRow());
        }
        tableAfterColumnExpression.datalength = tableAfterJoin.datalength;
        return ColumnExpressionExcu(tableAfterColumnExpression, ColumnExpression, tableAfterJoin);
    }

    private static Table ColumnExpressionExcu(Table tableAfterColumnExpression, String[] ColumnExpression, Table tableAfterJoin) throws RuntimeException {
        for (String i : ColumnExpression) {
            i = i.trim();
            if (i.matches("\\w*")) {
                i = i.trim();
                if (tableAfterJoin.typecol.contain(i)) {
                    ColEntry x = tableAfterJoin.typecol.getEntryByName(i);
                    tableAfterColumnExpression.typecol.addItem(x);
                    tableAfterColumnExpression.typelength += 1;
                    ArrayList<DateEntry> entrys = tableAfterJoin.DataColumn.get(x);
                    for (int j = 0; j < entrys.size(); j += 1) {
                        tableAfterColumnExpression.row.get(j).addItem(entrys.get(j));
                    }
                } else {
                    throw new RuntimeException("Error: Can't find the TypeColumn name");
                }
            } else if (i.matches("\\w+\\s*[+*/-]\\s*'?\\w*.?\\w*'?\\s+as\\s+\\w+")) {
                String[] ope = i.split("[+*/-]");
                String operand1 = ope[0].trim();
                if (!tableAfterJoin.typecol.contain(operand1)) {
                    throw new RuntimeException("Error: Can't find the TypeColumn name");
                }
                ColEntry ope1 = tableAfterJoin.typecol.getEntryByName(operand1);
                ope[1] = ope[1].trim();
                char operator = '1';
                for (int p = 0; p < i.length(); p += 1) {
                    char m = i.charAt(p);
                    if (m == '+' || m == '-' || m == '*' || m == '/')
                        operator = m;
                }
                String[] opes = ope[1].split("\\s+as\\s+");
                String operand2 = opes[0].trim();
                String name = opes[1].trim();
                if (tableAfterJoin.typecol.contain(operand2)) {
                    ColEntry ope2 = tableAfterJoin.typecol.getEntryByName(operand2);
                    tableAfterColumnExpression.typelength += 1;
                    ArrayList<DateEntry> entry1s = tableAfterJoin.DataColumn.get(ope1);
                    ArrayList<DateEntry> entry2s = tableAfterJoin.DataColumn.get(ope2);
                    if (ope1.type().equals("string")) {
                        if (ope2.type().equals("string") && (operator == '+')) {
                            tableAfterColumnExpression.typecol.addItem(new ColEntry(name, "string"));
                            for (int h = 0; h < entry1s.size(); h += 1) {
                                StringEntry entry1 = (StringEntry) (entry1s.get(h));
                                StringEntry entry2 = (StringEntry) (entry2s.get(h));
                                tableAfterColumnExpression.row.get(h).addItem(StringEntry.String_plus_String(entry1, entry2));
                            }
                        } else
                            throw new RuntimeException("Type Dismatch!");
                    } else if (ope1.type().equals("int") && ope2.type().equals("int")) {
                        tableAfterColumnExpression.typecol.addItem(new ColEntry(name, "int"));
                        for (int h = 0; h < entry1s.size(); h += 1) {
                            IntEntry entry1 = (IntEntry) (entry1s.get(h));
                            IntEntry entry2 = (IntEntry) (entry2s.get(h));
                            tableAfterColumnExpression.row.get(h).addItem(IntEntry.Int_operator_Int(entry1, entry2, operator));
                        }
                    } else {
                        tableAfterColumnExpression.typecol.addItem(new ColEntry(name, "float"));
                        for (int h = 0; h < entry1s.size(); h += 1) {
                            if (entry1s.get(h).type().equals("int"))
                                tableAfterColumnExpression.row.get(h).addItem(FloatEntry.Float_operator_Float((IntEntry) (entry1s.get(h)), (FloatEntry) (entry2s.get(h)), operator));
                            else if (entry2s.get(h).type().equals("int"))
                                tableAfterColumnExpression.row.get(h).addItem(FloatEntry.Float_operator_Float((FloatEntry) (entry1s.get(h)), (IntEntry) (entry2s.get(h)), operator));
                            else {
                                FloatEntry entry1 = (FloatEntry) (entry1s.get(h));
                                FloatEntry entry2 = (FloatEntry) (entry2s.get(h));
                                tableAfterColumnExpression.row.get(h).addItem(FloatEntry.Float_operator_Float(entry1, entry2, operator));
                            }
                        }
                    }
                } else {
                    tableAfterColumnExpression.typelength += 1;
                    ArrayList<DateEntry> entry1s = tableAfterJoin.DataColumn.get(ope1);
                    if (ope1.type().equals("string")) {
                        if (operator == '+') {
                            tableAfterColumnExpression.typecol.addItem(new ColEntry(name, "string"));
                            for (int h = 0; h < entry1s.size(); h += 1) {
                                StringEntry entry1 = ((StringEntry) (entry1s.get(h)));
                                tableAfterColumnExpression.row.get(h).addItem(StringEntry.String_plus_Literal(entry1, operand2));
                            }
                        } else
                            throw new RuntimeException("Error : Pattern dismatch!");
                    } else if (ope1.type().equals("int")) {
                        if (operand2.matches("\\d*\\.\\d+")) {
                            tableAfterColumnExpression.typecol.addItem(new ColEntry(name, "float"));
                            for (int h = 0; h < entry1s.size(); h += 1) {
                                IntEntry entry1 = (IntEntry) (entry1s.get(h));
                                tableAfterColumnExpression.row.get(h).addItem(FloatEntry.Int_operator_FloatLiteral(entry1, operand2, operator));
                            }
                        } else {
                            tableAfterColumnExpression.typecol.addItem(new ColEntry(name, "int"));
                            for (int h = 0; h < entry1s.size(); h += 1) {
                                IntEntry entry1 = (IntEntry) (entry1s.get(h));
                                tableAfterColumnExpression.row.get(h).addItem(IntEntry.Int_operator_Literal(entry1, operand2, operator));
                            }
                        }
                    } else if (ope1.type().equals("float")) {
                        tableAfterColumnExpression.typecol.addItem(new ColEntry(name, "float"));
                        for (int h = 0; h < entry1s.size(); h += 1) {
                            FloatEntry entry1 = (FloatEntry) (entry1s.get(h));
                            tableAfterColumnExpression.row.get(h).addItem(FloatEntry.Float_operator_Literal(entry1, operand2, operator));
                        }
                    }
                }
            } else {
                throw new RuntimeException("Error : Pattern dismatch!");
            }
        }
        return tableAfterColumnExpression;
    }

    private static Table ConditionPrep(Table tableAfterColumnExpression, String[] conds){
        if (conds == null)
            return tableAfterColumnExpression;

        TypeColumn typeColumn = tableAfterColumnExpression.typecol;
        ArrayList<Condition> condis = new ArrayList<>();
        for (String x : conds) {
            x = x.trim();
            condis.add(new Condition(x, typeColumn));
        }
        for (Condition con : condis) {
            con.obeysCondition(tableAfterColumnExpression, typeColumn);
        }
        return tableAfterColumnExpression;
    }

    public static Table select(String[] exprs, ArrayList<Table> tables, String[] conds) throws RuntimeException {
        Table tableAfterJoin = joinTable(tables);
        Table tableAfterColumnExpression = ColumnExpressionPrep(tableAfterJoin, exprs);
        Table tableAfterCondition = ConditionPrep(tableAfterColumnExpression, conds);
        return tableAfterCondition;
    }
}

