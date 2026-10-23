package com.examscheduler.model;

/**
 * Class representing the physical assignment of a student to a room and desk.
 * Captures row, column, course, and a human-readable seat label (e.g. "R1-C3").
 */
public class SeatAllocation {

    private Student student;
    private Course course;
    private String roomNo;
    private int row;
    private int col;
    private String seatLabel;

    /**
     * Parameterized constructor.
     */
    public SeatAllocation(Student student, Course course, String roomNo, int row, int col) {
        this.student = student;
        this.course = course;
        this.roomNo = roomNo;
        this.row = row;
        this.col = col;
        this.seatLabel = "R" + (row + 1) + "-C" + (col + 1);
    }

    // Getters and Setters
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
        return "[" + roomNo + " | Seat: " + seatLabel + " | " + student.getPrn() + " (" + course.getCode() + ")]";
    }
}
