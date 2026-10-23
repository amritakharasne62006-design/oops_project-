package com.examscheduler.strategy;

import com.examscheduler.model.Course;
import com.examscheduler.model.Room;
import com.examscheduler.model.SeatAllocation;
import com.examscheduler.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Concrete strategy: Assigns seats row by row, left to right.
 * Simple, standard seating strategy.
 */
public class SequentialSeating implements SeatingStrategy {

    @Override
    public List<SeatAllocation> allocate(List<Student> students, Map<Student, Course> courseMap, Room room) {
        List<SeatAllocation> allocations = new ArrayList<>();
        int studentIndex = 0;
        int totalStudents = students.size();

        for (int r = 0; r < room.getRows(); r++) {
            for (int c = 0; c < room.getCols(); c++) {
                if (studentIndex >= totalStudents) {
                    return allocations; // All students seated
                }

                Student student = students.get(studentIndex);
                Course course = courseMap.get(student);
                allocations.add(new SeatAllocation(student, course, room.getRoomNo(), r, c));
                studentIndex++;
            }
        }

        return allocations;
    }

    @Override
    public String getStrategyName() {
        return "Sequential (Row-by-Row)";
    }
}
