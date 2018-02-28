public class SLList {
    private class IntNode {
        public int item;
        public IntNode next;

        public IntNode(int item, IntNode next) {
            this.item = item;
            this.next = next;
        }
    }

    private IntNode first;

    public void addFirst(int x) {
        first = new IntNode(x, first);
    }

    public SLList(int x){
      addFirst(x);
    }

    public void insert(int item, int position) {
      IntNode temp = first;
      for (int i = 0; i < position - 1; i += 1){
        temp = temp.next;
      }
      IntNode temp_next = temp.next;
      temp.next = new IntNode(item, temp_next);
    }

    public void reverse() {
      IntNode prev = first;
      IntNode cur = first.next;
      IntNode nex = first.next.next;
      while (nex.next != null){
        cur.next = prev;
        prev = cur;
        cur = nex;
        nex = nex.next;
      }
      cur.next = prev;
      nex.next = cur;
      first = nex;
    }

    public static void test(){
      SLList testList = new SLList(5);
      testList.addFirst(15);
      testList.addFirst(20);
      testList.insert(10, 1);
      testList.reverse();
    }

    public static void main(String[] args){
      test();
    }
}
