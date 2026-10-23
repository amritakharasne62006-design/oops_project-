package com.examscheduler.strategy;

import com.examscheduler.model.Course;
import com.examscheduler.model.Room;
import com.examscheduler.model.SeatAllocation;
import com.examscheduler.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Concrete strategy: Randomizes student order before assigning seats.
 * Completely unpredictable seat assignment.
 */
public class RandomSeating implements SeatingStrategy {

    private Random random;

    public RandomSeating() {
        this.random = new Random(42); // fixed seed for reproducible demos
    }

    @Override
    public List<SeatAllocation> allocate(List<Student> students, Map<Student, Course> courseMap, Room room) {
        List<SeatAllocation> allocations = new ArrayList<>();
        List<Student> shuffledList = new ArrayList<>(students);
        Collections.shuffle(shuffledList, random);

        int studentIndex = 0;
        int totalStudents = shuffledList.size();

        for (int r = 0; r < room.getRows(); r++) {
            for (int c = 0; c < room.getCols(); c++) {
                if (studentIndex >= totalStudents) {
                    return allocations;
                }

                Student student = shuffledList.get(studentIndex++);
                Course course = courseMap.get(student);
                allocations.add(new SeatAllocation(student, course, room.getRoomNo(), r, c));
            }
        }

        return allocations;
    }

    @Override
    public String getStrategyName() {
        return "Randomized Seating";
    }
}
