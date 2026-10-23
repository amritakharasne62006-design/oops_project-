package com.examscheduler.strategy;

import com.examscheduler.model.Course;
import com.examscheduler.model.Room;
import com.examscheduler.model.SeatAllocation;
import com.examscheduler.model.Student;

import java.util.List;
import java.util.Map;

/**
 * Interface defining the Strategy Pattern for seating arrangements.
 * Demonstrates Abstraction, Interface, and Polymorphism.
 */
public interface SeatingStrategy {

    /**
     * Allocates students to desks in a given room according to the concrete strategy algorithm.
     *
     * @param students List of students to seat
     * @param courseMap Map of Student -> Course they are writing in this room
     * @param room The room with rows and cols
     * @return List of SeatAllocation objects
     */
    List<SeatAllocation> allocate(List<Student> students, Map<Student, Course> courseMap, Room room);

    /**
     * Returns a friendly name for the strategy.
     */
    String getStrategyName();
}
