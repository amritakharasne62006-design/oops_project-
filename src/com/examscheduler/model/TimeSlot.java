package com.examscheduler.model;

import java.util.Objects;

/**
 * Class representing an exam timing slot on a specific date.
 * E.g., Slot 1: 10:00 AM - 12:00 PM, Slot 2: 02:00 PM - 04:00 PM
 */
public class TimeSlot {

    private int slotId;
    private String date;      // E.g. "2026-11-02"
    private String startTime; // E.g. "10:00 AM"
    private String endTime;   // E.g. "12:00 PM"
    private String slotLabel; // E.g. "Morning Session"

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

    // Getters and Setters
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
