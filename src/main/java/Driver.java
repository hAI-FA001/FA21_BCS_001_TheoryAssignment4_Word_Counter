import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Driver {
    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        LocalTime startTime;

        System.out.println("Start time: " + (startTime = LocalTime.now()));

        List<FutureTask<List<HashMap<String, Integer>>>> tasks = new ArrayList<>();
        final int MAX_NUM = 12386;
        final int INCREMENT = 100;

        for (int i = 10001; i < MAX_NUM; i+=INCREMENT+1) {
            tasks.add(new FutureTask<>(new Matcher(i, i+=INCREMENT)));
            tasks.get(tasks.size()-1).run();
        }
        Aggregator.aggregate(tasks);

        LocalTime endTime;
        System.out.println("End time: " + (endTime = LocalTime.now()));
        System.out.println("Total Duration: " + LocalTime.ofNanoOfDay(endTime.toNanoOfDay() - startTime.toNanoOfDay()));
    }
}
