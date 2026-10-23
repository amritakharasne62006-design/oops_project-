package com.examscheduler;

import com.examscheduler.model.*;
import com.examscheduler.strategy.*;
import com.examscheduler.service.*;
import com.examscheduler.validation.ConflictChecker;
import com.examscheduler.report.ReportGenerator;
import com.examscheduler.util.FileHandler;
import com.examscheduler.exception.*;

import java.io.IOException;
import java.util.*;

/**
 * Main application class providing an interactive console menu.
 * Serves as the complete entry point for the Exam Timetable & Seating Generator.
 */
public class Main {

    // In-memory data repositories
    private static List<Course> courseList = new ArrayList<>();
    private static List<Student> studentList = new ArrayList<>();
    private static List<Room> roomList = new ArrayList<>();
    private static List<Faculty> facultyList = new ArrayList<>();
    private static List<TimeSlot> slotList = new ArrayList<>();
    private static List<String> holidayList = new ArrayList<>();

    // Generated state
    private static List<Exam> scheduledExams = new ArrayList<>();
    private static Map<String, List<SeatAllocation>> roomAllocations = new LinkedHashMap<>();
    private static List<InvigilatorDuty> assignedDuties = new ArrayList<>();
    private static SeatingService seatingService;

    public static void main(String[] args) {
        // Initialize default sample data for instant demo
        initializeSampleData();
        seatingService = new SeatingService(roomList);

        Scanner scanner = new Scanner(System.in);
        boolean exitProgram = false;

        System.out.println("====================================================================");
        System.out.println("  WELCOME TO EXAM TIMETABLE & SEATING GENERATOR SYSTEM (JAVA OOP)  ");
        System.out.println("  PCCoE Pune - Department of Information Technology                 ");
        System.out.println("====================================================================");

        while (!exitProgram) {
            printMenu();
            System.out.print("\nEnter your choice (1-12): ");

            String choiceStr = scanner.nextLine().trim();
            int choice = -1;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Please enter a valid number from 1 to 12.");
                continue;
            }

            switch (choice) {
                case 1:
                    displayStudentsAndCourses();
                    break;
                case 2:
                    displayRoomsAndFaculty();
                    break;
                case 3:
                    generateTimetable();
                    break;
                case 4:
                    configureAndRunSeating(scanner);
                    break;
                case 5:
                    allocateInvigilators();
                    break;
                case 6:
                    viewMasterTimetable();
                    break;
                case 7:
                    viewRoomGrid(scanner);
                    break;
                case 8:
                    searchHallTicket(scanner);
                    break;
                case 9:
                    viewDutyRoster();
                    break;
                case 10:
                    runConflictAudit();
                    break;
                case 11:
                    exportDataToFiles();
                    break;
                case 12:
                    System.out.println("\nThank you for using Exam Timetable & Seating Generator. Goodbye!");
                    exitProgram = true;
                    break;
                default:
                    System.out.println("[WARNING] Invalid option! Please select between 1 and 12.");
            }
        }

        scanner.close();
    }

    /**
     * Prints the interactive console menu.
     */
    private static void printMenu() {
        System.out.println("\n----------------------------- MAIN MENU -----------------------------");
        System.out.println(" 1. View Registered Students & Enrolled Courses");
        System.out.println(" 2. View Available Examination Rooms & Faculty");
        System.out.println(" 3. [MODULE 2] Generate Clash-Free Timetable (Graph Colouring)");
        System.out.println(" 4. [MODULE 3] Configure & Generate Seating (Strategy Pattern)");
        System.out.println(" 5. [MODULE 4] Allocate Invigilators (PriorityQueue)");
        System.out.println(" 6. [REPORT] View Master Exam Timetable");
        System.out.println(" 7. [REPORT] View Visual Room Seating Grid");
        System.out.println(" 8. [REPORT] Search Student Hall Ticket (by PRN)");
        System.out.println(" 9. [REPORT] View Faculty Invigilation Duty Roster");
        System.out.println("10. [VALIDATION] Run Conflict & Constraint Audit");
        System.out.println("11. [FILE I/O] Export Timetable & Seating to CSV Files");
        System.out.println("12. Exit Application");
        System.out.println("---------------------------------------------------------------------");
    }

    /**
     * Populates initial sample data for demonstration.
     */
    private static void initializeSampleData() {
        // 1. Courses
        Course c1 = new Course("CS201", "Object Oriented Programming", 120, "Theory");
        Course c2 = new Course("CS202", "Data Structures & Algorithms", 120, "Theory");
        Course c3 = new Course("IT203", "Database Management Systems", 120, "Theory");
        Course c4 = new Course("IT204", "Discrete Mathematics", 120, "Theory");
        Course c5 = new Course("BS205", "Environmental Science", 90, "Theory");

        courseList.add(c1);
        courseList.add(c2);
        courseList.add(c3);
        courseList.add(c4);
        courseList.add(c5);

        // 2. Students (with overlapping courses to test graph colouring)
        Student s1 = new Student("101", "Aarav Sharma", 19, "A", 3, "IT");
        Student s2 = new Student("102", "Ananya Verma", 20, "A", 3, "IT");
        Student s3 = new Student("103", "Rohan Kulkarni", 19, "A", 3, "IT");
        Student s4 = new Student("104", "Isha Deshmukh", 19, "A", 3, "IT");
        Student s5 = new Student("105", "Tanvi Patil", 20, "A", 3, "IT");

        Student s6 = new Student("106", "Siddharth Joshi", 19, "B", 3, "IT");
        Student s7 = new Student("107", "Diya Mehta", 20, "B", 3, "IT");
        Student s8 = new Student("108", "Kabir Shinde", 19, "B", 3, "IT");
        Student s9 = new Student("109", "Pooja Gaikwad", 20, "B", 3, "IT");
        Student s10 = new Student("110", "Aditya More", 19, "B", 3, "IT");

        // Batch 1 enrollments: CS201, CS202, IT203
        for (Student s : Arrays.asList(s1, s2, s3, s4, s5)) {
            s.enrollCourse(c1);
            s.enrollCourse(c2);
            s.enrollCourse(c3);
        }

        // Batch 2 enrollments: IT203, IT204, BS205 (Notice IT203 connects both batches!)
        for (Student s : Arrays.asList(s6, s7, s8, s9, s10)) {
            s.enrollCourse(c3);
            s.enrollCourse(c4);
            s.enrollCourse(c5);
        }

        studentList.addAll(Arrays.asList(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10));

        // 3. Examination Rooms
        roomList.add(new Room("Room-101", 12, 3, 4, true));
        roomList.add(new Room("Room-102", 15, 3, 5, true));
        roomList.add(new Room("Auditorium", 30, 5, 6, true));

        // 4. Faculty Members
        Faculty f1 = new Faculty("F001", "Dr. S. Sharma", 45, "Computer");
        Faculty f2 = new Faculty("F002", "Prof. P. Kulkarni", 38, "Information Technology");
        Faculty f3 = new Faculty("F003", "Prof. R. Patil", 35, "Mathematics");
        Faculty f4 = new Faculty("F004", "Dr. A. Deshmukh", 50, "Science");

        // Sample leave
        f3.addLeaveDate("2026-11-04");

        facultyList.addAll(Arrays.asList(f1, f2, f3, f4));

        // 5. Time Slots (Day 1: 2 slots, Day 2: 2 slots, Day 3: 2 slots)
        slotList.add(new TimeSlot(1, "2026-11-02", "10:00 AM", "12:00 PM", "Day 1 Morning"));
        slotList.add(new TimeSlot(2, "2026-11-02", "02:00 PM", "04:00 PM", "Day 1 Afternoon"));
        slotList.add(new TimeSlot(3, "2026-11-03", "10:00 AM", "12:00 PM", "Day 2 Morning"));
        slotList.add(new TimeSlot(4, "2026-11-03", "02:00 PM", "04:00 PM", "Day 2 Afternoon"));
        slotList.add(new TimeSlot(5, "2026-11-04", "10:00 AM", "12:00 PM", "Day 3 Morning"));

        // Holidays
        holidayList.add("2026-11-01"); // Sunday
    }

    private static void displayStudentsAndCourses() {
        System.out.println("\n=== REGISTERED COURSES ===");
        for (Course c : courseList) {
            System.out.println(" * " + c);
        }

        System.out.println("\n=== REGISTERED STUDENTS ===");
        for (Student s : studentList) {
            s.displayDetails();
            System.out.println();
        }
    }

    private static void displayRoomsAndFaculty() {
        System.out.println("\n=== EXAMINATION ROOMS ===");
        for (Room r : roomList) {
            System.out.println(" * " + r);
        }

        System.out.println("\n=== REGISTERED FACULTY ===");
        for (Faculty f : facultyList) {
            f.displayDetails();
            System.out.println();
        }
    }

    private static void generateTimetable() {
        System.out.println("\nExecuting Graph Colouring Algorithm for clash-free schedule...");
        TimetableGenerator generator = new TimetableGenerator(courseList, studentList, slotList);

        try {
            scheduledExams = generator.generateSchedule();
            System.out.println("[SUCCESS] Clash-free timetable generated successfully! Total exams scheduled: " + scheduledExams.size());
            viewMasterTimetable();
        } catch (SchedulingClashException e) {
            System.err.println("[CLASH ERROR] " + e.getMessage());
        }
    }

    private static void configureAndRunSeating(Scanner scanner) {
        if (scheduledExams.isEmpty()) {
            System.out.println("[WARNING] Please generate the timetable (Option 3) first!");
            return;
        }

        System.out.println("\nChoose Seating Strategy (Strategy Pattern Demonstration):");
        System.out.println("1. Mixed-Subject Interleaved (Anti-Malpractice, Recommended)");
        System.out.println("2. Zig-Zag Alternating Snake");
        System.out.println("3. Sequential (Row-by-Row)");
        System.out.println("4. Randomized Seating");
        System.out.print("Select strategy (1-4): ");

        String stratChoice = scanner.nextLine().trim();
        switch (stratChoice) {
            case "2":
                seatingService.setStrategy(new ZigZagSeating());
                break;
            case "3":
                seatingService.setStrategy(new SequentialSeating());
                break;
            case "4":
                seatingService.setStrategy(new RandomSeating());
                break;
            default:
                seatingService.setStrategy(new MixedSubjectSeating());
        }

        System.out.println("[ACTIVE STRATEGY] " + seatingService.getStrategy().getStrategyName());

        try {
            // Group exams by TimeSlot
            Map<TimeSlot, List<Exam>> slotGroups = new HashMap<>();
            for (Exam e : scheduledExams) {
                slotGroups.putIfAbsent(e.getTimeSlot(), new ArrayList<>());
                slotGroups.get(e.getTimeSlot()).add(e);
            }

            roomAllocations.clear();
            for (Map.Entry<TimeSlot, List<Exam>> entry : slotGroups.entrySet()) {
                Map<String, List<SeatAllocation>> slotAlloc = seatingService.generateSeatingForSlot(entry.getValue());
                for (Map.Entry<String, List<SeatAllocation>> roomEntry : slotAlloc.entrySet()) {
                    roomAllocations.putIfAbsent(roomEntry.getKey(), new ArrayList<>());
                    roomAllocations.get(roomEntry.getKey()).addAll(roomEntry.getValue());
                }
            }

            System.out.println("[SUCCESS] Seating arrangement generated for all rooms and slots!");
        } catch (RoomCapacityExceededException e) {
            System.err.println("[CAPACITY ERROR] " + e.getMessage());
        }
    }

    private static void allocateInvigilators() {
        if (scheduledExams.isEmpty()) {
            System.out.println("[WARNING] Please generate the timetable (Option 3) first!");
            return;
        }

        System.out.println("\nAllocating invigilators using PriorityQueue fairness algorithm...");
        // Reset duty counts prior to fresh run
        for (Faculty f : facultyList) {
            f.resetDuty();
        }

        InvigilatorService invigilatorService = new InvigilatorService(facultyList);
        try {
            assignedDuties = invigilatorService.allocateInvigilators(scheduledExams);
            System.out.println("[SUCCESS] Invigilators allocated successfully! Total duties assigned: " + assignedDuties.size());
            viewDutyRoster();
        } catch (InsufficientInvigilatorsException e) {
            System.err.println("[INVIGILATOR ERROR] " + e.getMessage());
        }
    }

    private static void viewMasterTimetable() {
        if (scheduledExams.isEmpty()) {
            System.out.println("[INFO] No timetable generated yet. Please run Option 3.");
            return;
        }
        ReportGenerator.printMasterTimetable(scheduledExams);
    }

    private static void viewRoomGrid(Scanner scanner) {
        if (roomAllocations.isEmpty()) {
            System.out.println("[INFO] No seating arrangement generated yet. Please run Option 4.");
            return;
        }

        System.out.println("\nAvailable Rooms:");
        for (int i = 0; i < roomList.size(); i++) {
            System.out.println((i + 1) + ". " + roomList.get(i).getRoomNo());
        }
        System.out.print("Select room number to display grid: ");
        String roomChoice = scanner.nextLine().trim();

        Room chosenRoom = null;
        for (Room r : roomList) {
            if (r.getRoomNo().equalsIgnoreCase(roomChoice)) {
                chosenRoom = r;
                break;
            }
        }

        if (chosenRoom == null) {
            int idx = -1;
            try {
                idx = Integer.parseInt(roomChoice) - 1;
                if (idx >= 0 && idx < roomList.size()) {
                    chosenRoom = roomList.get(idx);
                }
            } catch (NumberFormatException ignored) {}
        }

        if (chosenRoom != null) {
            List<SeatAllocation> allocs = roomAllocations.getOrDefault(chosenRoom.getRoomNo(), new ArrayList<>());
            seatingService.printRoomGrid(chosenRoom, allocs);
        } else {
            System.out.println("[ERROR] Room not found.");
        }
    }

    private static void searchHallTicket(Scanner scanner) {
        if (scheduledExams.isEmpty() || roomAllocations.isEmpty()) {
            System.out.println("[INFO] Please generate timetable (Option 3) and seating (Option 4) first.");
            return;
        }

        System.out.print("\nEnter Student PRN to search (e.g. 101, 102, 106): ");
        String prn = scanner.nextLine().trim();

        Student target = null;
        for (Student s : studentList) {
            if (s.getPrn().equalsIgnoreCase(prn)) {
                target = s;
                break;
            }
        }

        if (target != null) {
            ReportGenerator.printHallTicket(target, scheduledExams, roomAllocations);
        } else {
            System.out.println("[ERROR] No student found with PRN: " + prn);
        }
    }

    private static void viewDutyRoster() {
        if (assignedDuties.isEmpty()) {
            System.out.println("[INFO] No duty roster available. Please run Option 5.");
            return;
        }
        ReportGenerator.printDutyChart(assignedDuties, facultyList);
    }

    private static void runConflictAudit() {
        System.out.println("\nAuditing schedule for hard conflicts and soft warnings...");
        ConflictChecker checker = new ConflictChecker(scheduledExams, studentList, assignedDuties, holidayList);
        List<String> conflicts = checker.runFullAudit();

        if (conflicts.isEmpty()) {
            System.out.println("[SUCCESS] PERFECT SCHEDULE! Zero hard clashes found.");
        } else {
            System.out.println("AUDIT REPORT (" + conflicts.size() + " findings):");
            for (String issue : conflicts) {
                System.out.println(" -> " + issue);
            }
        }
        ReportGenerator.printStatistics(scheduledExams, roomList, facultyList);
    }

    private static void exportDataToFiles() {
        if (scheduledExams.isEmpty()) {
            System.out.println("[WARNING] Generate timetable (Option 3) and seating (Option 4) before exporting.");
            return;
        }

        try {
            FileHandler.exportTimetableToCSV(scheduledExams, "reports/timetable.csv");

            List<SeatAllocation> allAllocations = new ArrayList<>();
            for (List<SeatAllocation> list : roomAllocations.values()) {
                allAllocations.addAll(list);
            }
            FileHandler.exportSeatingToCSV(allAllocations, "reports/seating_plan.csv");

            System.out.println("[COMPLETED] All files successfully exported to 'reports/' directory using BufferedWriter!");
        } catch (IOException e) {
            System.err.println("[FILE ERROR] " + e.getMessage());
        }
    }
}
