package db;

import java.util.ArrayList;

public class Condition {
    boolean isUnary;
    String column0Name;
    String column1Name;
    Comparison c;

    public Condition(String str, TypeColumn typeColumn) throws RuntimeException {
        String[] strs = str.split("\\s*(?:==|!=|<=|>=|<|>)\\s*");
        column0Name = strs[0].trim();
        column1Name = strs[1].trim();
        if (typeColumn.contain(column0Name)) {
            if (str.contains(">="))
                c = new greaterThanOrEquals();
            else if (str.contains("<="))
                c = new lessThanOrEquals();
            else if (str.contains(">"))
                c = new greaterThan();
            else if (str.contains("<"))
                c = new lessThan();
            else if (str.contains("=="))
                c = new equals();
            else if (str.contains("!="))
                c = new notEquals();
            if (typeColumn.contain(column1Name)) {
                isUnary = false;
            } else {
                isUnary = true;
            }
        } else
            throw new RuntimeException("Error: Can't find the TypeColumn name.!");
    }


    public void obeysCondition(Table t, TypeColumn typecol) throws RuntimeException {
        ColEntry col0 = typecol.getEntryByName(column0Name);
        t.generateDataCol();
        ArrayList<DateEntry> dat0s = t.DataColumn.get(col0);
        if (isUnary) {
            for (int i = 0, k = 0; i < dat0s.size(); i += 1) {
                if (!(c.compare(dat0s.get(i), column1Name))) {
                    t.row.remove(k);
                    t.datalength -= 1;
                } else {
                    k += 1;
                }
            }
        } else {
            ColEntry col1 = typecol.getEntryByName(column1Name);
            ArrayList<DateEntry> dat1s = t.DataColumn.get(col1);
            for (int i = 0, k = 0; i < dat0s.size(); i += 1) {
                if (!(c.compare2(dat0s.get(i), dat1s.get(i)))) {
                    t.row.remove(k);
                    t.datalength -= 1;
                } else {
                    k += 1;
                }
            }
        }
    }
}