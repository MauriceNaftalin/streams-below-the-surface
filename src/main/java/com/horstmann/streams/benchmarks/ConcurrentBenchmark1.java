package com.horstmann.streams.benchmarks;

import com.horstmann.streams.Points;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import static java.util.concurrent.TimeUnit.*;
import java.awt.Point;
import java.util.stream.*;

public class ConcurrentBenchmark1 {
    private static int POINTS = 10_000_000;

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
    public int withSerialStream(MyState state, Blackhole blackhole) {
        var largest = Stream.of(state.randomPointArray)
                .map(Points::lengthSquared)
                .max(Integer::compare)
                .orElseThrow();
        System.out.println(largest);
        blackhole.consume(largest);
        return largest;
    }

    @Benchmark
    @BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
    @Warmup(iterations = 10, time = 500, timeUnit = MICROSECONDS)
    @Measurement(iterations = 20, time = 500, timeUnit = MICROSECONDS)
    @OutputTimeUnit(MICROSECONDS)
    public int withParallelStream(MyState state, Blackhole blackhole) {
        int largest = Stream.of(state.randomPointArray)
                .parallel()
                .map(Points::lengthSquared)
                .max(Integer::compare)
                .orElseThrow();
        System.out.println(largest);
        blackhole.consume(largest);
        return largest;
    }
}