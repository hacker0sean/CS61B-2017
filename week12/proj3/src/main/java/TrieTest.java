import org.junit.Test;

public class TrieTest {
    @Test
    public void PutTest(){
        Trie test = new Trie();
        test.put("ac ");
    }

    @Test
    public void findTest(){
        Trie test = new Trie();
        test.put("Key");
        test.put("Keys");
        test.put("Keh");
        test.put("Kem");
        test.put("Kwmzx");
        test.put("K eww");
        test.put("key");
        System.out.println(test.find("ke"));
    }
}
