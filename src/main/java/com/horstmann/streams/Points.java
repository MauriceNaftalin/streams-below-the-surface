package com.horstmann.streams;

import java.awt.*;
import java.util.random.RandomGenerator;
import java.util.stream.Gatherers;

public class Points {
    // Are both x and y short (between -32768 and 32767)?
    public static boolean isShort(Point p) { return p.x instanceof short _ && p.y instanceof short _; }
    // Avoiding a square root here. This is safe for short points.
    public static int lengthSquared(Point p) { return p.x * p.x + p.y * p.y; }
    // An array of random points, with x and y between -65536 and 65535 (25% chance of being short)
    public static Point[] randomArray(int count) {
        return RandomGenerator.getDefault().ints(2 * Short.MIN_VALUE, 2 * Short.MAX_VALUE + 1)
                .boxed()
                .gather(Gatherers.windowFixed(2))
                .map(p -> new Point(p.get(0), p.get(1)))
                .limit(count)
                .toArray(Point[]::new);
    }
}
