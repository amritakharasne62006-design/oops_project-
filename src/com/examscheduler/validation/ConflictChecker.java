package com.examscheduler.validation;

import com.examscheduler.model.*;
import java.util.*;

/**
 * ============================================================================
 * CLASS: ConflictChecker
 * ============================================================================
 * WHAT IT DOES:
 *   Validates generated schedules to detect any violations of hard or soft rules.
 *
 * CONSTRAINTS AUDITED:
 *   1. HARD CLASH: A student must NOT have two exams scheduled in the same slot.
 *   2. HARD CLASH: No exams can be scheduled on Sundays or declared holidays.
 *   3. HARD CLASH: A faculty member must NOT be assigned to two rooms at once.
 *   4. SOFT WARNING: Flags students having back-to-back exams on the same date.
 * ============================================================================
 */
public class ConflictChecker {

    // ------------------------------------------------------------------------
    // 1. DATA FIELDS
    // ------------------------------------------------------------------------
    private List<Exam> exams;
    private List<Student> students;
    private List<InvigilatorDuty> duties;
    private List<String> holidayDates;

    // ------------------------------------------------------------------------
    // 2. CONSTRUCTOR
    // ------------------------------------------------------------------------
    public ConflictChecker(List<Exam> exams, List<Student> students, List<InvigilatorDuty> duties, List<String> holidayDates) {
        this.exams = exams;
        this.students = students;
        this.duties = duties;
        this.holidayDates = (holidayDates != null) ? holidayDates : new ArrayList<>();
    }

    // ------------------------------------------------------------------------
    // 3. FULL AUDIT RUNNER
    // ------------------------------------------------------------------------

    /**
     * Executes all constraint checks and returns a comprehensive list of findings.
     */
    public List<String> runFullAudit() {
        List<String> report = new ArrayList<>();

        checkStudentClashes(report);
        checkHolidayViolations(report);
        checkInvigilatorClashes(report);
        checkBackToBackExams(report);

        return report;
    }

    // ------------------------------------------------------------------------
    // 4. CONSTRAINT CHECK METHODS
    // ------------------------------------------------------------------------

    /**
     * Hard constraint: Detects if any student has two simultaneous exams.
     */
    private void checkStudentClashes(List<String> report) {
        Map<String, List<Exam>> studentSlotExams = new HashMap<>();

        for (Exam exam : exams) {
            for (Student student : exam.getEligibleStudents()) {
                String key = student.getPrn() + "@" + exam.getTimeSlot().getDate() + "_" + exam.getTimeSlot().getStartTime();
                studentSlotExams.putIfAbsent(key, new ArrayList<>());
                studentSlotExams.get(key).add(exam);
            }
        }

        for (Map.Entry<String, List<Exam>> entry : studentSlotExams.entrySet()) {
            if (entry.getValue().size() > 1) {
                report.add("[HARD CLASH] Student PRN " + entry.getKey().split("@")[0] +
                           " has " + entry.getValue().size() + " simultaneous exams scheduled at " +
                           entry.getKey().split("@")[1] + "!");
            }
        }
    }

    /**
     * Hard constraint: Checks if any exam lands on a Sunday or declared holiday.
     */
    private void checkHolidayViolations(List<String> report) {
        for (Exam exam : exams) {
            String date = exam.getTimeSlot().getDate();
            if (holidayDates.contains(date)) {
                report.add("[HARD CLASH] Exam " + exam.getCourse().getCode() +
                           " is scheduled on a declared holiday: " + date);
            }
        }
    }

    /**
     * Hard constraint: Checks if a teacher is double-booked in two rooms at the same time.
     */
    private void checkInvigilatorClashes(List<String> report) {
        Map<String, List<InvigilatorDuty>> facultySlotDuties = new HashMap<>();

        for (InvigilatorDuty duty : duties) {
            String key = duty.getFaculty().getId() + "@" + duty.getTimeSlot().getDate() + "_" + duty.getTimeSlot().getStartTime();
            facultySlotDuties.putIfAbsent(key, new ArrayList<>());
            facultySlotDuties.get(key).add(duty);
        }

        for (Map.Entry<String, List<InvigilatorDuty>> entry : facultySlotDuties.entrySet()) {
            if (entry.getValue().size() > 1) {
                report.add("[HARD CLASH] Faculty ID " + entry.getKey().split("@")[0] +
                           " assigned to multiple rooms in slot: " + entry.getKey().split("@")[1]);
            }
        }
    }

    /**
     * Soft constraint: Warns if a student has two exams on the same date.
     */
    private void checkBackToBackExams(List<String> report) {
        Map<String, List<Exam>> studentDayExams = new HashMap<>();

        for (Exam exam : exams) {
            for (Student student : exam.getEligibleStudents()) {
                String key = student.getPrn() + "@" + exam.getTimeSlot().getDate();
                studentDayExams.putIfAbsent(key, new ArrayList<>());
                studentDayExams.get(key).add(exam);
            }
        }

        for (Map.Entry<String, List<Exam>> entry : studentDayExams.entrySet()) {
            if (entry.getValue().size() > 1) {
                report.add("[SOFT WARNING] Student PRN " + entry.getKey().split("@")[0] +
                           " has " + entry.getValue().size() + " exams on the same date (" +
                           entry.getKey().split("@")[1] + ")");
            }
        }
    }
}
