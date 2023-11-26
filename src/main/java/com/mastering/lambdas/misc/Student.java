package com.mastering.lambdas.misc;

// Java Program to Demonstrate TreeMap
// using Comparator Constructor

// Importing required classes

import java.util.Comparator;
import java.util.TreeMap;

// Class 1
// Helper class representing Student
public class Student {

    // Attributes of a student
    int rollno;
    String name, address;

    // Constructor
    public Student(int rollno, String name, String address) {

        // This keyword refers to current object itself
        this.rollno = rollno;
        this.name = name;
        this.address = address;
    }

    // Method of this class
    // To print student details
    public String toString() {
        return this.rollno + " " + this.name + " "
                + this.address;
    }
}

// Class 2
// Helper class - Comparator implementation
class Sortbyroll implements Comparator<Student> {

    // Used for sorting in ascending order of
    // roll number
    public int compare(Student a, Student b) {
        return a.rollno - b.rollno;
    }
}

// Class 3
// Main class
class GFG {

    private static Comparator<Student> compareStudents = Comparator.comparingInt(s -> s.rollno);

    // Calling constructor inside main()
    static void Example2ndConstructor() {
        // Creating an empty TreeMap
        TreeMap<Student, Integer> tree_map = new TreeMap<Student, Integer>(compareStudents);

        // Mapping string values to int keys
        tree_map.put(new Student(111, "bbbb", "london"), 2);
        tree_map.put(new Student(131, "aaaa", "nyc"), 3);
        tree_map.put(new Student(121, "cccc", "jaipur"), 1);

        // Printing the elements of TreeMap
        System.out.println("TreeMap: " + tree_map);
    }

    // Main driver method
    public static void main(String[] args) {

        System.out.println("TreeMap using "
                + "TreeMap(Comparator)"
                + " constructor:\n");
        Example2ndConstructor();
    }
}
