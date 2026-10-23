package com.examscheduler.report;

import com.examscheduler.model.*;

import java.util.*;

/**
 * ============================================================================
 * CLASS: ReportGenerator
 * ============================================================================
 * WHAT IT DOES:
 *   Formats and displays college reports in structured, readable tables:
 *   1. Master Timetable (Date and slot wise)
 *   2. Student Hall Ticket (Searchable by PRN)
 *   3. Room Seating Charts
 *   4. Attendance Sheets with signature columns
 *   5. Invigilator Duty Charts & Workload Statistics
 * ============================================================================
 */
public class ReportGenerator {

    // ------------------------------------------------------------------------
    // 1. MASTER TIMETABLE REPORT
    // ------------------------------------------------------------------------
    public static void printMasterTimetable(List<Exam> exams) {
        System.out.println("\n==========================================================================");
        System.out.println("                         MASTER EXAM TIMETABLE                            ");
        System.out.println("==========================================================================");
        System.out.printf("%-12s | %-12s | %-10s | %-30s | %-10s\n", "DATE", "TIME SLOT", "COURSE", "SUBJECT NAME", "STUDENTS");
        System.out.println("--------------------------------------------------------------------------");

        // Sort exams chronologically by date and start time
        List<Exam> sortedExams = new ArrayList<>(exams);
        sortedExams.sort(Comparator.comparing((Exam e) -> e.getTimeSlot().getDate())
                                   .thenComparing(e -> e.getTimeSlot().getStartTime()));

        for (Exam e : sortedExams) {
            System.out.printf("%-12s | %-12s | %-10s | %-30s | %-10d\n",
                    e.getTimeSlot().getDate(),
                    e.getTimeSlot().getStartTime(),
                    e.getCourse().getCode(),
                    e.getCourse().getName(),
                    e.getEligibleStudents().size());
        }
        System.out.println("==========================================================================\n");
    }

    // ------------------------------------------------------------------------
    // 2. STUDENT HALL TICKET (Personalized by PRN)
    // ------------------------------------------------------------------------
    public static void printHallTicket(Student student, List<Exam> allExams, Map<String, List<SeatAllocation>> roomAllocations) {
        System.out.println("\n==========================================================================");
        System.out.println("                   PCCoE PUNE - STUDENT HALL TICKET                       ");
        System.out.println("==========================================================================");
        System.out.println("Student Name : " + student.getName());
        System.out.println("PRN          : " + student.getPrn());
        System.out.println("Department   : " + student.getDepartment() + " (Div " + student.getDivision() + ")");
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-10s | %-25s | %-12s | %-12s | %-12s | %-8s\n", "COURSE", "COURSE NAME", "DATE", "TIME", "ROOM", "SEAT");
        System.out.println("--------------------------------------------------------------------------");

        for (Exam e : allExams) {
            if (e.getEligibleStudents().contains(student)) {
                String assignedRoom = "TBD";
                String assignedSeat = "TBD";

                // Lookup room and seat label from allocations
                for (Map.Entry<String, List<SeatAllocation>> entry : roomAllocations.entrySet()) {
                    for (SeatAllocation sa : entry.getValue()) {
                        if (sa.getStudent().getPrn().equalsIgnoreCase(student.getPrn()) &&
                            sa.getCourse().getCode().equalsIgnoreCase(e.getCourse().getCode())) {
                            assignedRoom = entry.getKey();
                            assignedSeat = sa.getSeatLabel();
                            break;
                        }
                    }
                }

                System.out.printf("%-10s | %-25s | %-12s | %-12s | %-12s | %-8s\n",
                        e.getCourse().getCode(),
                        e.getCourse().getName(),
                        e.getTimeSlot().getDate(),
                        e.getTimeSlot().getStartTime(),
                        assignedRoom,
                        assignedSeat);
            }
        }
        System.out.println("==========================================================================\n");
    }

    // ------------------------------------------------------------------------
    // 3. FACULTY INVIGILATION DUTY CHART
    // ------------------------------------------------------------------------
    public static void printDutyChart(List<InvigilatorDuty> duties, List<Faculty> facultyList) {
        System.out.println("\n==========================================================================");
        System.out.println("                      FACULTY INVIGILATION DUTY CHART                     ");
        System.out.println("==========================================================================");
        System.out.printf("%-10s | %-20s | %-12s | %-12s | %-12s | %-10s\n", "FACULTY ID", "NAME", "DATE", "TIME", "ROOM", "COURSE");
        System.out.println("--------------------------------------------------------------------------");

        for (InvigilatorDuty d : duties) {
            System.out.printf("%-10s | %-20s | %-12s | %-12s | %-12s | %-10s\n",
                    d.getFaculty().getId(),
                    d.getFaculty().getName(),
                    d.getTimeSlot().getDate(),
                    d.getTimeSlot().getStartTime(),
                    d.getRoomNo(),
                    d.getExam().getCourse().getCode());
        }
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("DUTY SUMMARY (Fairness Verification):");
        for (Faculty f : facultyList) {
            System.out.println(" -> " + f.getName() + " (" + f.getId() + ") - Total Duties Assigned: " + f.getDutyCount());
        }
        System.out.println("==========================================================================\n");
    }

    // ------------------------------------------------------------------------
    // 4. EXAMINATION ATTENDANCE SHEET
    // ------------------------------------------------------------------------
    public static void printAttendanceSheet(String roomNo, Course course, List<SeatAllocation> allocations) {
        System.out.println("\n==========================================================================");
        System.out.println("                    EXAMINATION ATTENDANCE SHEET                          ");
        System.out.println("Room: " + roomNo + " | Course: " + course.getCode() + " - " + course.getName());
        System.out.println("==========================================================================");
        System.out.printf("%-8s | %-12s | %-25s | %-18s\n", "SEAT", "PRN", "STUDENT NAME", "SIGNATURE");
        System.out.println("--------------------------------------------------------------------------");

        for (SeatAllocation sa : allocations) {
            if (sa.getRoomNo().equalsIgnoreCase(roomNo) && sa.getCourse().getCode().equalsIgnoreCase(course.getCode())) {
                System.out.printf("%-8s | %-12s | %-25s | %-18s\n",
                        sa.getSeatLabel(),
                        sa.getStudent().getPrn(),
                        sa.getStudent().getName(),
                        "[                ]");
            }
        }
        System.out.println("==========================================================================\n");
    }

    // ------------------------------------------------------------------------
    // 5. OVERALL SESSION SYSTEM STATISTICS
    // ------------------------------------------------------------------------
    public static void printStatistics(List<Exam> exams, List<Room> rooms, List<Faculty> facultyList) {
        Set<String> uniqueDates = new HashSet<>();
        int totalStudentAppearances = 0;
        for (Exam e : exams) {
            uniqueDates.add(e.getTimeSlot().getDate());
            totalStudentAppearances += e.getEligibleStudents().size();
        }

        int totalRoomCapacity = 0;
        for (Room r : rooms) {
            totalRoomCapacity += r.getCapacity();
        }

        System.out.println("\n==========================================================================");
        System.out.println("                      EXAM SESSION SYSTEM STATISTICS                      ");
        System.out.println("==========================================================================");
        System.out.println("Total Scheduled Exams    : " + exams.size());
        System.out.println("Total Exam Days Used     : " + uniqueDates.size() + " " + uniqueDates);
        System.out.println("Total Student Appearances: " + totalStudentAppearances);
        System.out.println("Total Available Rooms    : " + rooms.size() + " (Capacity: " + totalRoomCapacity + " seats)");
        System.out.println("Total Registered Faculty : " + facultyList.size());
        System.out.println("==========================================================================\n");
    }
}
