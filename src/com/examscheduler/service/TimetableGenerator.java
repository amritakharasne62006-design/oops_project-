package com.examscheduler.service;

import com.examscheduler.model.Course;
import com.examscheduler.model.Exam;
import com.examscheduler.model.Student;
import com.examscheduler.model.TimeSlot;
import com.examscheduler.exception.SchedulingClashException;

import java.util.*;

/**
 * Service to generate a clash-free exam timetable using Graph Colouring.
 * Nodes = Courses, Edges = Shared enrolled students, Colours = Time Slots.
 */
public class TimetableGenerator {

    private List<Course> courses;
    private List<Student> students;
    private List<TimeSlot> availableSlots;
    private Map<Course, Set<Course>> conflictGraph;

    /**
     * Parameterized constructor.
     */
    public TimetableGenerator(List<Course> courses, List<Student> students, List<TimeSlot> availableSlots) {
        this.courses = courses;
        this.students = students;
        this.availableSlots = availableSlots;
        this.conflictGraph = new HashMap<>();
    }

    /**
     * Builds the conflict graph where an edge connects two courses if they share >= 1 student.
     */
    public void buildConflictGraph() {
        // Initialize an empty neighbor set for each course
        for (Course course : courses) {
            conflictGraph.put(course, new HashSet<>());
        }

        // Compare every pair of courses
        for (int i = 0; i < courses.size(); i++) {
            Course c1 = courses.get(i);
            for (int j = i + 1; j < courses.size(); j++) {
                Course c2 = courses.get(j);

                // Check if any student takes both c1 and c2
                if (hasCommonStudents(c1, c2)) {
                    conflictGraph.get(c1).add(c2);
                    conflictGraph.get(c2).add(c1);
                }
            }
        }
    }

    /**
     * Checks if at least one student is enrolled in both c1 and c2.
     */
    private boolean hasCommonStudents(Course c1, Course c2) {
        for (Student s : students) {
            if (s.isEnrolledIn(c1.getCode()) && s.isEnrolledIn(c2.getCode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates exams by applying the Greedy Graph Colouring algorithm.
     * Throws SchedulingClashException if available slots are fewer than chromatic number.
     */
    public List<Exam> generateSchedule() throws SchedulingClashException {
        buildConflictGraph();

        // Map course -> color index (0, 1, 2, ...)
        Map<Course, Integer> courseColors = new HashMap<>();

        for (Course course : courses) {
            // Find colors already used by conflicting neighbor courses
            Set<Integer> usedColors = new HashSet<>();
            for (Course neighbor : conflictGraph.get(course)) {
                if (courseColors.containsKey(neighbor)) {
                    usedColors.add(courseColors.get(neighbor));
                }
            }

            // Assign the smallest available non-conflicting color
            int assignedColor = 0;
            while (usedColors.contains(assignedColor)) {
                assignedColor++;
            }

            courseColors.put(course, assignedColor);
        }

        // Verify if we have enough time slots for the max color used
        List<Exam> scheduledExams = new ArrayList<>();
        for (Course course : courses) {
            int colorIndex = courseColors.get(course);

            if (colorIndex >= availableSlots.size()) {
                throw new SchedulingClashException(
                    "Insufficient time slots! Course " + course.getCode() +
                    " needs slot index " + colorIndex + ", but only " + availableSlots.size() + " slots provided."
                );
            }

            TimeSlot slot = availableSlots.get(colorIndex);
            Exam exam = new Exam(course, slot);

            // Populate all students enrolled in this course
            for (Student s : students) {
                if (s.isEnrolledIn(course.getCode())) {
                    exam.addStudent(s);
                }
            }

            scheduledExams.add(exam);
        }

        return scheduledExams;
    }

    /**
     * Manual override: Moves an exam to a different slot and re-validates.
     */
    public boolean manualOverride(Exam examToMove, TimeSlot newSlot, List<Exam> allExams) throws SchedulingClashException {
        // Check if moving to newSlot causes any student to have two exams in the same slot
        for (Exam other : allExams) {
            if (other != examToMove && other.getTimeSlot().equals(newSlot)) {
                // If courses conflict, override must be rejected!
                if (conflictGraph.containsKey(examToMove.getCourse()) &&
                    conflictGraph.get(examToMove.getCourse()).contains(other.getCourse())) {
                    throw new SchedulingClashException(
                        "Manual override REJECTED: Common students enrolled in both " +
                        examToMove.getCourse().getCode() + " and " + other.getCourse().getCode() + "!"
                    );
                }
            }
        }

        // No clash found; apply override
        examToMove.setTimeSlot(newSlot);
        return true;
    }

    // Getter for conflict graph inspection
    public Map<Course, Set<Course>> getConflictGraph() {
        return conflictGraph;
    }
}
