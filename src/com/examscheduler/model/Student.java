package com.examscheduler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an examinee student.
 * Demonstrates Inheritance (extends Person), Encapsulation, and Polymorphism (method overriding).
 */
public class Student extends Person {

    private String prn;               // Permanent Registration Number
    private String division;          // E.g. "A", "B"
    private int semester;             // E.g. 4
    private String department;        // E.g. "Information Technology"
    private List<Course> registeredCourses; // List of enrolled courses

    /**
     * Default constructor.
     * Calls super() and assigns default values.
     */
    public Student() {
        super();
        this.prn = "PRN0000";
        this.division = "A";
        this.semester = 1;
        this.department = "General";
        this.registeredCourses = new ArrayList<>();
    }

    /**
     * Parameterized constructor.
     * Uses super to initialize Person fields and this to initialize Student fields.
     */
    public Student(String prn, String name, int age, String division, int semester, String department) {
        super(prn, name, age);
        this.prn = prn;
        this.division = division;
        this.semester = semester;
        this.department = department;
        this.registeredCourses = new ArrayList<>();
    }

    /**
     * Copy constructor.
     * Deep-copies student information and course list.
     */
    public Student(Student other) {
        super(other);
        if (other != null) {
            this.prn = other.prn;
            this.division = other.division;
            this.semester = other.semester;
            this.department = other.department;
            this.registeredCourses = new ArrayList<>(other.registeredCourses);
        }
    }

    /**
     * Enrolls the student into a new course.
     */
    public void enrollCourse(Course course) {
        if (course != null && !registeredCourses.contains(course)) {
            this.registeredCourses.add(course);
        }
    }

    /**
     * Checks if the student is registered for a specific course code.
     */
    public boolean isEnrolledIn(String courseCode) {
        for (Course c : registeredCourses) {
            if (c.getCode().equalsIgnoreCase(courseCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Overrides Person's abstract method displayDetails().
     * Demonstrates dynamic method dispatch (runtime polymorphism).
     */
    @Override
    public void displayDetails() {
        System.out.println("----- STUDENT DETAILS -----");
        System.out.println("PRN        : " + prn);
        System.out.println("Name       : " + name);
        System.out.println("Age        : " + age);
        System.out.println("Department : " + department + " (Sem " + semester + ", Div " + division + ")");
        System.out.print("Courses    : ");
        if (registeredCourses.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < registeredCourses.size(); i++) {
                System.out.print(registeredCourses.get(i).getCode() + (i == registeredCourses.size() - 1 ? "" : ", "));
            }
            System.out.println();
        }
    }

    // Getters and Setters
    public String getPrn() {
        return prn;
    }

    public void setPrn(String prn) {
        this.prn = prn;
        this.id = prn; // keep parent id synchronized
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
        return "Student[PRN=" + prn + ", Name=" + name + ", Div=" + division + "]";
    }
}
