package com.examscheduler.model;

import java.util.Objects;

/**
 * ============================================================================
 * CLASS: Course
 * ============================================================================
 * WHAT IT DOES:
 *   Represents an academic subject/course to be examined (e.g., CS201 - Java).
 *
 * WHY IT EXISTS (OOP CONCEPTS):
 *   1. ENCAPSULATION: Hides course data behind private attributes and public getters.
 *   2. CONSTRUCTOR OVERLOADING: Default, Parameterized, and Copy constructors.
 *   3. EQUALS & HASHCODE: Essential for Graph Colouring node keys in HashMaps.
 * ============================================================================
 */
public class Course {

    // ------------------------------------------------------------------------
    // 1. ATTRIBUTES
    // ------------------------------------------------------------------------
    private String code;         // Course Code (e.g., "CS201")
    private String name;         // Course Name (e.g., "Object Oriented Programming")
    private int durationMinutes; // Duration of exam in minutes (e.g., 120)
    private String examType;     // "Theory" or "Practical"

    // ------------------------------------------------------------------------
    // 2. CONSTRUCTORS
    // ------------------------------------------------------------------------

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

    // ------------------------------------------------------------------------
    // 3. GETTERS AND SETTERS
    // ------------------------------------------------------------------------

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

    // ------------------------------------------------------------------------
    // 4. EQUALS AND HASHCODE (Crucial for Graph Colouring)
    // ------------------------------------------------------------------------
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
        return code + " - " + name + " (" + durationMinutes + " mins, " + examType + ")";
    }
}
