package com.examscheduler.exception;

/**
 * User-defined exception thrown when there are not enough eligible faculty members to invigilate exams.
 */
public class InsufficientInvigilatorsException extends Exception {

    public InsufficientInvigilatorsException(String message) {
        super(message);
    }
}
