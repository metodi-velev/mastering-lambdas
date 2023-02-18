package com.mastering.lambdas.chapter1;

class TranslateByOne implements PointAction<Point> {
    public void doForPoint(Point p) {
        p.translate(1, 1);
    }
}
