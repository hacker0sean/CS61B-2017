public class OffByOne implements CharacterComparator{

    public OffByOne(){}

    @Override
    public boolean equalChars(char x, char y){
        if (Math.abs(x - y) == 1)
            return true;
        return false;
    };

}
