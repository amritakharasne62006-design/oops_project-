package com.examscheduler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * CLASS: Exam
 * ============================================================================
 * WHAT IT DOES:
 *   Represents a scheduled exam occurrence.
 *   Links together:
 *     - The Course being tested
 *     - The TimeSlot when it takes place
 *     - The Rooms assigned to host it
 *     - The Students registered to write it
 * ============================================================================
 */
public class Exam {

    // ------------------------------------------------------------------------
    // 1. ATTRIBUTES
    // ------------------------------------------------------------------------
    private Course course;                   // The subject of this exam
    private TimeSlot timeSlot;               // Assigned date & time
    private List<Room> assignedRooms;        // List of rooms allocated for this exam
    private List<Student> eligibleStudents;  // Students enrolled in this exam

    // ------------------------------------------------------------------------
    // 2. CONSTRUCTORS
    // ------------------------------------------------------------------------

    public Exam(Course course, TimeSlot timeSlot) {
        this.course = course;
        this.timeSlot = timeSlot;
        this.assignedRooms = new ArrayList<>();
        this.eligibleStudents = new ArrayList<>();
    }

    // ------------------------------------------------------------------------
    // 3. METHODS
    // ------------------------------------------------------------------------

    public void addRoom(Room room) {
        if (room != null && !assignedRooms.contains(room)) {
            assignedRooms.add(room);
        }
    }

    public void addStudent(Student student) {
        if (student != null && !eligibleStudents.contains(student)) {
            eligibleStudents.add(student);
        }
    }

    // ------------------------------------------------------------------------
    // 4. GETTERS AND SETTERS
    // ------------------------------------------------------------------------

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public List<Room> getAssignedRooms() {
        return assignedRooms;
    }

    public List<Student> getEligibleStudents() {
        return eligibleStudents;
    }

    public void setEligibleStudents(List<Student> students) {
        this.eligibleStudents = students;
    }

    @Override
    public String toString() {
        return "Exam: " + (course != null ? course.getCode() : "N/A") + " on " + (timeSlot != null ? timeSlot.toString() : "TBD");
    }
}
