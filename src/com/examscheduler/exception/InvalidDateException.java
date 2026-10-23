package com.examscheduler.exception;

/**
 * User-defined exception thrown when an exam is scheduled on a Sunday, holiday, or invalid date format.
 */
public class InvalidDateException extends Exception {

    public InvalidDateException(String message) {
        super(message);
    }
}
