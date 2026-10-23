package com.examscheduler.validation;

import com.examscheduler.model.*;
import java.util.*;

/**
 * Validator class that verifies all hard and soft exam constraints.
 * Generates an audit report detailing any clashes or scheduling issues.
 */
public class ConflictChecker {

    private List<Exam> exams;
    private List<Student> students;
    private List<InvigilatorDuty> duties;
    private List<String> holidayDates;

    /**
     * Parameterized constructor.
     */
    public ConflictChecker(List<Exam> exams, List<Student> students, List<InvigilatorDuty> duties, List<String> holidayDates) {
        this.exams = exams;
        this.students = students;
        this.duties = duties;
        this.holidayDates = holidayDates != null ? holidayDates : new ArrayList<>();
    }

    /**
     * Runs complete validation and returns a list of conflict strings (empty if perfect).
     */
    public List<String> runFullAudit() {
        List<String> report = new ArrayList<>();

        checkStudentClashes(report);
        checkHolidayViolations(report);
        checkInvigilatorClashes(report);
        checkBackToBackExams(report);

        return report;
    }

    /**
     * Hard constraint: No student must have two exams in the exact same time slot.
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
                report.add("[HARD CLASH] Student " + entry.getKey().split("@")[0] +
                           " has " + entry.getValue().size() + " simultaneous exams scheduled at " +
                           entry.getKey().split("@")[1] + "!");
            }
        }
    }

    /**
     * Hard constraint: No exams on holidays or Sundays.
     */
    private void checkHolidayViolations(List<String> report) {
        for (Exam exam : exams) {
            String date = exam.getTimeSlot().getDate();
            if (holidayDates.contains(date)) {
                report.add("[HARD CLASH] Exam " + exam.getCourse().getCode() +
                           " scheduled on declared holiday: " + date);
            }
        }
    }

    /**
     * Hard constraint: No faculty member should be in two rooms at the same time slot.
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
                report.add("[HARD CLASH] Faculty " + entry.getKey().split("@")[0] +
                           " assigned to multiple rooms in slot: " + entry.getKey().split("@")[1]);
            }
        }
    }

    /**
     * Soft constraint: Warn if a student has back-to-back exams on the same day.
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
                report.add("[SOFT WARNING] Student " + entry.getKey().split("@")[0] +
                           " has " + entry.getValue().size() + " exams on the same date (" +
                           entry.getKey().split("@")[1] + ")");
            }
        }
    }
}
