package com.examscheduler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * CLASS: Student (Subclass of Person)
 * ============================================================================
 * WHAT IT DOES:
 *   Represents a college student who appears for examinations.
 *   Keeps track of PRN, division, semester, department, and enrolled courses.
 *
 * WHY IT EXISTS (OOP CONCEPTS):
 *   1. INHERITANCE: Inherits id, name, and age from 'Person' using 'extends'.
 *   2. SUPER KEYWORD: Calls the parent class constructor using 'super(...)'.
 *   3. POLYMORPHISM (Method Overriding): Overrides 'displayDetails()' to show student info.
 *   4. ENCAPSULATION: Enrolled courses and student data are kept private.
 * ============================================================================
 */
public class Student extends Person {

    // ------------------------------------------------------------------------
    // 1. SPECIFIC STUDENT ATTRIBUTES
    // ------------------------------------------------------------------------
    private String prn;                       // Permanent Registration Number (e.g., "101")
    private String division;                  // Class division (e.g., "A", "B")
    private int semester;                     // Current semester (e.g., 3)
    private String department;                // Department name (e.g., "Information Technology")
    private List<Course> registeredCourses;   // List of subjects this student is enrolled in

    // ------------------------------------------------------------------------
    // 2. CONSTRUCTORS (Default, Parameterized, Copy)
    // ------------------------------------------------------------------------

    /**
     * A) DEFAULT CONSTRUCTOR
     * Invokes parent default constructor with super() and sets student defaults.
     */
    public Student() {
        super(); // Calls Person()
        this.prn = "PRN0000";
        this.division = "A";
        this.semester = 1;
        this.department = "General";
        this.registeredCourses = new ArrayList<>();
    }

    /**
     * B) PARAMETERIZED CONSTRUCTOR
     * Passes personal info to parent Person class via super(id, name, age)
     * and sets student-specific fields using 'this'.
     */
    public Student(String prn, String name, int age, String division, int semester, String department) {
        super(prn, name, age); // Pass PRN as the ID to Person
        this.prn = prn;
        this.division = division;
        this.semester = semester;
        this.department = department;
        this.registeredCourses = new ArrayList<>();
    }

    /**
     * C) COPY CONSTRUCTOR
     * Creates a new Student from an existing Student, creating a fresh course list.
     */
    public Student(Student other) {
        super(other); // Copies parent fields
        if (other != null) {
            this.prn = other.prn;
            this.division = other.division;
            this.semester = other.semester;
            this.department = other.department;
            // Create a fresh list copy to preserve encapsulation
            this.registeredCourses = new ArrayList<>(other.registeredCourses);
        }
    }

    // ------------------------------------------------------------------------
    // 3. COURSE MANAGEMENT METHODS
    // ------------------------------------------------------------------------

    /**
     * Enrolls the student in an academic course.
     */
    public void enrollCourse(Course course) {
        if (course != null && !registeredCourses.contains(course)) {
            this.registeredCourses.add(course);
        }
    }

    /**
     * Helper method to verify if this student takes a specific course code.
     * Used by TimetableGenerator to detect student clashes between two courses.
     */
    public boolean isEnrolledIn(String courseCode) {
        for (Course c : registeredCourses) {
            if (c.getCode().equalsIgnoreCase(courseCode)) {
                return true; // Match found
            }
        }
        return false; // Student does not take this course
    }

    // ------------------------------------------------------------------------
    // 4. OVERRIDDEN ABSTRACT METHOD (Runtime Polymorphism)
    // ------------------------------------------------------------------------

    /**
     * Provides the concrete implementation of Person's abstract displayDetails().
     */
    @Override
    public void displayDetails() {
        System.out.println("----- STUDENT PROFILE -----");
        System.out.println("PRN        : " + prn);
        System.out.println("Name       : " + name);
        System.out.println("Age        : " + age);
        System.out.println("Department : " + department + " (Sem " + semester + ", Div " + division + ")");
        System.out.print("Enrolled In: ");
        if (registeredCourses.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < registeredCourses.size(); i++) {
                System.out.print(registeredCourses.get(i).getCode() + (i == registeredCourses.size() - 1 ? "" : ", "));
            }
            System.out.println();
        }
    }

    // ------------------------------------------------------------------------
    // 5. GETTERS AND SETTERS
    // ------------------------------------------------------------------------

    public String getPrn() {
        return prn;
    }

    public void setPrn(String prn) {
        this.prn = prn;
        this.id = prn; // Keep parent ID matching
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Course> getRegisteredCourses() {
        return registeredCourses;
    }

    @Override
    public String toString() {
        return "Student [PRN=" + prn + ", Name=" + name + ", Division=" + division + "]";
    }
}
