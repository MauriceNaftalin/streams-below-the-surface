package scot.jalba.streams.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import module java.base;

import static scot.jalba.streams.Util.randomIntArray;

public class LoopVsStreamBenchmark {

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        public int LOOPS = 50_000;
        public int POINTS = 20_000;
        public int[] coords = randomIntArray(2 * POINTS);
    }

    @Benchmark
    public void streamInLoop(BenchmarkState state, Blackhole bh) {
        int[] coords = state.coords;
        for (int k = 0; k < state.LOOPS; k++) {
            long result = IntStream.range(0, state.POINTS).mapToLong(i -> {
                        int x = coords[2 * i];
                        int y = coords[2 * i + 1];
                        return (long) x * x + (long) y * y;
                    })
                    .filter(p -> p > Integer.MAX_VALUE)
                    .limit(state.POINTS / 10)
                    .min()
                    .orElseThrow();
            bh.consume(result);
        }
    }

    @Benchmark
    public void loopInLoop(BenchmarkState state, Blackhole bh) {
        int[] coords = state.coords;
        for (int k = 0; k < state.LOOPS; k++) {
            int limit = state.POINTS / 10;
            long min = Long.MAX_VALUE;
            for (int i = 0; i < state.POINTS && limit >= 0; i++) {
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
            bh.consume(min);
        }
    }
}
