package com.examscheduler.strategy;

import com.examscheduler.model.Course;
import com.examscheduler.model.Room;
import com.examscheduler.model.SeatAllocation;
import com.examscheduler.model.Student;

import java.util.*;

/**
 * ============================================================================
 * CLASS: MixedSubjectSeating (Implements SeatingStrategy)
 * ============================================================================
 * WHAT IT DOES:
 *   ANTI-MALPRACTICE / ANTI-CHEATING SEATING STRATEGY.
 *   Groups students by course and interleaves them across the desk grid.
 *
 * HOW IT WORKS:
 *   1. Students are partitioned into separate queues by their subject exam.
 *   2. Desks are filled one by one across rows and columns.
 *   3. At each desk, we pick a student from an alternating course queue.
 *   4. Direct physical neighbors (left, right, front, back) do NOT write
 *      the same exam subject, making copying impossible!
 * ============================================================================
 */
public class MixedSubjectSeating implements SeatingStrategy {

    @Override
    public List<SeatAllocation> allocate(List<Student> students, Map<Student, Course> courseMap, Room room) {
        List<SeatAllocation> allocations = new ArrayList<>();
        if (students.isEmpty()) return allocations;

        // Step 1: Group students into separate queues based on course code
        Map<String, Queue<Student>> courseQueues = new HashMap<>();
        for (Student s : students) {
            Course c = courseMap.get(s);
            String courseCode = (c != null) ? c.getCode() : "DEFAULT";
            courseQueues.putIfAbsent(courseCode, new LinkedList<>());
            courseQueues.get(courseCode).add(s);
        }

        List<String> courseList = new ArrayList<>(courseQueues.keySet());

        // Step 2: Iterate through each desk in the room grid
        int courseIndex = 0;
        for (int r = 0; r < room.getRows(); r++) {
            for (int c = 0; c < room.getCols(); c++) {

                // Check if all queues are empty
                boolean studentsRemaining = false;
                for (Queue<Student> q : courseQueues.values()) {
                    if (!q.isEmpty()) {
                        studentsRemaining = true;
                        break;
                    }
                }
                if (!studentsRemaining) {
                    return allocations; // All students have been seated
                }

                // Find next course queue that still has students
                int searchAttempts = 0;
                while (courseQueues.get(courseList.get(courseIndex)).isEmpty() && searchAttempts < courseList.size()) {
                    courseIndex = (courseIndex + 1) % courseList.size();
                    searchAttempts++;
                }

                String currentCourseCode = courseList.get(courseIndex);
                Queue<Student> activeQueue = courseQueues.get(currentCourseCode);

                if (activeQueue != null && !activeQueue.isEmpty()) {
                    Student seatedStudent = activeQueue.poll();
                    Course course = courseMap.get(seatedStudent);

                    // Create physical seat assignment
                    allocations.add(new SeatAllocation(seatedStudent, course, room.getRoomNo(), r, c));
                }

                // Move to the next subject for the next adjacent desk
                courseIndex = (courseIndex + 1) % courseList.size();
            }
        }

        return allocations;
    }

    @Override
    public String getStrategyName() {
        return "Mixed-Subject Interleaved (Anti-Cheating)";
    }
}
