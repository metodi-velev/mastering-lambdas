package com.mastering.lambdas.designpatterns.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphicObject {
    protected String name = "Group";
    protected String color;
    public List<GraphicObject> children = new ArrayList<>();

    public GraphicObject() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void print(StringBuilder stringBuilder, int depth) {
        stringBuilder.append(String.join("", Collections.nCopies(depth, "*")))
                .append(depth > 0 ? " " : "")
                .append((color == null || color.isEmpty()) ? "" : color + " ")
                .append(getName())
                .append(System.lineSeparator());
        for (GraphicObject child : children)
            child.print(stringBuilder, depth + 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        print(sb, 0);
        return sb.toString();
    }
}

class Circle extends GraphicObject {
    public Circle(String color) {
        this.name = "Circle";
        this.color = color;
    }
}

class Square extends GraphicObject {
    public Square(String color) {
        this.name = "Square";
        this.color = color;
    }
}

class Demo1 {
    public static void main(String[] args) {
        GraphicObject drawing = new GraphicObject();
        drawing.setName("My Drawing");
        drawing.children.add(new Square("Red"));
        drawing.children.add(new Circle("Yellow"));

        GraphicObject group = new GraphicObject();
        group.children.add(new Circle("Blue"));
        group.children.add(new Square("Blue"));
        drawing.children.add(group);

        GraphicObject nested = new GraphicObject();
        nested.children.add(new Circle("Pink"));
        nested.children.add(new Square("Pink"));
        group.children.add(nested);

        GraphicObject nested2 = new GraphicObject();
        nested2.children.add(new Circle("Purple"));
        nested2.children.add(new Square("Purple"));
        nested2.children.add(new Square("Purple"));
        nested.children.add(nested2);

        GraphicObject nested3 = new GraphicObject();
        nested3.children.add(new Circle("Magenta"));
        nested3.children.add(new Square("Magenta"));
        nested3.children.add(new Square("Magenta"));
        nested2.children.add(nested3);

        System.out.println(drawing);
        System.out.println(Collections.nCopies(11, "+"));
    }
}