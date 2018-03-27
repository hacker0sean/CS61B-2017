import java.io.IOException;
import java.util.*;

public class HuffmanEncoder {

    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols){
        Map<Character, Integer> map = new TreeMap<>();
        for (char i : inputSymbols){
            if (map.containsKey(i)){
                map.put(i, map.get(i) + 1);
            }
            else{
                map.put(i, 0);
            }
        }
        return map;
    }

    public static void main(String[] args) throws IOException {
        ArrayList<Character> charCache = new ArrayList<>();
        char[] inputSymbols = FileUtils.readFile(args[0]);
        Map<Character, Integer> FrequencyTable = buildFrequencyTable(inputSymbols);
        BinaryTrie binaryTrie = new BinaryTrie(FrequencyTable);
        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        Map<Character, BitSequence> characterBitSequenceMap = binaryTrie.buildLookupTable();
        ow.writeObject(binaryTrie);
        ow.writeObject(inputSymbols.length);
        List<BitSequence> bitSequences = new ArrayList<>();
        for (Character entry : inputSymbols) {
            bitSequences.add(characterBitSequenceMap.get(entry));
        }
        BitSequence bitSequence = BitSequence.assemble(bitSequences);
        ow.writeObject(bitSequence);
    }
}