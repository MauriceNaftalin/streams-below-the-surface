package com.horstmann.streams.demos;

import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

public class Memory {
    int LOOPS = 100_000;
    int POINTS = 100_000_000;

    RandomGenerator generator = RandomGenerator.getDefault();

    int[] init(int points) {
        int[] randomCoordArray = new int[2 * points];
        for (int i = 0; i < randomCoordArray.length; i++) randomCoordArray[i] = generator.nextInt();
        return randomCoordArray;
    }

    void main() {
        int[] coords = init(POINTS / LOOPS);
        for (int k = 0; k < LOOPS; k++) {
            long result = IntStream.range(0, coords.length / 2)
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
}
