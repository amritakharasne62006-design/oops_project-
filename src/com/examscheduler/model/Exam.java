package com.examscheduler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a scheduled exam instance.
 * Connects a Course, an assigned TimeSlot, and allocated Rooms.
 */
public class Exam {

    private Course course;
    private TimeSlot timeSlot;
    private List<Room> assignedRooms;
    private List<Student> eligibleStudents;

    /**
     * Parameterized constructor.
     */
    public Exam(Course course, TimeSlot timeSlot) {
        this.course = course;
        this.timeSlot = timeSlot;
        this.assignedRooms = new ArrayList<>();
        this.eligibleStudents = new ArrayList<>();
    }

    /**
     * Adds an allocated room for this exam.
     */
    public void addRoom(Room room) {
        if (room != null && !assignedRooms.contains(room)) {
            assignedRooms.add(room);
        }
    }

    /**
     * Adds an eligible student taking this exam.
     */
    public void addStudent(Student student) {
        if (student != null && !eligibleStudents.contains(student)) {
            eligibleStudents.add(student);
        }
    }

    // Getters and Setters
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
