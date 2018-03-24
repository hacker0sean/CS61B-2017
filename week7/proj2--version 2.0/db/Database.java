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
    public static Map<String, Table> table;


    public Database() {
        table = new HashMap<>();
    }

    public static void load(String name, Table t) {
        table.put(name, t);
    }

    public static boolean contain(String name) {
        if (table.containsKey(name))
            return true;
        return false;
    }

    public String transact(String query) {
        return Parse.eval(query);
    }
}

