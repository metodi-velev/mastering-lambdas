package com.mastering.lambdas.designpatterns.chainofresponsibility;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

abstract class HomeChecker {
    protected HomeChecker successor;

    public abstract void check(HomeStatus home) throws Exception;

    public void setSuccessor(HomeChecker successor) {
        this.successor = successor;
    }

    public void next(HomeStatus homeStatus) throws Exception {
        if (this.successor != null) {
            this.successor.check(homeStatus);
        }
    }
}

class Locks extends HomeChecker {
    public void check(HomeStatus homeStatus) throws Exception {
        if (!homeStatus.locked) {
            throw new Exception("The doors are not locked!! Abort, abort.");
        }
        System.out.println("The doors are locked.");
        this.next(homeStatus);
    }
}

class Lights extends HomeChecker {
    public void check(HomeStatus homeStatus) throws Exception {
        if (!homeStatus.lightsOff) {
            throw new Exception("The lights are still on!! Abort, abort.");
        }
        System.out.println("The lights are off.");
        this.next(homeStatus);
    }
}

class Alarm extends HomeChecker {
    public void check(HomeStatus homeStatus) throws Exception {
        if (!homeStatus.alarmOn) {
            throw new Exception("The alarm has not been set!! Abort, abort.");
        }
        System.out.println("The alarm has been set.");
        this.next(homeStatus);
    }
}

class HomeStatus {
    public boolean locked = true;
    public boolean lightsOff = true;
    public boolean alarmOn = true;
}

class App {
    public static void main(String[] args) throws Exception {

        Locks locks = new Locks();
        Lights lights = new Lights();
        Alarm alarm = new Alarm();

        locks.setSuccessor(lights);
        lights.setSuccessor(alarm);
        locks.check(new HomeStatus());
    }
}

class CreateFile {
    protected void writeToPosition(String filename, int data, long position)
            throws IOException {
        try (RandomAccessFile writer = new RandomAccessFile(filename, "rw")) {
            writer.seek(position);
            writer.writeInt(data);
        }
    }

    protected int readFromPosition(String filename, long position)
            throws IOException {
        int result;
        try (RandomAccessFile reader = new RandomAccessFile(filename, "r")) {
            reader.seek(position);
            result = reader.readInt();
        }
        return result;
    }

    public static void main(String[] args) {
/*        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\metod\\Desktop\\filename1.txt", true))) {
            writer.write("Metodi Dimitrov Velev");
        } catch (IOException ex) {
            ex.getMessage();
        }*/
        String value = null;
        String result = null;
        String valueBG = null;
        Path path = Paths.get("C:\\Users\\metod\\Desktop\\filename1.txt");
        try (DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(path)))) {
            value = "Metodi Dimitrov Velev and Neno Miroslavov Dimitrov";
            valueBG = "Методи Димитров Велев и Нено Мирославов Димитров";
            outStream.writeUTF(valueBG);
        } catch (IOException ex) {
            String msg = ex.getMessage();
            System.out.println(msg);
        }
        try (DataInputStream reader = new DataInputStream(new BufferedInputStream(Files.newInputStream(path)))) {
            result = reader.readUTF();
        } catch (IOException ex) {
            String msg = ex.getMessage();
            System.out.println(msg);
        }
        System.out.println(valueBG);
        System.out.println(result);
        System.out.println(Objects.requireNonNull(valueBG).equals(result));
    }
}