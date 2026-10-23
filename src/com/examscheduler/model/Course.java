package com.examscheduler.model;

import java.util.Objects;

/**
 * Class representing an academic course/subject.
 * Demonstrates Encapsulation and equals/hashCode for Graph Colouring node comparisons.
 */
public class Course {

    private String code;         // E.g. "CS201"
    private String name;         // E.g. "Object Oriented Programming"
    private int durationMinutes; // E.g. 120 (2 hours)
    private String examType;     // "Theory" or "Practical"

    /**
     * Default constructor.
     */
    public Course() {
        this("CRS000", "Generic Subject", 120, "Theory");
    }

    /**
     * Parameterized constructor.
     */
    public Course(String code, String name, int durationMinutes, String examType) {
        this.code = code;
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.examType = examType;
    }

    /**
     * Copy constructor.
     */
    public Course(Course other) {
        if (other != null) {
            this.code = other.code;
            this.name = other.name;
            this.durationMinutes = other.durationMinutes;
            this.examType = other.examType;
        }
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(code, course.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return code + " - " + name + " (" + durationMinutes + " mins)";
    }
}
