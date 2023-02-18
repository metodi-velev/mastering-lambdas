package com.mastering.lambdas.chapter1;

import java.util.ArrayList;

public class PointArrayList extends ArrayList<Point> {
    public void forEach(PointAction<Point> t) {
        for (Point p : this) {
            t.doForPoint(p);
        }
    }
}
