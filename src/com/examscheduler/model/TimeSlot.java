package com.examscheduler.model;

import java.util.Objects;

/**
 * ============================================================================
 * CLASS: TimeSlot
 * ============================================================================
 * WHAT IT DOES:
 *   Represents an exam session period on a specific date.
 *   E.g., Slot 1: 2026-11-02 from 10:00 AM to 12:00 PM.
 *
 * WHY IT EXISTS (OOP CONCEPTS):
 *   1. ENCAPSULATION: Packages slot metadata together cleanly.
 *   2. EQUALS & HASHCODE: Allows matching slots in maps and conflict checking.
 * ============================================================================
 */
public class TimeSlot {

    // ------------------------------------------------------------------------
    // 1. ATTRIBUTES
    // ------------------------------------------------------------------------
    private int slotId;        // Numeric slot index (1, 2, 3...)
    private String date;       // Date string in YYYY-MM-DD format
    private String startTime;  // Starting time (e.g., "10:00 AM")
    private String endTime;    // Ending time (e.g., "12:00 PM")
    private String slotLabel;  // Session name (e.g., "Day 1 Morning")

    // ------------------------------------------------------------------------
    // 2. CONSTRUCTORS
    // ------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public TimeSlot() {
        this(1, "2026-11-02", "10:00 AM", "12:00 PM", "Morning Session");
    }

    /**
     * Parameterized constructor.
     */
    public TimeSlot(int slotId, String date, String startTime, String endTime, String slotLabel) {
        this.slotId = slotId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotLabel = slotLabel;
    }

    // ------------------------------------------------------------------------
    // 3. GETTERS AND SETTERS
    // ------------------------------------------------------------------------

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSlotLabel() {
        return slotLabel;
    }

    public void setSlotLabel(String slotLabel) {
        this.slotLabel = slotLabel;
    }

    // ------------------------------------------------------------------------
    // 4. EQUALS AND HASHCODE
    // ------------------------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return slotId == timeSlot.slotId &&
               Objects.equals(date, timeSlot.date) &&
               Objects.equals(startTime, timeSlot.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slotId, date, startTime);
    }

    @Override
    public String toString() {
        return date + " (" + startTime + " - " + endTime + ") [" + slotLabel + "]";
    }
}
