package com.mastering.lambdas.chapter1;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Comparator.comparing;

public class Demo {

    private static Demo demo = new Demo();
    private List<Integer> intList;

    List<Point> pointList;

    Demo() {
        intList = Arrays.asList(1, 2, 3, 4, 5);
        pointList = new ArrayList<>();
    }

    public void movePoint() {
        PointArrayList pointArrayList = new PointArrayList();
        pointArrayList.add(new Point(1, 2));
        pointArrayList.add(new Point(2, 3));
        pointArrayList.add(new Point(3, 4));
        pointArrayList.forEach(new TranslateByOne());
        pointArrayList.forEach(new PointAction<Point>() {
            public void doForPoint(Point p) {
                p.translate(1, 1);
            }
        });

        List<Point> pointList = Arrays.asList(new Point(1, 2), new Point(2, 3));

        // Loop after Java 5
        for (Point p : pointList) {
            p.translate(1, 1);
        }

        // Loop Before Java 5
        for (Iterator<Point> pointItr = pointList.iterator(); pointItr.hasNext(); ) {
            ((Point) pointItr.next()).translate(1, 1);
        }

        // Loop After Java 8
        pointList.forEach(point -> point.translate(1, 1));

        System.out.println(pointList.stream()
                .map(Point::toString)
                .collect(Collectors.joining(" ; ")));
    }

    public double maxDistance() {
        for (Integer i : intList) {
            pointList.add(new Point(i % 3, i / 3));
        }
        double maxDistance = Double.MIN_VALUE;
        for (Point p : pointList) {
            maxDistance = Math.max(p.distance(1, 10), maxDistance);
        }
        return maxDistance;
    }

    public double maxDistanceJava8() {
        OptionalDouble max = IntStream.range(1, 6)
                .mapToObj(i -> new Point(i % 3, i / 3))
                .peek(p -> p.distance(0, 0))
                .sorted(comparing(Point::getDistance).thenComparing(Point::getX))
                //.sorted((p1, p2) -> Double.compare(p1.getX(), p2.getX()))
                .peek(System.out::println)
                .mapToDouble(p -> p.distance(0, 0))
                .max();
        return max.orElse(Double.MIN_VALUE);
    }

    public static void main(String[] args) {
        demo.movePoint();
        System.out.println(demo.maxDistance());
        System.out.println(demo.maxDistanceJava8());
    }
}