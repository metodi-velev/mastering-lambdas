package com.mastering.lambdas.designpatterns.composite;

import jakarta.validation.constraints.NotNull;

import java.util.*;
import java.util.function.Consumer;


interface SomeNeurons extends Iterable<Neuron> {
    default void connectTo(SomeNeurons other) {
        if (this == other) return;
        for (Neuron from : this)
            for (Neuron to : other) {
                from.out.add(to);
                to.in.add(from);
            }
    }
}

public class Neuron implements SomeNeurons {
    public ArrayList<Neuron> in, out;

    @NotNull
    @Override
    public Iterator<Neuron> iterator() {
        return Collections.singleton(this).iterator();
    }

    @Override
    public void forEach(Consumer<? super Neuron> action) {
        action.accept(this);
    }

    @Override
    public Spliterator<Neuron> spliterator() {
        return Collections.singleton(this).spliterator();
    }
}

class NeuronLayer extends ArrayList<Neuron> implements SomeNeurons {

}

class Demo {
    public static void main(String[] args) {
        Neuron neuron = new Neuron();
        Neuron neuron2 = new Neuron();
        NeuronLayer layer = new NeuronLayer();
        NeuronLayer layer2 = new NeuronLayer();
    }
}

class Main {
    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
                new Student(1, 3, "John"),
                new Student(2, 4, "Jane"),
                new Student(3, 3, "Jack"));

        Consumer<Student> raiser = e -> {
            e.gpa = e.gpa * 1.1;
        };

        raiseStudents(students, System.out::println);
        raiseStudents(students, raiser.andThen(System.out::println));
    }

    private static void raiseStudents(List<Student> employees,
                                      Consumer<Student> fx) {
        for (Student e : employees) {
            fx.accept(e);
        }
    }

}

class Student {
    public int id;
    public double gpa;
    public String name;

    Student(int id, long g, String name) {
        this.id = id;
        this.gpa = g;
        this.name = name;
    }

    @Override
    public String toString() {
        return id + ">" + name + ": " + gpa;
    }
}
