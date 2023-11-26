package com.mastering.lambdas.designpatterns.strategy;

// encapsulate and make them interchangeable
interface Logger {
    void log(String data);
}

// Define a family of algorithms
public class LogToFile implements Logger {
    public void log(String data) {
        System.out.println("Log the data to a file: " + data);
    }
}

class LogToDatabase implements Logger {
    public void log(String data) {
        System.out.println("Log the data to the database: " + data);
    }
}

class LogToXWebService implements Logger {
    public void log(String data) {
        System.out.println("Log the data to a Saas site: " + data);
    }
}

class App {
    /*    public void log(String data) {
            Logger logger = new LogToFile();
            logger.log(data);
        }*/
    public void log(String data, Logger logger) {
        logger = logger == null ? new LogToFile() : logger;
        logger.log(data);
    }

    public static void main(String[] args) {
        App app = new App();
        app.log("Some information here", new LogToXWebService());
    }
}