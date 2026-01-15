package scot.jalba.streams.demos;

import module java.base;
import module java.desktop;
import scot.jalba.streams.Points;

class FilterMapDemo {
    int POINTS = 100_000_000;

    int largestDistance(Point[] points) {
        return Stream.of(points)
            .filter(Points::isShort)
            .map(Points::lengthSquared)
            .max(Integer::compare)
            .orElseThrow();
    }

    void main(String[] args) {
        int reps = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        Point[] points = Points.randomArray(POINTS / reps);
        for (int i = 0; i < reps; i++) {
            var largest = largestDistance(points);
            if (i == reps - 1) IO.println(largest);
        }
    }
}
