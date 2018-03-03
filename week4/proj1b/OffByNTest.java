import org.junit.Test;
import static org.junit.Assert.*;

public class OffByNTest {
    @Test
    public void testEqualChar(){
        OffByN test = new OffByN(5);
        assertFalse(test.equalChars('f', 'h'));
        assertTrue(test.equalChars('a', 'f'));
    }
}