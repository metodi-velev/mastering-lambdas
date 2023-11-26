package com.mastering.lambdas.designpatterns.observer;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Jeffrey Way
 */
interface Subject {
    Subject attach(Object observable);

    void detach(Observer observer);

    void notifyObservers();
}  //Publisher

interface Observer {
    void handle();
}  //Subscriber or Listener

class LogHandler implements Observer {

    @Override
    public void handle() {
        System.out.println("Log something important.");
    }
}

class EmailHandler implements Observer {

    @Override
    public void handle() {
        System.out.println("Fire off an email.");
    }
}

class LoginReporter implements Observer {

    @Override
    public void handle() {
        System.out.println("Do some form of reporting.");
    }
}

class Login implements Subject {

    protected List<Observer> observers = new ArrayList<>();

    @Override
    public Subject attach(Object observable) {
        if (observable instanceof List) {
            return attachObservers((List<?>) observable);
        }
        observers.add((Observer) observable);
        return this;
    }

    @NotNull
    private Login attachObservers(List<?> observable) {
        observable.forEach(o -> {
            if (!(o instanceof Observer))
                throw new RuntimeException(o + " is not an observer.");
            attach(o);
        });
        return this;
    }

    private void fire() {
        // perform the login
        this.notifyObservers();
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Filter through all the observers and trigger them
     */
    @Override
    public void notifyObservers() {
        observers.forEach(Observer::handle);
    }

    public static void main(String[] args) {
        List<Object> observables = Arrays.asList(new LogHandler(), new EmailHandler(), new LoginReporter());
        Login login = new Login();
        //login.attach(new LogHandler()).attach(new EmailHandler());
        login.attach(observables);
        login.fire();
    }
}
