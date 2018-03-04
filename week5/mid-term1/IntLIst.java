public class IntList {
    public int first;
    public IntList rest;

    public IntList (int f, IntList r) {
        this.first = f;
        this.rest = r;
    }

    /**
     * Given a sorted linked list of items - remove duplicates.
     * For example given 1 -> 2 -> 2 -> 2 -> 3,
     * Mutate it to become 1 -> 2 -> 3 (destructively)
     */
    public static void removeDuplicates(IntList p) {
        if (p.rest == null) {
            return;
        }
        IntList current = p.rest;
        IntList previous = p;
        while (current != null) {
            if (current.first == previous.first) {
                previous.rest = current.rest;
            } else {
                previous = current;
            }
            current = previous.rest;
        }
    }

    @Override
    public boolean equals(Object o) { ...}

    public void skippify() {
        IntList p = this;
        int n = 1;
        while (p != null) {
            IntList next = p.rest;
            for (int i = 0; i < n; i += 1) {
                if (next == null) {
                    return;
                }
                next = next.rest;
            }
            p.rest = next;
            p = p.rest;
            n = n + 1;
        }
    }

    @Test
    public testSkippify() {
        IntList A = IntList.list(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        IntList B = IntList.list(9, 8, 7, 6, 5, 4, 3, 2, 1);
        A.skippify();
        B.skippify();
        IntList expectA = IntList.list(1, 3, 6, 10);
        IntList expectB = IntList.list(9, 7, 4);
        assertEquals(expectA, A);
        assertEquals(expectB, B);
    }
}