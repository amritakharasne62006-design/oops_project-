package com.examscheduler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a teacher or invigilator.
 * Extends Person and tracks invigilation duty count for fair scheduling.
 */
public class Faculty extends Person implements Comparable<Faculty> {

    private String department;
    private List<String> leaveDates; // Dates faculty is unavailable (YYYY-MM-DD)
    private int dutyCount;          // Number of duties assigned so far

    /**
     * Default constructor.
     */
    public Faculty() {
        super();
        this.department = "Information Technology";
        this.leaveDates = new ArrayList<>();
        this.dutyCount = 0;
    }

    /**
     * Parameterized constructor.
     */
    public Faculty(String id, String name, int age, String department) {
        super(id, name, age);
        this.department = department;
        this.leaveDates = new ArrayList<>();
        this.dutyCount = 0;
    }

    /**
     * Copy constructor.
     */
    public Faculty(Faculty other) {
        super(other);
        if (other != null) {
            this.department = other.department;
            this.leaveDates = new ArrayList<>(other.leaveDates);
            this.dutyCount = other.dutyCount;
        }
    }

    /**
     * Adds an approved leave date.
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
     * Resets duty count back to zero.
     */
    public void resetDuty() {
        this.dutyCount = 0;
    }

    /**
     * Compares two faculty members based on duty count.
     * Needed for PriorityQueue ordering (fewest duties first).
     */
    @Override
    public int compareTo(Faculty other) {
        if (other == null) return -1;
        return Integer.compare(this.dutyCount, other.dutyCount);
    }

    /**
     * Overrides abstract displayDetails() method.
     */
    @Override
    public void displayDetails() {
        System.out.println("----- FACULTY DETAILS -----");
        System.out.println("Faculty ID : " + id);
        System.out.println("Name       : " + name);
        System.out.println("Department : " + department);
        System.out.println("Duties Done: " + dutyCount);
        System.out.println("Leaves     : " + (leaveDates.isEmpty() ? "None" : leaveDates.toString()));
    }

    // Getters and Setters
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
        return "Faculty[" + id + ", " + name + ", Duties: " + dutyCount + "]";
    }
}
