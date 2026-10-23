package com.examscheduler.model;

/**
 * Class representing an invigilation duty assignment.
 * Links a Faculty member to an Exam, a Room, and the associated TimeSlot.
 */
public class InvigilatorDuty {

    private Faculty faculty;
    private Exam exam;
    private String roomNo;
    private TimeSlot timeSlot;

    /**
     * Parameterized constructor.
     */
    public InvigilatorDuty(Faculty faculty, Exam exam, String roomNo, TimeSlot timeSlot) {
        this.faculty = faculty;
        this.exam = exam;
        this.roomNo = roomNo;
        this.timeSlot = timeSlot;
    }

    // Getters and Setters
    public Faculty getFaculty() {
        return faculty;
    }

    public Exam getExam() {
        return exam;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    @Override
    public String toString() {
        return "Duty: " + faculty.getName() + " in " + roomNo + " for " +
               (exam != null ? exam.getCourse().getCode() : "Exam") +
               " on " + (timeSlot != null ? timeSlot.toString() : "TBD");
    }
}
