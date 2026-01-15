package scot.jalba.streams.demos;

import java.util.stream.IntStream;
import java.util.stream.Stream;

class Surprise {
    void main() {
        var result1 = IntStream.range(0, 10)
                .boxed()
                .peek(IO::println)
                .map(i -> i * i )
                .count();
        IO.println("result1 = " + result1);

        // https://daniel.avery.io/writing/the-java-streams-parallel
        var result2 = IntStream.range(0, 10)
                .boxed()
                .parallel()
                .unordered()
                .flatMap(i -> IntStream.range(0, i).boxed().peek(IO::println))
                .limit(10).toList();
        IO.println("result2 = " + result2);

        // https://bugs.openjdk.org/browse/JDK-8277306
        var first = Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
                .sorted()
                .peek(IO::println);
        var second = Stream.concat(first, Stream.of());

        var result3 = second.findFirst().orElseThrow();
        IO.println("result3 = " + result3);

        // But:
        first = IntStream.range(0, 10)
                .boxed()
                .sorted()
                .peek(IO::println);
        second = Stream.concat(first, Stream.of());

        var result4 = second.findFirst().orElseThrow();
        IO.println("result4 = " + result3);

    }
}
