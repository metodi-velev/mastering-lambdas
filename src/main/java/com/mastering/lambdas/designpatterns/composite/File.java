package com.mastering.lambdas.designpatterns.composite;

// Before
/*import java.util.ArrayList;

public class File {
    private String name;

    public File(String name) {
        this.name = name;
    }

    public void ls() {
        System.out.println(CompositeDemo.compositeBuilder + name);
    }
}

class Directory {
    private String name;
    private ArrayList includedFiles = new ArrayList();

    public Directory(String name) {
        this.name = name;
    }

    public void add(Object obj) {
        includedFiles.add(obj);
    }

    public void ls() {
        System.out.println(CompositeDemo.compositeBuilder + name);
        CompositeDemo.compositeBuilder.append("   ");
        for (Object obj : includedFiles) {
            // Recover the type of this object
            String name = obj.getClass().getSimpleName();
            if (name.equals("Directory")) {
                ((Directory) obj).ls();
            } else {
                ((File) obj).ls();
            }
        }
        CompositeDemo.compositeBuilder.setLength(CompositeDemo.compositeBuilder.length() - 3);
    }
}

class CompositeDemo {
    public static StringBuilder compositeBuilder = new StringBuilder();

    public static void main(String[] args) {
        Directory music = new Directory("MUSIC");
        Directory scorpions = new Directory("SCORPIONS");
        Directory dio = new Directory("DIO");
        File track1 = new File("Don't wary, be happy.mp3");
        File track2 = new File("track2.m3u");
        File track3 = new File("Wind of change.mp3");
        File track4 = new File("Big city night.mp3");
        File track5 = new File("Rainbow in the dark.mp3");
        music.add(track1);
        music.add(scorpions);
        music.add(track2);
        scorpions.add(track3);
        scorpions.add(track4);
        scorpions.add(dio);
        dio.add(track5);
        music.ls();
    }
} */

import java.util.ArrayList;

// After
// Define a "lowest common denominator"
interface AbstractFile {
    void ls();
}

// File implements the "lowest common denominator"
class File implements AbstractFile {
    private String name;

    public File(String name) {
        this.name = name;
    }

    public void ls() {
        System.out.println(CompositeDemo.compositeBuilder + name);
    }
}

// Directory implements the "lowest common denominator"
class Directory implements AbstractFile {
    private String name;
    private ArrayList<AbstractFile> includedFiles = new ArrayList<>();

    public Directory(String name) {
        this.name = name;
    }

    public void add(AbstractFile obj) {
        includedFiles.add(obj);
    }

    public void ls() {
        System.out.println(CompositeDemo.compositeBuilder + name);
        CompositeDemo.compositeBuilder.append("   ");
        for (AbstractFile includedFile : includedFiles) {
            // Leverage the "lowest common denominator"
            includedFile.ls();
        }
        CompositeDemo.compositeBuilder.setLength(CompositeDemo.compositeBuilder.length() - 3);
    }
}

class CompositeDemo {
    public static StringBuffer compositeBuilder = new StringBuffer();

    public static void main(String[] args) {
        Directory music = new Directory("MUSIC");
        Directory scorpions = new Directory("SCORPIONS");
        Directory dio = new Directory("DIO");
        File track1 = new File("Don't wary, be happy.mp3");
        File track2 = new File("track2.m3u");
        File track3 = new File("Wind of change.mp3");
        File track4 = new File("Big city night.mp3");
        File track5 = new File("Rainbow in the dark.mp3");
        music.add(track1);
        music.add(scorpions);
        music.add(track2);
        scorpions.add(track3);
        scorpions.add(track4);
        scorpions.add(dio);
        dio.add(track5);
        music.ls();
    }
}
