public class HuffmanDecoder {
    public static void main(String[] args){
        ObjectReader or = new ObjectReader(args[0]);
        Object x = or.readObject();
        Object y = or.readObject();
        Object z = or.readObject();
        BitSequence bitSequence = (BitSequence) z;
        int inputSymbolsLength = (int) y;
        BinaryTrie binaryTrie = (BinaryTrie) x;
        char[] outputSymbols = new char[inputSymbolsLength];
        int i = 0;
        int length = 0;
        while (i < inputSymbolsLength){
            Match temp = binaryTrie.longestPrefixMatch(bitSequence.allButFirstNBits(length));
            length += temp.getSequence().length();
            outputSymbols[i] = temp.getSymbol();
            i++;
        }
        FileUtils.writeCharArray(args[1], outputSymbols);
    }
}