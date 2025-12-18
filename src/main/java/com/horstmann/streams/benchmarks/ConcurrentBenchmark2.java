package com.horstmann.streams.benchmarks;

import com.horstmann.streams.Points;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import static java.util.concurrent.TimeUnit.*;
import java.awt.Point;
import java.util.stream.*;

public class ConcurrentBenchmark2 {
    private static int POINTS = 10_000_000;
    private static long DIST = Integer.MAX_VALUE;

    @State(Scope.Benchmark)
    public static class MyState {
        public Point[] randomPointArray;
        @Setup(Level.Trial)
        public void doSetup() {
            randomPointArray = Points.randomArray(POINTS);
        }
    }

    @Benchmark
    @BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
    @Warmup(iterations = 10, time = 500, timeUnit = MICROSECONDS)
    @Measurement(iterations = 20, time = 500, timeUnit = MICROSECONDS)
    @OutputTimeUnit(MICROSECONDS)
    public long withSerialStream(MyState state, Blackhole blackhole) {
        long result = Stream.of(state.randomPointArray)
                .mapToLong(p -> (long) p.x * p.x + (long) p.y * p.y)
                .filter(p -> p > DIST)
                .limit(POINTS / 10)
                .min()
                .orElseThrow();
        System.out.println(result);
        blackhole.consume(result);
        return result;
    }

    @Benchmark
    @BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
    @Warmup(iterations = 10, time = 500, timeUnit = MICROSECONDS)
    @Measurement(iterations = 20, time = 500, timeUnit = MICROSECONDS)
    @OutputTimeUnit(MICROSECONDS)
    public long withParallelStream(MyState state, Blackhole blackhole) {
        long result = Stream.of(state.randomPointArray)
                .parallel()
                .mapToLong(p -> (long) p.x * p.x + (long) p.y * p.y)
                .filter(p -> p > DIST)
                .limit(POINTS / 10)
                .min()
                .orElseThrow();
        System.out.println(result);
        blackhole.consume(result);
        return result;
    }

    @Benchmark
    @BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
    @Warmup(iterations = 10, time = 500, timeUnit = MICROSECONDS)
    @Measurement(iterations = 20, time = 500, timeUnit = MICROSECONDS)
    @OutputTimeUnit(MICROSECONDS)
    public long withUnorderedParallelStream(MyState state, Blackhole blackhole) {
        long result = Stream.of(state.randomPointArray)
                .parallel()
                .unordered()
                .mapToLong(p -> (long) p.x * p.x + (long) p.y * p.y)
                .filter(p -> p > DIST)
                .limit(POINTS / 10)
                .min()
                .orElseThrow();
        System.out.println(result);
        blackhole.consume(result);
        return result;
    }
}