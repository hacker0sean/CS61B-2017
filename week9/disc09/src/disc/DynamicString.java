package disc;
import java.util.ArrayList;

class DynamicString {
    ArrayList<Character> vals;

    public int hashCode() {
        int h = 0;
        for (int i = 0; i < vals.size(); i++) {
            h = 31 * h + vals.get(i);
        }
        return h;
    }

    public boolean equals(Object o) {
        DynamicString d = (DynamicString) o;
        return vals.equals(d.vals);
    }
}
