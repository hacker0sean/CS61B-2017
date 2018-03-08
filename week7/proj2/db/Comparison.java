package db;

public interface Comparison {
    /** Compares columnNum1 with the literal using the comparisonCharacter */
    boolean compare(Table t, int columnNum1, String literal);

    /** Compares columnNum1 with columnNum2 using the comparisonCharacter */
    boolean compare(Table t, int columnNum1, int columnNum2);
}
