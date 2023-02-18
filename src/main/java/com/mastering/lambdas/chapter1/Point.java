package com.mastering.lambdas.chapter1;

import lombok.Getter;

@Getter
public class Point {
    int x;
    int y;
    int distance;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void translate(int ix, int iy) {
        this.x += ix;
        this.y += iy;
    }

    public double distance(int xValue, int yValue) {
        this.x += xValue;
        this.y += yValue;
        this.distance += (this.y + this.x) + 5;
        return distance;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Point{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", distance=").append(distance);
        sb.append('}');
        return sb.toString();
    }
}
