package com.examscheduler.model;

/**
 * Class representing an examination hall or classroom.
 * Demonstrates 2D arrays (Array of Objects), encapsulation, and constructor overloading.
 */
public class Room {

    private String roomNo;
    private int capacity;
    private int rows;
    private int cols;
    private boolean isAvailable;

    /**
     * Default constructor.
     */
    public Room() {
        this("R101", 30, 5, 6, true);
    }

    /**
     * Parameterized constructor.
     */
    public Room(String roomNo, int capacity, int rows, int cols, boolean isAvailable) {
        this.roomNo = roomNo;
        this.capacity = capacity;
        this.rows = rows;
        this.cols = cols;
        this.isAvailable = isAvailable;
    }

    /**
     * Copy constructor.
     */
    public Room(Room other) {
        if (other != null) {
            this.roomNo = other.roomNo;
            this.capacity = other.capacity;
            this.rows = other.rows;
            this.cols = other.cols;
            this.isAvailable = other.isAvailable;
        }
    }

    /**
     * Calculates the maximum theoretical seats based on row x col grid.
     */
    public int getGridCapacity() {
        return rows * cols;
    }

    // Getters and Setters
    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Room " + roomNo + " [Capacity: " + capacity + " (" + rows + "x" + cols + ")]";
    }
}
