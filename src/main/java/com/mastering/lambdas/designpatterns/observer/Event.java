package com.mastering.lambdas.designpatterns.observer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Event<TArgs> {
    private int count = 0;
    private Map<Integer, Consumer<TArgs>> handlers = new HashMap<>();

    public Subscription addHandler(Consumer<TArgs> handler) {
        int i = count;
        handlers.put(count++, handler);
        return new Subscription(this, i);
    }

    public void fire(TArgs args) {
        handlers.values().forEach(v -> v.accept(args));
    }

    public class Subscription implements AutoCloseable {
        private Event<TArgs> event;
        private int id;

        public Subscription(Event<TArgs> event, int id) {
            this.event = event;
            this.id = id;
        }

        @Override
        public void close() throws Exception {
            event.handlers.remove(id);
        }
    }
}

class PropertyChangedEventArgsV2 {
    public Object source;
    public String propertyName;

    public PropertyChangedEventArgsV2(Object source, String propertyName) {
        this.source = source;
        this.propertyName = propertyName;
    }
}

class PersonV2 {
    public Event<PropertyChangedEventArgsV2> propertyChanged = new Event<>();
    private int age;
    private boolean canVote;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (this.age == age) return;
        boolean oldCanVote = getCanVote();
        this.age = age;
        propertyChanged.fire(new PropertyChangedEventArgsV2(this, "age"));
        if (oldCanVote != getCanVote()) {
            this.canVote = getCanVote();
            propertyChanged.fire(new PropertyChangedEventArgsV2(this, "canVote"));
        }
    }

    public boolean getCanVote() {
        return age >= 18;
    }
}

class DemoV2 {
    public static void main(String[] args) throws Exception {
        PersonV2 person = new PersonV2();
        Event<PropertyChangedEventArgsV2>.Subscription subscription =
                person.propertyChanged.addHandler(x -> System.out.println("Person's " + x.propertyName + " has changed"));
        person.setAge(17);
        person.setAge(18);
        subscription.close();
        person.setAge(19);
    }
}
