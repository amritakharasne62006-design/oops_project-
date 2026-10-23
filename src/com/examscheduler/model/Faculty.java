package com.examscheduler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * CLASS: Faculty (Subclass of Person)
 * ============================================================================
 * WHAT IT DOES:
 *   Represents a teacher or invigilator appointed to monitor exam rooms.
 *   Tracks assigned duties and leave dates to ensure fair and clash-free allocation.
 *
 * WHY IT EXISTS (OOP CONCEPTS):
 *   1. INHERITANCE: Inherits id, name, and age from 'Person'.
 *   2. INTERFACES: Implements 'Comparable<Faculty>' for automatic sorting in a PriorityQueue.
 *   3. POLYMORPHISM: Overrides 'compareTo()' and 'displayDetails()'.
 *   4. FAIRNESS LOGIC: Keeps a dutyCount counter so duties are evenly balanced.
 * ============================================================================
 */
public class Faculty extends Person implements Comparable<Faculty> {

    // ------------------------------------------------------------------------
    // 1. FACULTY ATTRIBUTES
    // ------------------------------------------------------------------------
    private String department;       // Faculty department (e.g., "Information Technology")
    private List<String> leaveDates; // Dates when faculty is on approved leave (YYYY-MM-DD)
    private int dutyCount;          // Total invigilation duties assigned so far

    // ------------------------------------------------------------------------
    // 2. CONSTRUCTORS
    // ------------------------------------------------------------------------

    /**
     * A) DEFAULT CONSTRUCTOR
     */
    public Faculty() {
        super();
        this.department = "Information Technology";
        this.leaveDates = new ArrayList<>();
        this.dutyCount = 0;
    }

    /**
     * B) PARAMETERIZED CONSTRUCTOR
     */
    public Faculty(String id, String name, int age, String department) {
        super(id, name, age);
        this.department = department;
        this.leaveDates = new ArrayList<>();
        this.dutyCount = 0;
    }

    /**
     * C) COPY CONSTRUCTOR
     */
    public Faculty(Faculty other) {
        super(other);
        if (other != null) {
            this.department = other.department;
            this.leaveDates = new ArrayList<>(other.leaveDates);
            this.dutyCount = other.dutyCount;
        }
    }

    // ------------------------------------------------------------------------
    // 3. DUTY & LEAVE METHODS
    // ------------------------------------------------------------------------

    /**
     * Records a date when this faculty member cannot attend invigilation duties.
     */
    public void addLeaveDate(String dateStr) {
        if (dateStr != null && !leaveDates.contains(dateStr)) {
            leaveDates.add(dateStr);
        }
    }

    /**
     * Checks if faculty is on leave on a given date string.
     */
    public boolean isOnLeave(String dateStr) {
        return leaveDates.contains(dateStr);
    }

    /**
     * Increments duty count by 1 after an assignment.
     */
    public void incrementDuty() {
        this.dutyCount++;
    }

    /**
     * Resets duty count back to zero (for fresh timetable runs).
     */
    public void resetDuty() {
        this.dutyCount = 0;
    }

    // ------------------------------------------------------------------------
    // 4. COMPARABLE IMPLEMENTATION (For PriorityQueue)
    // ------------------------------------------------------------------------

    /**
     * Compares two faculty members based on duty count.
     * The PriorityQueue will always pick the teacher with the lowest duty count first.
     */
    @Override
    public int compareTo(Faculty other) {
        if (other == null) return -1;
        return Integer.compare(this.dutyCount, other.dutyCount);
    }

    // ------------------------------------------------------------------------
    // 5. OVERRIDDEN ABSTRACT METHOD
    // ------------------------------------------------------------------------
    @Override
    public void displayDetails() {
        System.out.println("----- FACULTY PROFILE -----");
        System.out.println("Faculty ID : " + id);
        System.out.println("Name       : " + name);
        System.out.println("Department : " + department);
        System.out.println("Duties Done: " + dutyCount);
        System.out.println("Leave Dates: " + (leaveDates.isEmpty() ? "None" : leaveDates.toString()));
    }

    // ------------------------------------------------------------------------
    // 6. GETTERS AND SETTERS
    // ------------------------------------------------------------------------

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getLeaveDates() {
        return leaveDates;
    }

    public int getDutyCount() {
        return dutyCount;
    }

    public void setDutyCount(int dutyCount) {
        this.dutyCount = dutyCount;
    }

    @Override
    public String toString() {
        return "Faculty [" + id + " - " + name + " | Duties: " + dutyCount + "]";
    }
}
