import org.junit.Test;

public class TriesTest {
    @Test
    public void PutTest(){
        Tries test = new Tries();
        test.put("ac ");
    }

    @Test
    public void findTest(){
        Tries test = new Tries();
        test.put("Key");
        test.put("keys");
        test.put("keh");
        test.put("kem");
        test.put("Kwmzx");
        test.put("K eww");
        test.put("key");
        System.out.println(test.keysThatMatch("Key"));
        System.out.println(test.keysThatMatch("K e"));
        System.out.println(test.keyWithPrefix("ke"));
    }
}