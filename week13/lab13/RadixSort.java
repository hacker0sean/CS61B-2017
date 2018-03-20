import org.junit.Test;

import java.util.ArrayList;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra
 * @version 1.4 - April 14, 2016
 *
 **/
public class RadixSort
{
    public static String[] nonNegative = {"220", "221", "224", "189", "227", "21", "9", "923", "231", "221", "222", "225", "92", "242", "273", "987", "912", "531", "256", "742", "632",  "abc"};
    public static String[] nonNegative2 = {"220", "221", "224", "227", "231", "221", "222", "225", "242", "273", "256"};
    /**
     * Does Radix sort on the passed in array with the following restrictions:
     *  The array can only have ASCII Strings (sequence of 1 byte characters)
     *  The sorting is stable and non-destructive
     *  The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     **/
    public static String[] sort(String[] asciis)
    {
        String[] strs = asciis.clone();
        strs = sortprepartion(strs);
        return strs;
    }

    private static String[] sortprepartion(String[] strs){
        int maxLength = 0;
        for (String i : strs){
            if (i.length() > maxLength)
                maxLength = i.length();
        }
        ArrayList<ArrayList<String>> countlist = new ArrayList<>(maxLength + 1);
        for (int i = 0; i < maxLength + 1; i++){
            countlist.add(new ArrayList<String>());
        }
        for (int i = 0; i < strs.length; i++) {
            int number = strs[i].length();
            countlist.get(number).add(strs[i]);
        }

        // put the value count times into a new array
        int k = 0;
        for (int i = 0; i < countlist.size(); i += 1) {
            for (int j = 0; j < countlist.get(i).size(); j += 1, k += 1) {
                strs[k] = countlist.get(i).get(j);
            }
        }
        int start = 0;
        int end = strs.length;
        while (start < end){
            int s = start;
            while ((s + 1 < end) && strs[s].length() == strs[s + 1].length()) s++;
            s = s + 1;
            if (s - start == 1){}
            else{
                sort(strs, start, s , 0, strs[start].length());
            }
            start = s;
        }
        return strs;
    }

    private static String[] sort(String[] strs, int start, int end, int index, int maxlength){
        sortHelper(strs, start, end, index);
        if (index == (maxlength - 1)) {
            return strs;
        }
        else{
            while (start < end){
                int k = start;
                while ((k + 1 < end) && strs[k].charAt(index) == strs[k + 1].charAt(index)) k++;
                k = k + 1;
                if (k - start == 1){}
                else {
                    sort(strs, start, k, index + 1, maxlength);
                }
                start = k;
            }
            return strs;
        }
    }

    /**
     * Radix sort helper function that recursively calls itself to achieve the sorted array
     *  destructive method that changes the passed in array, asciis
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelper(String[] asciis, int start, int end, int index)
    {
        String[] arr = asciis;
        int max = 255;

        // gather all the counts for each value
        ArrayList<ArrayList<String>> countlist = new ArrayList<>(max + 1);
        for (int i = 0; i < max + 1; i++){
            countlist.add(new ArrayList<String>());
        }
        for (int i = start; i < end; i++) {
            int number = (int)arr[i].charAt(index);
            countlist.get(number).add(arr[i]);
        }

        // put the value count times into a new array
        int k = start;
        for (int i = 0; i < countlist.size(); i += 1) {
            for (int j = 0; j < countlist.get(i).size(); j += 1, k += 1) {
                arr[k] = countlist.get(i).get(j);
            }
        }
    }

    public void printString(String[] arr){
        for (String i : arr){
            System.out.print(i + " ");
        }
        System.out.println();
    }
    @Test
    public void TestSortHelper(){
        printString(nonNegative2);
        sortHelper(nonNegative2, 0, nonNegative2.length, 1);
        printString(nonNegative2);
    }

    @Test
    public void TestSort(){
        printString(nonNegative);
        printString(sort(nonNegative));
    }
}
