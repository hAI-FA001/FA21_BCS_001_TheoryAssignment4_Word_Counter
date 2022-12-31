import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

public class WordCounter implements Callable<HashMap<String, Integer>> {
    List<String> lines;
    int lineNum;
    HashMap<String, Integer> wordsWithCount;
    WordCounter(List<String> lines, int lineNum) {
        this.lines = lines;
        this.lineNum = lineNum;
    }

    @Override
    public HashMap<String, Integer> call() {
        wordsWithCount = new HashMap<>();

        lines.stream().map(line -> line.split(" ")).forEach(words -> Arrays.stream(words)
                .map(word -> word.replaceAll("[^a-zA-Z0-9 ]+", " ").toLowerCase()
                        .strip().split(" "))
                .forEach(splitWords -> Arrays.stream(splitWords).forEach(word -> {
                    if (word.isBlank() || word.isEmpty()) return;
                    if (wordsWithCount.containsKey(word)) wordsWithCount.replace(word, wordsWithCount.get(word) + 1);
                    else wordsWithCount.put(word, 1);
                })));

        return wordsWithCount;
    }
}
