package scot.jalba.streams.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import module java.base;

import static scot.jalba.streams.Util.randomIntArray;

public class LoopVsStreamBenchmark {

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        @Param({
                "200000|5000",
                "50000|20000",
                "20000|50000",
                "5000|200000"
        })
        public String config;

        @Setup(Level.Trial)
        public void setup() {
            String[] parts = config.split("\\|");
            LOOPS = Integer.parseInt(parts[0]);
            POINTS = Integer.parseInt(parts[1]);
            coords = randomIntArray(2 * POINTS);
        }
        public int LOOPS;
        public int POINTS;
        public int[] coords;
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

    @Benchmark
    public void streamInLoopNoLimit(BenchmarkState state, Blackhole bh) {
        int[] coords = state.coords;
        for (int k = 0; k < state.LOOPS; k++) {
            long result = IntStream.range(0, state.POINTS).mapToLong(i -> {
                        int x = coords[2 * i];
                        int y = coords[2 * i + 1];
                        return (long) x * x + (long) y * y;
                    })
                    .filter(p -> p > Integer.MAX_VALUE)
                    .min()
                    .orElseThrow();
            bh.consume(result);
        }
    }

    @Benchmark
    public void loopInLoopNoLimit(BenchmarkState state, Blackhole bh) {
        int[] coords = state.coords;
        for (int k = 0; k < state.LOOPS; k++) {
            long min = Long.MAX_VALUE;
            for (int i = 0; i < state.POINTS; i++) {
                int x = coords[2 * i];
                int y = coords[2 * i + 1];
                long z = (long) x * x + (long) y * y;
                if (z < min) {
                    min = z;
                }
            }
            bh.consume(min);
        }
    }
}