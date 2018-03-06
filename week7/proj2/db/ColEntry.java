package db;

import org.junit.Test;

import static org.junit.Assert.*;

public class ColEntry {
    private String name;
    private String type;

    public ColEntry(String name, String type) {
        this.name = name;
        if (type != "int" && type != "float" && type != "String")
            throw new RuntimeException("Unresolved type");
        this.type = type;
    }

    public boolean equals(Object O) {
        ColEntry b = (ColEntry) O;
        return (this.name == b.name) && (this.type == b.type);
    }

    public String name(){
        return this.name;
    }

    public String type(){
        return this.type;
    }

    public static class test{

        @Test
        public void testEquals(){
            ColEntry a = new ColEntry("x", "int");
            ColEntry b = new ColEntry("x", "int");
            ColEntry c = new ColEntry("y", "double");
            assertEquals(a, b);
            assertNotEquals(a, c);
            assertNotEquals(b, c);
        }

        @Test
        public void testFail(){
            try{
                new ColEntry("x", "string");
            }
            catch(Exception e){
                System.out.print("second test pass");
            }
        }
    }
}
