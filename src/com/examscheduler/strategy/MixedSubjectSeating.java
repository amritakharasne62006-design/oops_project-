package com.examscheduler.strategy;

import com.examscheduler.model.Course;
import com.examscheduler.model.Room;
import com.examscheduler.model.SeatAllocation;
import com.examscheduler.model.Student;

import java.util.*;

/**
 * Concrete strategy: Anti-Malpractice Interleaved Seating.
 * Groups students by subject and interleaves them across the grid so that
 * direct neighbors (left/right/front/back) do not write the same subject exam.
 */
public class MixedSubjectSeating implements SeatingStrategy {

    @Override
    public List<SeatAllocation> allocate(List<Student> students, Map<Student, Course> courseMap, Room room) {
        List<SeatAllocation> allocations = new ArrayList<>();
        if (students.isEmpty()) return allocations;

        // Group students by course
        Map<String, Queue<Student>> courseQueues = new HashMap<>();
        for (Student s : students) {
            Course c = courseMap.get(s);
            String cCode = (c != null) ? c.getCode() : "DEFAULT";
            courseQueues.putIfAbsent(cCode, new LinkedList<>());
            courseQueues.get(cCode).add(s);
        }

        List<String> courseList = new ArrayList<>(courseQueues.keySet());

        // Fill grid in a checkerboard pattern
        int courseIdx = 0;
        for (int r = 0; r < room.getRows(); r++) {
            for (int c = 0; c < room.getCols(); c++) {
                // If all queues are empty, we are done
                boolean hasRemaining = false;
                for (Queue<Student> q : courseQueues.values()) {
                    if (!q.isEmpty()) {
                        hasRemaining = true;
                        break;
                    }
                }
                if (!hasRemaining) return allocations;

                // Pick next course queue that has students
                int attempts = 0;
                while (courseQueues.get(courseList.get(courseIdx)).isEmpty() && attempts < courseList.size()) {
                    courseIdx = (courseIdx + 1) % courseList.size();
                    attempts++;
                }

                String selectedCourseCode = courseList.get(courseIdx);
                Queue<Student> selectedQueue = courseQueues.get(selectedCourseCode);

                if (selectedQueue != null && !selectedQueue.isEmpty()) {
                    Student seatedStudent = selectedQueue.poll();
                    Course course = courseMap.get(seatedStudent);
                    allocations.add(new SeatAllocation(seatedStudent, course, room.getRoomNo(), r, c));
                }

                // Alternate subject for next adjacent seat
                courseIdx = (courseIdx + 1) % courseList.size();
            }
        }

        return allocations;
    }

    @Override
    public String getStrategyName() {
        return "Mixed-Subject Interleaved (Anti-Cheating)";
    }
}
