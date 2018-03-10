package db;

import java.util.ArrayList;

public class Condition {
    boolean isUnary;
    String column0Name;
    String column1Name;
    Comparison c;

    public Condition(String str, column acol) throws RuntimeException {
        String[] strs = str.split("\\s*(?:==|!=|<=|>=|<|>)\\s*");
        column0Name = strs[0].trim();
        column1Name = strs[1].trim();
        if (acol.contain(column0Name)) {
            if (str.contains(">"))
                c = new greaterThan();
            else if (str.contains(">="))
                c = new greaterThanOrEquals();
            else if (str.contains("<"))
                c = new lessThan();
            else if (str.contains("<="))
                c = new lessThanOrEquals();
            else if (str.contains("=="))
                c = new equals();
            else if (str.contains("!="))
                c = new notEquals();
            if (acol.contain(column1Name)) {
                isUnary = false;
            } else {
                isUnary = true;
            }
        } else
            throw new RuntimeException("Error: Can't find the column name.!");
    }

    /**
     * Returns true if the given rowNum in the table t obeys this
     * condition.
     */
    public boolean obeysCondition(Table t, column acol) throws RuntimeException {
        if (isUnary) {
            ColEntry col0 = acol.getEntryByName(column0Name);
            t.generateDataRow();
            ArrayList<DateEntry> dat0s = t.datrow.get(col0);
            for (int i = 0, k = 0; i < dat0s.size(); i += 1) {
                if (!(c.compare(dat0s.get(i), column1Name))) {
                    t.datcol.remove(k);
                    t.datalength -= 1;
                }
                else{
                    k += 1;
                }
            }
        } else {
            ColEntry col0 = acol.getEntryByName(column0Name);
            ColEntry col1 = acol.getEntryByName(column1Name);
            t.generateDataRow();
            ArrayList<DateEntry> dat0s = t.datrow.get(col0);
            ArrayList<DateEntry> dat1s = t.datrow.get(col1);
            if (col0.type().equals("string")) {
                if (col1.type().equals("string")) {
                    for (int i = 0, k = 0; i < dat0s.size(); i += 1) {
                        if (!(c.compare2(dat0s.get(i), dat1s.get(i)))) {
                            t.datcol.remove(k);
                            t.datalength -= 1;
                        }
                        else{
                            k += 1;
                        }
                    }
                } else
                    throw new RuntimeException("Error: Type dismatch!");
            } else {
                for (int i = 0, j = 0; i < dat0s.size(); i += 1) {
                    if (!(c.compare2(dat0s.get(i), dat1s.get(i)))) {
                        t.datcol.remove(j);
                        t.datalength -= 1;
                    }
                    else{
                        j += 1;
                    }
                }
            }
        }
        return true;
    }
}
