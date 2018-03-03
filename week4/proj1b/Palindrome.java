public class Palindrome {
    public static Deque<Character> wordToDeque(String word){
        Deque<Character> result = new ArrayDequeSolution<>();
        for (int i = 0; i < word.length(); i += 1){
            result.addLast(word.charAt(i));
        }
        return result;
    }

    public static boolean isPalindrome(String word){
        if (word.length() == 0 || word.length() == 1)
            return true;
        for (int i = 0, j = word.length() - 1; i < j; i++, j--){
            if (word.charAt(i) != word.charAt(j))
                return false;
        }
        return true;
    }

    public static boolean isPalindrome(String word, CharacterComparator cc){
        if (word.length() == 0 || word.length() == 1)
            return true;
        for (int i = 0, j = word.length() - 1; i < j; i++, j--){
            if (!cc.equalChars(word.charAt(i), word.charAt(j)))
                return false;
        }
        return true;
    }
}
