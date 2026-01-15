package scot.jalba.streams.benchmarks;

import module java.base;

import org.openjdk.jmh.annotations.*;

import static java.util.concurrent.TimeUnit.*;

public class GathererBenchmark {
    private static final int NUMBER_OF_ELEMENTS_PER_RUN = 10_000;
    private static final List<Integer> origin = prepareOriginList();

    public static <T, R> Gatherer<T, Void, R> mapSequential(Function<T, R> mapper) {
        Gatherer.Integrator<Void, T, R> integrator =
                Gatherer.Integrator.of((_, element, downstream) -> {
                    R mappedElement = mapper.apply(element);
                    return downstream.push(mappedElement);
                });

        return Gatherer.ofSequential(integrator);
    }

    public static <T, R> Gatherer<T, Void, R> mapSequentialGreedy(Function<T, R> mapper) {
        Gatherer.Integrator<Void, T, R> integrator =
                Gatherer.Integrator.ofGreedy((_, element, downstream) -> {
                    R mappedElement = mapper.apply(element);
                    return downstream.push(mappedElement);
                });

        return Gatherer.ofSequential(integrator);
    }

    public static <T, R> Gatherer<T, Void, R> map(Function<T, R> mapper) {
        Gatherer.Integrator<Void, T, R> integrator =
                Gatherer.Integrator.of((_, element, downstream) -> {
                    R mappedElement = mapper.apply(element);
                    return downstream.push(mappedElement);
                });

        return Gatherer.of(integrator);
    }

    public static <T, R> Gatherer<T, Void, R> mapGreedy(Function<T, R> mapper) {
        Gatherer.Integrator<Void, T, R> integrator =
                Gatherer.Integrator.ofGreedy((_, element, downstream) -> {
                    R mappedElement = mapper.apply(element);
                    return downstream.push(mappedElement);
                });

        return Gatherer.of(integrator);
    }

    private static List<Integer> prepareOriginList() {
        List<Integer> origin = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_ELEMENTS_PER_RUN; i++) {
            origin.add(i);
        }
        return origin;
    }

    @Benchmark
    @BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
    @OutputTimeUnit(MICROSECONDS)
    public int parallelThenSequentialGreedy() {
        var result = origin.parallelStream()
                .map(i -> i + " " + Thread.currentThread().getName())
                .gather(mapSequentialGreedy(s -> s + " " + Thread.currentThread().getName()))
                .map(s -> s + " " + Thread.currentThread().getName())
                .toList();
        return result.size();
    }

    @Benchmark
    @BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
    @OutputTimeUnit(MICROSECONDS)
    public int parallelThenSequentialPicky() {
        var result = origin.parallelStream()
                .map(i -> i + " " + Thread.currentThread().getName())
                .gather(mapSequential(s -> s + " " + Thread.currentThread().getName()))
                .map(s -> s + " " + Thread.currentThread().getName())
                .toList();
        return result.size();
    }

    @Benchmark
    @BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
    @OutputTimeUnit(MICROSECONDS)
    public int parallelThenParallelizableGreedy() {
        var result = origin.parallelStream()
                .map(i -> i + " " + Thread.currentThread().getName())
                .gather(mapGreedy(s -> s + " " + Thread.currentThread().getName()))
                .map(s -> s + " " + Thread.currentThread().getName())
                .toList();
        return result.size();
    }

    @Benchmark
    @BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
    @OutputTimeUnit(MICROSECONDS)
    public int parallelThenParallelizablePicky() {
        var result = origin.parallelStream()
                .map(i -> i + " " + Thread.currentThread().getName())
                .gather(map(s -> s + " " + Thread.currentThread().getName()))
                .map(s -> s + " " + Thread.currentThread().getName())
                .toList();
        return result.size();
    }

    @Benchmark
    @BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
    @OutputTimeUnit(MICROSECONDS)
    public int serialThenSequentialGreedy() {
        var result = origin.stream()
                .map(i -> i + " " + Thread.currentThread().getName())
                .gather(mapSequentialGreedy(s -> s + " " + Thread.currentThread().getName()))
                .map(s -> s + " " + Thread.currentThread().getName())
                .toList();
        return result.size();
    }

    @Benchmark
    @BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
    @OutputTimeUnit(MICROSECONDS)
    public int serialThenSequentialPicky() {
        var result = origin.stream()
                .map(i -> i + " " + Thread.currentThread().getName())
                .gather(mapSequential(s -> s + " " + Thread.currentThread().getName()))
                .map(s -> s + " " + Thread.currentThread().getName())
                .toList();
        return result.size();
    }

    @Benchmark
    @BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
    @OutputTimeUnit(MICROSECONDS)
    public int serialThenParallelizableGreedy() {
        var result = origin.stream()
                .map(i -> i + " " + Thread.currentThread().getName())
                .gather(mapGreedy(s -> s + " " + Thread.currentThread().getName()))
                .map(s -> s + " " + Thread.currentThread().getName())
                .toList();
        return result.size();
    }

    @Benchmark
    @BenchmarkMode(org.openjdk.jmh.annotations.Mode.AverageTime)
    @OutputTimeUnit(MICROSECONDS)
    public int serialThenParallelizablePicky() {
        var result = origin.stream()
                .map(i -> i + " " + Thread.currentThread().getName())
                .gather(map(s -> s + " " + Thread.currentThread().getName()))
                .map(s -> s + " " + Thread.currentThread().getName())
                .toList();
        return result.size();
    }
}

