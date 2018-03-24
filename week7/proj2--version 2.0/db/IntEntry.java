package db;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntEntry extends DateEntry<Integer> {

    public IntEntry(String value) throws RuntimeException {
        super(false, false, value.trim(), "int");
        realvalue = value();
        if (value.equals(NOVALUE)){
            value = null;
            super.NOVALUE = true;
        }
    }

    public IntEntry(){
        super(true, false, "NaN", "int");
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Integer value(){
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
