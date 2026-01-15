package scot.jalba.streams;

import java.util.random.RandomGenerator;

public class Util {
    public static void time(Runnable r) {
        long start = System.nanoTime();
        r.run();
        long end = System.nanoTime();
        IO.println("%.3f sec".formatted((end - start) / 1E9));
    }

    private static RandomGenerator generator = RandomGenerator.getDefault();

    public static int[] randomIntArray(int n) {
        int[] result = new int[n];
        for (int i = 0; i < n; i++) result[i] = generator.nextInt();
        return result;
    }
}
