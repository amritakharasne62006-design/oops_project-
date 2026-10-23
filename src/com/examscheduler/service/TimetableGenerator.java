package com.examscheduler.service;

import com.examscheduler.model.Course;
import com.examscheduler.model.Exam;
import com.examscheduler.model.Student;
import com.examscheduler.model.TimeSlot;
import com.examscheduler.exception.SchedulingClashException;

import java.util.*;

/**
 * ============================================================================
 * CLASS: TimetableGenerator
 * ============================================================================
 * WHAT IT DOES:
 *   Automatically builds a clash-free exam timetable using Graph Colouring.
 *
 * HOW THE GRAPH COLOURING ALGORITHM WORKS:
 *   1. Vertices (Nodes) = Courses / Subjects to be scheduled.
 *   2. Edges (Lines)    = Drawn between Course A and Course B if at least ONE
 *                         student is registered for both subjects.
 *   3. Colours          = Non-overlapping Time Slots (Slot 1, Slot 2, Slot 3...).
 *   4. Greedy Colouring = For each course, look at the colours already taken
 *                         by its neighbors, and assign the lowest available colour.
 *
 * RESULT:
 *   Zero student clashes guaranteed! Connected courses will NEVER share the same slot.
 * ============================================================================
 */
public class TimetableGenerator {

    // ------------------------------------------------------------------------
    // 1. DATA FIELDS
    // ------------------------------------------------------------------------
    private List<Course> courses;                    // All academic courses to schedule
    private List<Student> students;                  // All enrolled students
    private List<TimeSlot> availableSlots;           // Available calendar time slots
    private Map<Course, Set<Course>> conflictGraph;  // Adjacency list representation of Graph

    // ------------------------------------------------------------------------
    // 2. CONSTRUCTOR
    // ------------------------------------------------------------------------
    public TimetableGenerator(List<Course> courses, List<Student> students, List<TimeSlot> availableSlots) {
        this.courses = courses;
        this.students = students;
        this.availableSlots = availableSlots;
        this.conflictGraph = new HashMap<>();
    }

    // ------------------------------------------------------------------------
    // 3. STEP A: BUILD THE CONFLICT GRAPH
    // ------------------------------------------------------------------------

    /**
     * Constructs the undirected graph where an edge represents common student enrollment.
     */
    public void buildConflictGraph() {
        // Step 1: Initialize an empty set of adjacent neighbors for every course node
        for (Course course : courses) {
            conflictGraph.put(course, new HashSet<>());
        }

        // Step 2: Compare every pair of courses (Course i and Course j)
        for (int i = 0; i < courses.size(); i++) {
            Course c1 = courses.get(i);
            for (int j = i + 1; j < courses.size(); j++) {
                Course c2 = courses.get(j);

                // If any student is taking both c1 and c2, add an edge between them
                if (hasCommonStudents(c1, c2)) {
                    conflictGraph.get(c1).add(c2);
                    conflictGraph.get(c2).add(c1);
                }
            }
        }
    }

    /**
     * Checks if there exists at least one student enrolled in both c1 and c2.
     */
    private boolean hasCommonStudents(Course c1, Course c2) {
        for (Student s : students) {
            if (s.isEnrolledIn(c1.getCode()) && s.isEnrolledIn(c2.getCode())) {
                return true; // Clash exists if scheduled at same time!
            }
        }
        return false; // Safe: no common students
    }

    // ------------------------------------------------------------------------
    // 4. STEP B: GREEDY GRAPH COLOURING & SLOT ASSIGNMENT
    // ------------------------------------------------------------------------

    /**
     * Assigns time slots to courses using greedy graph colouring.
     * Throws SchedulingClashException if we run out of available time slots.
     */
    public List<Exam> generateSchedule() throws SchedulingClashException {
        // Build graph connections first
        buildConflictGraph();

        // Maps: Course -> Assigned Color Number (0 = Slot 1, 1 = Slot 2, ...)
        Map<Course, Integer> courseColors = new HashMap<>();

        // Process each course one-by-one
        for (Course currentCourse : courses) {

            // Step 1: Collect colors already used by conflicting neighbor courses
            Set<Integer> neighborColors = new HashSet<>();
            for (Course neighbor : conflictGraph.get(currentCourse)) {
                if (courseColors.containsKey(neighbor)) {
                    neighborColors.add(courseColors.get(neighbor));
                }
            }

            // Step 2: Pick the lowest non-conflicting color (0, 1, 2, ...)
            int chosenColor = 0;
            while (neighborColors.contains(chosenColor)) {
                chosenColor++; // Increment until an unused color is found
            }

            // Step 3: Assign this color to the current course
            courseColors.put(currentCourse, chosenColor);
        }

        // Step 4: Map each assigned color index to an actual real-world TimeSlot
        List<Exam> scheduledExams = new ArrayList<>();
        for (Course course : courses) {
            int colorIndex = courseColors.get(course);

            // Constraint check: do we have enough slots?
            if (colorIndex >= availableSlots.size()) {
                throw new SchedulingClashException(
                    "Insufficient time slots! Course " + course.getCode() +
                    " requires slot index " + colorIndex +
                    ", but only " + availableSlots.size() + " slots were provided."
                );
            }

            TimeSlot slot = availableSlots.get(colorIndex);
            Exam exam = new Exam(course, slot);

            // Populate all students taking this course
            for (Student s : students) {
                if (s.isEnrolledIn(course.getCode())) {
                    exam.addStudent(s);
                }
            }

            scheduledExams.add(exam);
        }

        return scheduledExams;
    }

    // ------------------------------------------------------------------------
    // 5. MANUAL OVERRIDE (With Re-Validation)
    // ------------------------------------------------------------------------

    /**
     * Admin override: Moves an exam to a different slot and re-validates constraints.
     * Throws SchedulingClashException if the move would create a student clash.
     */
    public boolean manualOverride(Exam examToMove, TimeSlot newSlot, List<Exam> allExams) throws SchedulingClashException {
        for (Exam other : allExams) {
            // Check other exams taking place in this target newSlot
            if (other != examToMove && other.getTimeSlot().equals(newSlot)) {
                // If there is an edge between their courses in conflictGraph, REJECT!
                if (conflictGraph.containsKey(examToMove.getCourse()) &&
                    conflictGraph.get(examToMove.getCourse()).contains(other.getCourse())) {
                    throw new SchedulingClashException(
                        "Manual override REJECTED: Students are enrolled in both " +
                        examToMove.getCourse().getCode() + " and " + other.getCourse().getCode() + "!"
                    );
                }
            }
        }

        // Safe move: update the time slot
        examToMove.setTimeSlot(newSlot);
        return true;
    }

    // ------------------------------------------------------------------------
    // 6. GETTER FOR CONFLICT GRAPH
    // ------------------------------------------------------------------------
    public Map<Course, Set<Course>> getConflictGraph() {
        return conflictGraph;
    }
}
