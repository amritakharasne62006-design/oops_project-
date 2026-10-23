package com.examscheduler.strategy;

import com.examscheduler.model.Course;
import com.examscheduler.model.Room;
import com.examscheduler.model.SeatAllocation;
import com.examscheduler.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Concrete strategy: Assigns seats in alternating snake/zig-zag direction.
 * Even rows: Left-to-Right. Odd rows: Right-to-Left.
 * Helps reduce adjacent peer copying during exams.
 */
public class ZigZagSeating implements SeatingStrategy {

    @Override
    public List<SeatAllocation> allocate(List<Student> students, Map<Student, Course> courseMap, Room room) {
        List<SeatAllocation> allocations = new ArrayList<>();
        int studentIndex = 0;
        int totalStudents = students.size();

        for (int r = 0; r < room.getRows(); r++) {
            boolean leftToRight = (r % 2 == 0);

            if (leftToRight) {
                for (int c = 0; c < room.getCols(); c++) {
                    if (studentIndex >= totalStudents) return allocations;
                    Student student = students.get(studentIndex++);
                    Course course = courseMap.get(student);
                    allocations.add(new SeatAllocation(student, course, room.getRoomNo(), r, c));
                }
            } else {
                for (int c = room.getCols() - 1; c >= 0; c--) {
                    if (studentIndex >= totalStudents) return allocations;
                    Student student = students.get(studentIndex++);
                    Course course = courseMap.get(student);
                    allocations.add(new SeatAllocation(student, course, room.getRoomNo(), r, c));
                }
            }
        }

        return allocations;
    }

    @Override
    public String getStrategyName() {
        return "Zig-Zag (Alternating Snake)";
    }
}
