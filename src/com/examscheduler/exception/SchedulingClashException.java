package com.examscheduler.exception;

/**
 * User-defined checked exception thrown when two exams conflict in timing or room allocation.
 * Demonstrates Custom Exception Handling in Java.
 */
public class SchedulingClashException extends Exception {

    public SchedulingClashException(String message) {
        super(message);
    }

    public SchedulingClashException(String message, Throwable cause) {
        super(message, cause);
    }
}
