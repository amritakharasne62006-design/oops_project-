package com.examscheduler.model;

/**
 * ============================================================================
 * CLASS: Room
 * ============================================================================
 * WHAT IT DOES:
 *   Represents a physical exam classroom or hall with rows and columns of desks.
 *
 * WHY IT EXISTS (OOP CONCEPTS):
 *   1. 2D ARRAY / GRID MODELING: Represents classroom seating matrices.
 *   2. CONSTRUCTOR OVERLOADING: Default, Parameterized, and Copy constructors.
 *   3. ENCAPSULATION: Hides capacity, dimensions, and availability status.
 * ============================================================================
 */
public class Room {

    // ------------------------------------------------------------------------
    // 1. ATTRIBUTES
    // ------------------------------------------------------------------------
    private String roomNo;        // Room identifier (e.g., "Room-101", "Auditorium")
    private int capacity;         // Maximum number of students allowed
    private int rows;             // Number of desk rows
    private int cols;             // Number of desk columns per row
    private boolean isAvailable;  // Whether room is open for exam use

    // ------------------------------------------------------------------------
    // 2. CONSTRUCTORS
    // ------------------------------------------------------------------------

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

    // ------------------------------------------------------------------------
    // 3. METHODS
    // ------------------------------------------------------------------------

    /**
     * Returns total physical desks in the row x col matrix.
     */
    public int getGridCapacity() {
        return rows * cols;
    }

    // ------------------------------------------------------------------------
    // 4. GETTERS AND SETTERS
    // ------------------------------------------------------------------------

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
        return "Room " + roomNo + " [Capacity: " + capacity + " (" + rows + "x" + cols + "), Available: " + isAvailable + "]";
    }
}
