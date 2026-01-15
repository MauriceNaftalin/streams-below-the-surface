package scot.jalba.streams.demos;

import scot.jalba.streams.Points;

import java.util.stream.Stream;

public class ParallelWithLimit {
    private static int POINTS = 10_000_000;
    private static long DIST = Integer.MAX_VALUE;

    void main() {
        var randomPointArray = Points.randomArray(POINTS);
        long result = Stream.of(randomPointArray)
                .parallel()
                .mapToLong(p -> (long) p.x * p.x + (long) p.y * p.y)
                .filter(p -> p > DIST)
                .limit(POINTS / 10)
                .min()
                .orElseThrow();
        System.out.println(result);
    }
}
