import org.junit.Test;
import static org.junit.Assert.*;

public class OffByOneTest {
    @Test
    public void testEqualChar(){
        OffByOne test = new OffByOne();
        assertTrue(test.equalChars('r', 'q'));
        assertTrue(test.equalChars('a', 'b'));
        assertFalse(test.equalChars('a', 'e'));
    }
}
