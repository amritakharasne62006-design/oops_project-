package com.examscheduler.model;

/**
 * ============================================================================
 * CLASS: InvigilatorDuty
 * ============================================================================
 * WHAT IT DOES:
 *   Represents one exam invigilation duty assignment.
 *   Connects:
 *     - The assigned Faculty teacher
 *     - The Exam being supervised
 *     - The Room being invigilated
 *     - The TimeSlot when duty takes place
 * ============================================================================
 */
public class InvigilatorDuty {

    // ------------------------------------------------------------------------
    // 1. ATTRIBUTES
    // ------------------------------------------------------------------------
    private Faculty faculty;    // Appointed faculty member
    private Exam exam;          // Exam being invigilated
    private String roomNo;      // Room number (e.g., "Auditorium")
    private TimeSlot timeSlot;  // Date and timing of the duty

    // ------------------------------------------------------------------------
    // 2. CONSTRUCTOR
    // ------------------------------------------------------------------------
    public InvigilatorDuty(Faculty faculty, Exam exam, String roomNo, TimeSlot timeSlot) {
        this.faculty = faculty;
        this.exam = exam;
        this.roomNo = roomNo;
        this.timeSlot = timeSlot;
    }

    // ------------------------------------------------------------------------
    // 3. GETTERS AND SETTERS
    // ------------------------------------------------------------------------

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public String toString() {
        return "InvigilatorDuty [Faculty=" + faculty.getName() +
               ", Room=" + roomNo +
               ", Course=" + (exam != null ? exam.getCourse().getCode() : "N/A") +
               ", Slot=" + (timeSlot != null ? timeSlot.toString() : "TBD") + "]";
    }
}
