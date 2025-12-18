package com.horstmann.streams.demos;

import java.util.stream.Gatherers;
import java.util.stream.IntStream;

public class Blocking {
    String task(int i) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException _) {
        }
        return i + " " + Thread.currentThread().toString();
    }

    void time(Runnable r) {
        long start = System.nanoTime();
        r.run();
        long end = System.nanoTime();
        IO.println("%.3f sec".formatted((end - start) / 1E9));
    }

    void main() {
        time(() -> IntStream.range(0, 60).mapToObj(this::task).toList());
        time(() -> IntStream.range(0, 60).parallel().mapToObj(this::task).toList());
        time(() -> IntStream.range(0, 60).boxed().gather(Gatherers.mapConcurrent(60, this::task)).toList());
    }
}
