package db;

import edu.princeton.cs.introcs.In;;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.StringJoiner;

public class Parse {
    // Various common constructs, simplifies parsing.
    private static final String REST = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND = "\\s+and\\s+";

    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
            LOAD_CMD = Pattern.compile("load " + REST),
            STORE_CMD = Pattern.compile("store " + REST),
            DROP_CMD = Pattern.compile("drop table " + REST),
            INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD = Pattern.compile("print " + REST),
            SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW = Pattern.compile("(\\S+)\\s+\\(\\s*(\\S+\\s+\\S+\\s*" +
            "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
                    "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
                    "([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+" +
                    "[\\w\\s+\\-*/'<>=!.]+?)*))?"),
            CREATE_SEL = Pattern.compile("(\\S+)\\s+as select\\s+" +
                    SELECT_CLS.pattern()),
            INSERT_CLS = Pattern.compile("(\\S+)\\s+values\\s+(.+?" +
                    "\\s*(?:,\\s*.+?\\s*)*)");


    static String eval(String query) {
        Matcher m;
        if ((m = CREATE_CMD.matcher(query)).matches()) {
            return createTable(m.group(1));
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
            return loadTable(m.group(1));
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
            return storeTable(m.group(1));
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
            return dropTable(m.group(1));
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
            return insertRow(m.group(1));
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
            return printTable(m.group(1));
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
            return select(m.group(1));
        } else {
            return "ERROR: Malformed query: " + query;
        }
    }

    private static String createTable(String expr) {
        try {
            Matcher m;
            if ((m = CREATE_NEW.matcher(expr)).matches()) {
                return createNewTable(m.group(1), m.group(2).split(COMMA));
            } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
                return createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
            } else {
                return "ERROR: Malformed create: " + expr;
            }
        } catch (Exception e) {
            return "ERROR: Malformed create: " + expr;
        }
    }

    private static String createNewTable(String name, String[] typecols) {
        StringJoiner joiner = new StringJoiner(", ");
        for (int i = 0; i < typecols.length - 1; i++) {
            joiner.add(typecols[i]);
        }
        try {
            if (Database.contain(name))
                throw new RuntimeException("ERROR: Table Already exist!");

            Table table = new Table(name, typecols);
            Database.load(name, table);
            return "";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private static String createSelectedTable(String name, String exprs, String tables, String conds) {
        try {
            Table temp = select2(exprs, tables, conds);
            temp.changeTableName(name);
            Database.load(name, temp);
            return "";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private static Table select2(String exprs, String tables, String conds) throws RuntimeException {
        String[] tablenames = tables.split(",");
        String[] columns = exprs.split(",");
        String[] conditions = null;
        if (conds != null) {
            conditions = conds.split("and");
        }
        ArrayList<Table> jointable = new ArrayList<>();
        for (String i : tablenames) {
            i = i.trim();
            if (Database.contain(i))
                jointable.add(Database.table.get(i));
            else
                throw new RuntimeException("ERROR: There's no " + i + " Database.table in the memory.!");
        }
        if (jointable.size() == 0)
            throw new RuntimeException("ERROR: There's no tables.!");
        Table temp = Table.select(columns, jointable, conditions);
        return temp;
    }

    private static String loadTable(String name) {
        In in;
        try {
            in = new In("db/tbl/" + name + ".tbl");
            Table t = new Table(in, name);
            Database.load(name, t);
            return "";
        } catch (IllegalArgumentException a) {
            return "ERROR: There's no " + name + ".tbl file.";
        } catch (RuntimeException a){
            return a.getMessage();
        }
    }

    private static String storeTable(String name) {
        try {
            if (!Database.contain(name))
                throw new RuntimeException("ERROR: There's no " + name + " Database.table in the memory.!");
            FileWriter outFile = new FileWriter("db/tbl/" + name + ".tbl");
            BufferedWriter bWriter = new BufferedWriter(outFile);
            Table x = Database.table.get(name);
            Table.storeTable(x, bWriter);
            bWriter.close();
            return "";
        }
         catch (RuntimeException e) {
            return e.getMessage();
        }catch (IOException e) {
            return "ERROR: IOException!";
        }
    }

    private static String dropTable(String name) {
        try {
            if (!Database.contain(name))
                throw new RuntimeException("ERROR: There's no " + name + " Database.table in the memory.!");
            Database.table.remove(name);
            return "";
        }catch (RuntimeException e) {
                return e.getMessage();
        }
    }

    private static String insertRow(String expr) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            return "ERROR: Malformed insert: " + expr;
        }
        String[] data = m.group(2).split(",");
        String name = m.group(1);
        try {
            if (!Database.contain(name))
                throw new RuntimeException("ERROR: There's no " + name + " Database.table in the memory.!");
            Table table = Database.table.get(name);
            Table.InsertDataRow(table, data);
        return "";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private static String printTable(String name) {
        try {
            if (!Database.contain(name))
                throw new RuntimeException("ERROR: There's no " + name + " table in the memory.!");
        Table x = Database.table.get(name);
        Table.printTable(x);
        return "";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private static String select(String expr) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            return "ERROR: Malformed select: " + expr;
        }

        return select(m.group(1), m.group(2), m.group(3));
    }

    private static String select(String exprs, String tables, String conds) {
        try {
            String[] tablenames = tables.split(",");
            String[] columns = exprs.split(",");
            String[] conditions = null;
            if (conds != null) {
                conditions = conds.split("and");
            }
            ArrayList<Table> jointable = new ArrayList<>();
            for (String i : tablenames) {
                i = i.trim();
                if (Database.contain(i))
                    jointable.add(Database.table.get(i));
                else
                    throw new RuntimeException("ERROR: There's no " + i + " table in the memory.!");
            }
            if (jointable.size() == 0)
                throw new RuntimeException("ERROR: There's no tables.!");
            Table temp = Table.select(columns, jointable, conditions);
            Table.printTable(temp);
            return "";
        } catch (RuntimeException e) {
            return e.getMessage();
        } catch (Exception e){
            return e.getMessage();
        }
    }
}

