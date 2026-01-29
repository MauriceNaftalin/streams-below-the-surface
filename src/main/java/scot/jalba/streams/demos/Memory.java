package scot.jalba.streams.demos;

import java.util.stream.IntStream;

import static scot.jalba.streams.Util.*;

public class Memory {
    int LOOPS = 5_000_000;
    int POINTS = 20_000;

    void main() {
        int[] coords = randomIntArray(2* POINTS);
        IO.println("stream");
        time(() -> streamInLoop(coords));
        IO.println("loop");
        time(() -> loopInLoop(coords));
    }

    private void streamInLoop(int[] coords) {
        for (int k = 0; k < LOOPS; k++) {
            long result = IntStream.range(0, POINTS)
                    .mapToLong(i -> {
                        int x = coords[2 * i];
                        int y = coords[2 * i + 1];
                        return (long) x * x + (long) y * y;
                    })
                    .filter(p -> p > Integer.MAX_VALUE)
                    .limit(POINTS / 10)
                    .min()
                    .orElseThrow();
            if (k == LOOPS - 1) IO.println(result);
        }
    }

    private void loopInLoop(int[] coords) {
        for (int k = 0; k < LOOPS; k++) {
            int limit = POINTS / 10;
            long min = Long.MAX_VALUE;
            for (int i = 0; i < POINTS && limit >= 0; i++) {
                int x = coords[2 * i];
                int y = coords[2 * i + 1];
                long z = (long) x * x + (long) y * y;
                if (z > Integer.MAX_VALUE) {
                    limit--;
                    if (z < min) {
                        min = z;
                    }
                }
            }
            if (k == LOOPS - 1) IO.println(min);
        }
    }
}
