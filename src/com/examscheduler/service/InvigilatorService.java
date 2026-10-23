package com.examscheduler.service;

import com.examscheduler.model.*;
import com.examscheduler.exception.InsufficientInvigilatorsException;

import java.util.*;

/**
 * ============================================================================
 * CLASS: InvigilatorService
 * ============================================================================
 * WHAT IT DOES:
 *   Distributes exam invigilation duties fairly across available faculty.
 *
 * HOW THE FAIR ALLOCATION WORKS:
 *   1. Utilizes a PriorityQueue<Faculty> ordered by 'dutyCount' ascending.
 *   2. The faculty member with the lowest number of duties is always polled first.
 *   3. Enforces Hard Rules:
 *      - Teacher must not already have a duty in the same time slot.
 *      - Teacher must not be on leave on the exam date.
 *      - Avoid assigning a teacher to monitor their own subject/department.
 *   4. Increments duty count and re-inserts into PriorityQueue to balance duties.
 * ============================================================================
 */
public class InvigilatorService {

    // ------------------------------------------------------------------------
    // 1. DATA FIELDS
    // ------------------------------------------------------------------------
    private List<Faculty> facultyList;

    // ------------------------------------------------------------------------
    // 2. CONSTRUCTOR
    // ------------------------------------------------------------------------
    public InvigilatorService(List<Faculty> facultyList) {
        this.facultyList = facultyList;
    }

    // ------------------------------------------------------------------------
    // 3. MAIN ALLOCATION ALGORITHM
    // ------------------------------------------------------------------------

    /**
     * Fairly distributes duties for all scheduled exams across all exam halls.
     */
    public List<InvigilatorDuty> allocateInvigilators(List<Exam> exams) throws InsufficientInvigilatorsException {
        List<InvigilatorDuty> dutyRoster = new ArrayList<>();

        // PriorityQueue ordered by fewest duties so far (via Faculty.compareTo)
        PriorityQueue<Faculty> queue = new PriorityQueue<>(facultyList);

        // Group exams by TimeSlot to prevent assigning a teacher to 2 rooms simultaneously
        Map<TimeSlot, List<Exam>> slotExamMap = new LinkedHashMap<>();
        for (Exam exam : exams) {
            slotExamMap.putIfAbsent(exam.getTimeSlot(), new ArrayList<>());
            slotExamMap.get(exam.getTimeSlot()).add(exam);
        }

        // Process each exam session slot
        for (Map.Entry<TimeSlot, List<Exam>> entry : slotExamMap.entrySet()) {
            TimeSlot slot = entry.getKey();
            List<Exam> slotExams = entry.getValue();

            // Set of faculty already assigned during this slot
            Set<Faculty> busyInThisSlot = new HashSet<>();

            for (Exam exam : slotExams) {
                for (Room room : exam.getAssignedRooms()) {
                    Faculty assignedTeacher = findEligibleFaculty(queue, slot, exam, busyInThisSlot);

                    if (assignedTeacher == null) {
                        throw new InsufficientInvigilatorsException(
                            "Cannot find available faculty for " + exam.getCourse().getCode() +
                            " in Room " + room.getRoomNo() + " on " + slot.getDate() + " (" + slot.getStartTime() + ")!"
                        );
                    }

                    // Create duty record
                    InvigilatorDuty duty = new InvigilatorDuty(assignedTeacher, exam, room.getRoomNo(), slot);
                    dutyRoster.add(duty);

                    // Mark teacher as busy for this specific slot
                    busyInThisSlot.add(assignedTeacher);

                    // Increment duty count
                    assignedTeacher.incrementDuty();

                    // Re-position in PriorityQueue to maintain min-heap order
                    queue.remove(assignedTeacher);
                    queue.offer(assignedTeacher);
                }
            }
        }

        return dutyRoster;
    }

    // ------------------------------------------------------------------------
    // 4. ELIGIBILITY FILTER
    // ------------------------------------------------------------------------

    /**
     * Polls the PriorityQueue to find the teacher with lowest duties who satisfies all rules.
     */
    private Faculty findEligibleFaculty(PriorityQueue<Faculty> queue, TimeSlot slot, Exam exam, Set<Faculty> busySet) {
        List<Faculty> temporaryHold = new ArrayList<>();
        Faculty selected = null;

        while (!queue.isEmpty()) {
            Faculty candidate = queue.poll();
            temporaryHold.add(candidate);

            // Rule 1: Cannot be assigned to another room in this same slot
            if (busySet.contains(candidate)) {
                continue;
            }

            // Rule 2: Cannot be on leave on this date
            if (candidate.isOnLeave(slot.getDate())) {
                continue;
            }

            // Rule 3: Avoid invigilating own subject's exam if other faculty are available
            boolean isOwnSubject = exam.getCourse().getName().toLowerCase().contains(candidate.getDepartment().toLowerCase());
            if (isOwnSubject && queue.size() > 1) {
                continue;
            }

            // Candidate is eligible and has the fewest duties!
            selected = candidate;
            break;
        }

        // Return all inspected faculty back into the PriorityQueue
        queue.addAll(temporaryHold);
        return selected;
    }

    // ------------------------------------------------------------------------
    // 5. BONUS: DUTY SWAP REQUEST
    // ------------------------------------------------------------------------

    /**
     * Enables swapping duties between two faculty members if neither is on leave.
     */
    public boolean swapDuty(InvigilatorDuty duty1, InvigilatorDuty duty2) {
        if (duty1 == null || duty2 == null) return false;

        Faculty f1 = duty1.getFaculty();
        Faculty f2 = duty2.getFaculty();

        // Check if swap violates leave dates
        if (f1.isOnLeave(duty2.getTimeSlot().getDate()) || f2.isOnLeave(duty1.getTimeSlot().getDate())) {
            return false; // Swap rejected
        }

        // Swap faculty references
        duty1.setFaculty(f2);
        duty2.setFaculty(f1);
        return true;
    }
}
