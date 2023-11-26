package com.mastering.lambdas.designpatterns.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

class PropertyChangedEventArgs<T> {
    public T source;
    public String propertyName;
    public Object newValue;

    public PropertyChangedEventArgs(T source, String propertyName, Object newValue) {
        this.source = source;
        this.propertyName = propertyName;
        this.newValue = newValue;
    }
}

interface ObserverV2<T> {
    void handle(PropertyChangedEventArgs<T> args);
}

class Observable<T> {
    private List<ObserverV2<T>> observers = new ArrayList<>();

    public void subscribe(ObserverV2<T> observer) {
        observers.add(observer);
    }

    protected void propertyChanged(T source,
                                   String propertyName,
                                   Object newValue) {
        observers.forEach(o -> o.handle(new PropertyChangedEventArgs<>(source, propertyName, newValue)));
    }
}

class Person extends Observable<Person> {
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (this.age == age) return;
        this.age = age;
        propertyChanged(this, "age", age);
    }
}

class Demo implements ObserverV2<Person> {
    @Override
    public void handle(PropertyChangedEventArgs<Person> args) {
        System.out.println("Person's " + args.propertyName
                + " has changed to " + args.newValue);
    }

    public static void main(String[] args) {
        new Demo();
    }

    public Demo() {
        Person person = new Person();
        person.subscribe(this);
        IntStream.range(20, 24).forEach(person::setAge);
    }
}


