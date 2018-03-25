package db;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class IntEntry extends DateEntry<Integer> {

    public IntEntry(String value) throws RuntimeException {
        super(false, false, value.trim(), "int");
        if (value.equals("NOVALUE")) {
            value = null;
            super.NOVALUE = true;
            return;
        }
        realvalue = value();
    }

    public IntEntry() {
        super(true, false, "NaN", "int");
    }

    public static IntEntry Int_operator_Literal(IntEntry entry1, String x2, char operator) throws RuntimeException{
        IntEntry entry2 = new IntEntry(x2);
        return Int_operator_Int(entry1, entry2, operator);
    }

    public static IntEntry Int_operator_Int(IntEntry entry1, IntEntry entry2, char operator) throws RuntimeException {
        IntEntry entrynew;
        if (entry1.NaN || entry2.NaN)
            entrynew = new IntEntry();
        else if (entry1.NOVALUE && entry2.NOVALUE)
            entrynew = new IntEntry("NOVALUE");
        else if (entry1.NOVALUE || entry2.NOVALUE)
            entrynew = new IntEntry("0");
        else {
            int x1 = entry1.realvalue;
            int x2 = entry2.realvalue;
            if (operator == '+') {
                entrynew = new IntEntry(((Integer) (x1 + x2)).toString());
            } else if (operator == '-') {
                entrynew = new IntEntry(((Integer) (x1 - x2)).toString());
            } else if (operator == '*') {
                entrynew = new IntEntry(((Integer) (x1 * x2)).toString());
            } else {
                if (x2 == 0)
                    entrynew = new IntEntry();
                else {
                    entrynew = new IntEntry(((Integer) (x1 / x2)).toString());
                }
            }
        }
        return entrynew;
    }

    @Override
    public String toString() {
        if (NaN)
            return "NaN";
        if (NOVALUE)
            return "NOVALUE";
        return value;
    }

    public int realvalue(){
        return realvalue;
    }

    @Override
    public Integer value() {
        return Integer.parseInt(value);
    }

    public boolean equals(Object o) {
        DateEntry b = (DateEntry) o;
        if (b.NaN() && this.NaN())
            return true;
        else if (b.NaN() || this.NaN())
            return false;

        if (b.NOVALUE() || this.NOVALUE())
            return false;

        if (!(b.type().equals(this.type())))
            return false;
        else if (!(b.value.equals(this.value)))
            return false;
        return true;
    }

    public static class test {

        @Test
        public void testEqual() {
            IntEntry a = new IntEntry("2");
            IntEntry b = new IntEntry("3");
            assertNotEquals(a, b);
            b.value = "2";
            assertEquals(a, b);
            b.setNaN();
            assertNotEquals(a, b);
            b.setNaN();
            assertEquals(a, b);
            StringEntry c = new StringEntry("3");
            assertNotEquals(b, c);
        }

        @Test
        public void testNeg() {
            IntEntry a = new IntEntry("-2");
            FloatEntry d = new FloatEntry("-2");
            Integer b = -2;
            Integer c = a.realvalue;
            // assertEquals(b, d.realvalue);
        }
    }
}
