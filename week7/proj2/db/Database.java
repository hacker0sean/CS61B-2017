package db;

import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import edu.princeton.cs.introcs.In;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Database {
    private Map<String, Table> table;


    public Database() {
        table = new HashMap<>();
    }

    public void load(String name, Table t) {
        table.put(name, t);
    }

    private boolean contain(String name) {
        if (table.containsKey(name))
            return true;
        return false;
    }



    public String transact(String query) {
        return eval(query);
    }

    /**Parse*/
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


    private String eval(String query) {
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

    private String createTable(String expr) {
        try {
            Matcher m;
            if ((m = CREATE_NEW.matcher(expr)).matches()) {
                return createNewTable(m.group(1), m.group(2).split(COMMA));
            } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
                return createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
            } else {
                return "ERROR: Malformed create: " + expr;
            }
        }catch(Exception e){
            return "ERROR: Malformed create: " + expr;
        }
    }

    private String createNewTable(String name, String[] cols) {
        StringJoiner joiner = new StringJoiner(", ");
        for (int i = 0; i < cols.length - 1; i++) {
            joiner.add(cols[i]);
        }
        try{
            if (contain(name))
                throw new RuntimeException("ERROR: Create a table that already exists.!");
        }catch(RuntimeException e){
            return e.getMessage();
        }
        column a = new column(cols);
        Table t = new Table(name, a);
        load(name ,t);
        String colSentence = joiner.toString() + " and " + cols[cols.length - 1];
        return "";
    }

    private String createSelectedTable(String name, String exprs, String tables, String conds) {
        System.out.printf("You are trying to create a table named %s by selecting these expressions:" +
                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", name, exprs, tables, conds);
        return null;
    }

    private String loadTable(String name) {
        In in;
        try {
            in = new In("db/tbl/" + name + ".tbl");
        } catch (IllegalArgumentException a) {
            return "ERROR: There's no " + name + ".tbl file.";
        }
        Table t = new Table(in, name);
        load(name, t);
        return "";
    }

    private String storeTable(String name) {
        try {
            if (!contain(name))
                throw new RuntimeException("ERROR: There's no " + name + " table in the memory.!");
        }catch(RuntimeException e){
            return e.getMessage();
        }

        try {
            FileWriter outFile = new FileWriter("db/tbl/" + name + ".tbl");
            BufferedWriter bWriter = new BufferedWriter(outFile);
            Table x = table.get(name);
            Table.storeTable(x, bWriter);
            bWriter.close();
        }catch(IOException e){
            return "ERROR: IOException!";
        }
        return "";
    }

    private String dropTable(String name) {
        try {
            if (!contain(name))
                throw new RuntimeException("ERROR: There's no " + name + " table in the memory.!");
        }catch(RuntimeException e){
            return e.getMessage();
        }

        table.remove(name);
        return "";
    }

    private String insertRow(String expr) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            return "ERROR: Malformed insert: " + expr;
        }
        String[] data = m.group(2).split(" ");
        String name = m.group(1);
        try {
            if (!contain(name))
                throw new RuntimeException("ERROR: There's no " + name + " table in the memory.!");
        }catch(RuntimeException e){
            return e.getMessage();
        }

        try {
            Table t = table.get(name);
            Table.InsertDataCol(t, data);
        }catch(RuntimeException e){
            return e.getMessage();
        }
        return "";
    }

    private String printTable(String name) {
        try {
            if (!contain(name))
                throw new RuntimeException("ERROR: There's no " + name + " table in the memory.!");
        }catch(RuntimeException e){
            return e.getMessage();
        }

        Table x = table.get(name);
        Table.printTable(x);
        return "";
    }

    private String select(String expr) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            return "ERROR: Malformed select: " + expr;
        }

        return select(m.group(1), m.group(2), m.group(3));
    }

    private String select(String exprs, String tables, String conds) {
        try {
            String[] tablenames = tables.split(",");
            String[] columns = exprs.split(",");
            String[] conditions = null;
            ArrayList<Table> jointable = new ArrayList<>();
            for (String i : tablenames) {
                i = i.trim();
                if (contain(i))
                    jointable.add(table.get(i));
                else
                    throw new RuntimeException("ERROR: There's no " + i + " table in the memory.!");
            }
            if (jointable.size() == 0)
                throw new RuntimeException("ERROR: There's no tables.!");
            Table temp = Table.select(columns, jointable, conditions);
            return "";
        }catch (RuntimeException e){
            return e.getMessage();
        }
    }
}

