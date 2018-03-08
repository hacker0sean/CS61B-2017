package db;

public class Condition {
    boolean isUnary;
    String columnName;
    String column0Name;
    String column1Name;
    Comparison c;

    /** Returns true if the given rowNum in the table t obeys this
     * condition. */
    public boolean obeysCondition(Table t, int rowNum){
        return true;
    }
}
