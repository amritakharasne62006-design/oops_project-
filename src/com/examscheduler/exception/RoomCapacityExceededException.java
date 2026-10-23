package com.examscheduler.exception;

/**
 * User-defined exception thrown when the number of students exceeds available room capacity.
 */
public class RoomCapacityExceededException extends Exception {

    public RoomCapacityExceededException(String message) {
        super(message);
    }
}
