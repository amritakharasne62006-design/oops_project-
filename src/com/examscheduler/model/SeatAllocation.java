package com.examscheduler.model;

/**
 * ============================================================================
 * CLASS: SeatAllocation
 * ============================================================================
 * WHAT IT DOES:
 *   Represents the physical desk assignment of one student in an exam hall.
 *   Tracks: Student, Course, Room Number, Row Index, Column Index, and Seat Label.
 *   E.g., Seat "R1-C2" for Student "101" in "Auditorium".
 * ============================================================================
 */
public class SeatAllocation {

    // ------------------------------------------------------------------------
    // 1. ATTRIBUTES
    // ------------------------------------------------------------------------
    private Student student;    // Student seated at this desk
    private Course course;      // Subject exam paper they are writing
    private String roomNo;      // Exam room (e.g., "Auditorium")
    private int row;            // 0-indexed row number
    private int col;            // 0-indexed column number
    private String seatLabel;   // Human-friendly label (e.g., "R1-C2")

    // ------------------------------------------------------------------------
    // 2. CONSTRUCTOR
    // ------------------------------------------------------------------------
    public SeatAllocation(Student student, Course course, String roomNo, int row, int col) {
        this.student = student;
        this.course = course;
        this.roomNo = roomNo;
        this.row = row;
        this.col = col;
        // Construct human-readable seat code (1-indexed for students)
        this.seatLabel = "R" + (row + 1) + "-C" + (col + 1);
    }

    // ------------------------------------------------------------------------
    // 3. GETTERS AND SETTERS
    // ------------------------------------------------------------------------

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getSeatLabel() {
        return seatLabel;
    }

    @Override
    public String toString() {
        return "[" + roomNo + " | Seat: " + seatLabel + " | PRN: " + student.getPrn() + " (" + course.getCode() + ")]";
    }
}
