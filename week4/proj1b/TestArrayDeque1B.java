import org.junit.Test;
import static org.junit.Assert.*;

public class TestArrayDeque1B {

    @Test
    public void testEmpty(){
        StudentArrayDeque<Integer> array = new StudentArrayDeque<>();
        assertEquals(array.size(), 0);
        assertTrue(array.isEmpty());
    }

    @Test
    public void testAddFirst(){
        StudentArrayDeque<Integer> array = new StudentArrayDeque<>();
        array.addFirst(1);
        array.addFirst(2);
        array.addFirst(3);
        array.addFirst(4);
        assertEquals(array.size(), 4);
        int test = array.get(2);
        assertEquals(2, test);
    }

    @Test
    public void testAddLast(){
        StudentArrayDeque<Integer> array = new StudentArrayDeque<>();
        array.addLast(1);
        array.addLast(2);
        array.addLast(3);
        array.addLast(4);
        assertEquals(4, array.size());
        int test = array.get(2);
        assertEquals(3, test);
    }

    @Test
    public void testRemove(){
        StudentArrayDeque<Integer> array = new StudentArrayDeque<>();
        for (int i = 0; i < 16; i += 1){
            Integer random = (int)(Math.random() * 10);
            array.addFirst(random);
        }

        for (int i = 0; i < 16; i += 1){
            array.removeFirst();
        }

        assertEquals(0, array.size());
        assertTrue(array.isEmpty());
    }

    @Test
    public void testRemove2(){
        StudentArrayDeque<Integer> array = new StudentArrayDeque<>();
        array.addFirst(1);
        array.addFirst(2);
        array.addFirst(3);
        array.addFirst(4);
        Integer x = array.removeFirst();
        assertEquals((Integer)4, x);
    }

    @Test
    public void testResize(){
        StudentArrayDeque<Integer> saq1 = new StudentArrayDeque<>();
        OperationSequence fs = new OperationSequence();
        for(int i =1;i<=20;i++) {
            saq1.addLast(i);
            fs.addOperation(new DequeOperation("addFirst", i));
        }
        for(int i=1;i<=16;i++){
            saq1.removeFirst();
            fs.addOperation(new DequeOperation("removeFirst"));
        }
        Integer result =saq1.get(0);
        fs.addOperation(new DequeOperation("get", 0));
        //assertEquals(,17,result);
        assertEquals("\n" + fs.toString(),
                (Integer) 17, result);
    }
    public static void main(String[] args){
        jh61b.junit.TestRunner.runTests("all", TestArrayDeque1B.class);
    }
}
