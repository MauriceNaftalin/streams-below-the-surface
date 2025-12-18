package com.horstmann.streams.benchmarks;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import com.horstmann.streams.Points;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import static java.util.concurrent.TimeUnit.*;

public class FilterMaxBenchmark {
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
    @OutputTimeUnit(MILLISECONDS)
    public Point withCollection(MyState state, Blackhole blackhole) {
        List<Point> points = List.of(state.randomPointArray);
        Point largest = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            Point p = points.get(i);
            if (Points.isShort(p)) {
                if (Points.lengthSquared(p) > Points.lengthSquared(largest)) {
                    largest = p;
                }
            }
        }
        blackhole.consume(largest);
        return largest;
    }

    @Benchmark
    @BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
    @Warmup(iterations = 10, time = 500, timeUnit = MICROSECONDS)
    @Measurement(iterations = 20, time = 500, timeUnit = MICROSECONDS)
    @OutputTimeUnit(MILLISECONDS)
    public Point withArray(MyState state, Blackhole blackhole) {
        Point[] points = state.randomPointArray;
        Point largest = points[0];
        for (int i = 1; i < points.length; i++) {
            Point p = points[i];
            if (Points.isShort(p)) {
                if (Points.lengthSquared(p) > Points.lengthSquared(largest)) {
                    largest = p;
                }
            }
        }
        blackhole.consume(largest);
        return largest;
    }

    @Benchmark
    @BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
    @Warmup(iterations = 10, time = 500, timeUnit = MICROSECONDS)
    @Measurement(iterations = 20, time = 500, timeUnit = MICROSECONDS)
    @OutputTimeUnit(MILLISECONDS)
    public Point withStream(MyState state, Blackhole blackhole) {
        Point largest = Stream.of(state.randomPointArray)
                .filter(Points::isShort)
                .max(Comparator.comparingInt(Points::lengthSquared))
                .orElseThrow();
        blackhole.consume(largest);
        return largest;
    }
}
