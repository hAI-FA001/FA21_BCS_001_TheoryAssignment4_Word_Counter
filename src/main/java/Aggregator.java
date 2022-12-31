import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

public class Aggregator {
    public static void aggregate(List<FutureTask<List<HashMap<String, Integer>>>> tasks) throws IOException {
        HashMap<String, Integer> mainResult = new HashMap<>();

        BufferedWriter writer = new BufferedWriter(new FileWriter("result.txt"));
        writer.write(String.format("%5s%20s%5s%12s\n", "INDEX", "WORDS", "-->", "OCCURENCES"));

        tasks.stream().map(listFutureTask -> {
            try {
                return listFutureTask.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).forEach(mappedList -> mappedList.forEach(curMap -> curMap
                .forEach((word, count) -> mainResult.merge(word, count, Integer::sum))));

        AtomicInteger wordIdx = new AtomicInteger(1);
        mainResult.forEach((word, count) -> {
            try {
                writer.write(String.format("%5s%20s%5s%7s\n", wordIdx.getAndIncrement() +".", word, "-->", count));
            } catch (IOException ignored) {}
        });

        writer.flush();
        writer.close();
    }
}
