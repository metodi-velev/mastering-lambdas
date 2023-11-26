package com.mastering.lambdas.designpatterns.adapter;

public class Book implements BookInterface {
    public void open() {
        System.out.println("Opening the paper book.");
    }

    public void turnPage() {
        System.out.println("Turning the page of the paper book.");
    }
}

class Kindle implements EReaderInterface {
    public void turnOn() {
        System.out.println("Turn the Kindle on.");
    }

    public void pressNextButton() {
        System.out.println("Press the Next button on the Kindle.");
    }
}

class Nook implements EReaderInterface {
    public void turnOn() {
        System.out.println("Turn the Nook on.");
    }

    public void pressNextButton() {
        System.out.println("Press the Next button on the Nook.");
    }
}

class EReaderAdapter implements BookInterface {

    private EReaderInterface reader;

    public EReaderAdapter(EReaderInterface reader) {
        this.reader = reader;
    }

    public void open() {
        this.reader.turnOn();
    }

    public void turnPage() {
        reader.pressNextButton();
    }
}

//We want our Person to be able to read a Kindle
//without changing our client code even one bit
class Person {

    public static void main(String[] args) {
        //new Person().read(new Book());
        new Person().read(new EReaderAdapter(new Nook()));
    }

    public void read(BookInterface book) {
        book.open();
        book.turnPage();
    }
}
