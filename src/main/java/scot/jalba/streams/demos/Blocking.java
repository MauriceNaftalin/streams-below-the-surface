package scot.jalba.streams.demos;

import java.util.stream.Gatherers;
import java.util.stream.IntStream;
import static scot.jalba.streams.Util.time;

public class Blocking {
    String task(int i) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException _) {
        }
        return i + " " + Thread.currentThread().toString();
    }

    void main() {
        time(() -> IntStream.range(0, 60).mapToObj(this::task).toList());
        time(() -> IntStream.range(0, 60).parallel().mapToObj(this::task).toList());
        time(() -> IntStream.range(0, 60).boxed().gather(Gatherers.mapConcurrent(60, this::task)).toList());
    }
}
