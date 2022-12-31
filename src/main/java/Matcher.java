import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;

public class Matcher implements Callable<List<HashMap<String, Integer>>> {
    SortedMap<String, Charset> charsetsToTry = Charset.availableCharsets();
    String pathToFile = "dataset/dataset/";
    List<List<String>> filesLines;
    int start;
    int end;

    Matcher(int start, int end){
        this.start = start;
        this.end = end;
    }
    @Override
    public List<HashMap<String, Integer>> call() {
        filesLines = new ArrayList<>();
        for (int i = start; i < end + 1; i++)
            try {
                filesLines.add(Files.readAllLines(Paths.get(pathToFile + i + ".txt")));
            } catch (IOException e) {
                if(e instanceof NoSuchFileException) {
//                    System.out.println("No file known as:" + pathToFile + i + ".txt");
                }
                else if(e instanceof MalformedInputException) {
                    int j=0;
                    while(j < charsetsToTry.values().size()){
                        try {
                            filesLines.add(Files.readAllLines(Paths.get(pathToFile+i+".txt"),
                                    charsetsToTry.values().stream().toList().get(j++)));
                            break;
                        } catch (IOException ignored) {}
                    }
                }
                else
                    e.printStackTrace();
            }

        List<HashMap<String, Integer>> wordWithCount = filesLines.stream().map(
                lines -> new WordCounter(lines, filesLines.indexOf(lines)).call()
        ).toList();

        HashMap<String, Integer> resultant = new HashMap<>();
        wordWithCount.forEach(curMap -> curMap.forEach((key, value) -> resultant.merge(key, value, Integer::sum)));

        return Collections.singletonList(resultant);
    }
}
