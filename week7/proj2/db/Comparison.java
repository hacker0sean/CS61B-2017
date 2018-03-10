package db;

public interface Comparison {
    /** Compares columnNum1 with the literal using the comparisonCharacter */
    boolean compare(DateEntry Num1, String columnNum2);

    /** Compares columnNum1 with columnNum2 using the comparisonCharacter */
    boolean compare2(DateEntry Num1, DateEntry columnNum2);
}
