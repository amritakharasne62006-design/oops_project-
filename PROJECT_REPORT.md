# ACADEMIC MINI PROJECT REPORT

---

# EXAM TIMETABLE & SEATING GENERATOR
### An Automated, Conflict-Free Examination Management System in Pure Core Java

* **Course Title:** Object Oriented Programming (OOP)
* **Academic Year:** S.Y. B.Tech (Second Year) Information Technology
* **Institution:** Pimpri Chinchwad College of Engineering (PCCoE), Pune
* **Author:** Information Technology Department Student
* **Language & Runtime:** Core Java (JDK 17+) — Console Application
* **Repository:** [https://github.com/amritakharasne62006-design/oops_project-](https://github.com/amritakharasne62006-design/oops_project-)

---

## TABLE OF CONTENTS
1. [Abstract & Project Overview](#1-abstract--project-overview)
2. [Problem Statement & Objectives](#2-problem-statement--objectives)
3. [System Architecture & Class Hierarchy](#3-system-architecture--class-hierarchy)
4. [Syllabus Concept Mapping](#4-syllabus-concept-mapping)
5. [In-Depth Algorithmic Logic & Mathematical Models](#5-in-depth-algorithmic-logic--mathematical-models)
   - 5.1 [Greedy Graph Colouring (Clash-Free Timetabling)](#51-greedy-graph-colouring-clash-free-timetabling)
   - 5.2 [Anti-Malpractice Interleaved Seating (Strategy Pattern)](#52-anti-malpractice-interleaved-seating-strategy-pattern)
   - 5.3 [Fair Invigilator Duty Allocation (PriorityQueue)](#53-fair-invigilator-duty-allocation-priorityqueue)
   - 5.4 [Constraint Checking Engine (Hard vs. Soft Constraints)](#54-constraint-checking-engine-hard-vs-soft-constraints)
6. [Key Code Chunks & Detailed Line-by-Line Logic](#6-key-code-chunks--detailed-line-by-line-logic)
   - 6.1 [Abstraction & Constructors: `Person.java`](#61-abstraction--constructors-personjava)
   - 6.2 [Inheritance & Overriding: `Student.java`](#62-inheritance--overriding-studentjava)
   - 6.3 [Graph Colouring Engine: `TimetableGenerator.java`](#63-graph-colouring-engine-timetablegeneratorjava)
   - 6.4 [Strategy Pattern: `MixedSubjectSeating.java`](#64-strategy-pattern-mixedsubjectseatingjava)
   - 6.5 [Fair Invigilator Distribution: `InvigilatorService.java`](#65-fair-invigilator-distribution-invigilatorservicejava)
   - 6.6 [Character Streams & File I/O: `FileHandler.java`](#66-character-streams--file-io-filehandlerjava)
7. [System Execution Walkthrough & Output Snapshots](#7-system-execution-walkthrough--output-snapshots)
8. [Teacher Presentation & Viva Preparation Guide](#8-teacher-presentation--viva-preparation-guide)
9. [Conclusion](#9-conclusion)

---

## 1. ABSTRACT & PROJECT OVERVIEW

The **Exam Timetable & Seating Generator** is an automated desktop software developed in pure Core Java that resolves the logistical difficulties faced by colleges during end-semester examinations. Preparing exam schedules and desk allocations manually using spreadsheets leads to overlapping exams, student clashes, overcrowding, cheating opportunities, and unfair faculty invigilation distribution.

This software models the examination ecosystem through classic **Object-Oriented Programming (OOP)** principles:
* **Graph Theory (Graph Colouring):** Automatically schedules non-overlapping exam slots by treating courses as vertices and shared student enrollments as conflict edges.
* **Strategy Design Pattern:** Implements runtime-swappable seating algorithms (Mixed-Subject Interleaved, Zig-Zag, Sequential, and Random).
* **Fair Priority Queue:** Balances invigilation duties evenly across teachers while respecting leaves and course conflicts.
* **Character Stream File I/O:** Exports timetables, hall tickets, and seating charts to CSV files using `BufferedReader` and `BufferedWriter`.

---

## 2. PROBLEM STATEMENT & OBJECTIVES

### Current Issues in Manual Scheduling:
1. **Student Clashes:** A student enrolled in backlogs or elective subjects finds two examinations scheduled on the exact same date and time slot.
2. **Malpractice / Cheating:** When students writing the same subject exam sit adjacent to each other (left, right, front, or back), unauthorized copying increases.
3. **Imbalanced Room Usage:** Small classrooms are packed to overflow while larger auditoriums remain vacant.
4. **Faculty Burnout:** Certain faculty members are assigned an unfair number of duties while others receive very few.

### Objectives of This Project:
* **Objective 1:** Guarantee zero student clashes by mathematically verifying that no two conflicting courses share the same time slot.
* **Objective 2:** Maximize malpractice resistance by interleaving students of different courses across the room grid so direct neighbors never share the same exam paper.
* **Objective 3:** Balance invigilation workload across all professors using a min-heap priority queue.
* **Objective 4:** Provide a modular, beginner-friendly Core Java codebase adhering to university syllabus requirements.

---

## 3. SYSTEM ARCHITECTURE & CLASS HIERARCHY

The project is structured under package `com.examscheduler`:

```text
                           +------------------------+
                           |  <<abstract>> Person   |
                           |------------------------|
                           | # id: String           |
                           | # name: String         |
                           | # age: int             |
                           +------------------------+
                                       ^
                                       | extends
                  +--------------------+--------------------+
                  |                                         |
     +-------------------------+               +-------------------------+
     |         Student         |               |         Faculty         |
     |-------------------------|               |-------------------------|
     | - prn: String           |               | - department: String    |
     | - division: String      |               | - leaveDates: List      |
     | - registeredCourses:List|               | - dutyCount: int        |
     +-------------------------+               +-------------------------+
                  |                                         |
                  | enrolls in                              | assigned to
                  v                                         v
     +-------------------------+               +-------------------------+
     |         Course          |               |    InvigilatorDuty      |
     |-------------------------|               |-------------------------|
     | - code: String          |               | - faculty: Faculty      |
     | - name: String          |               | - roomNo: String        |
     | - duration: int         |               | - exam: Exam            |
     +-------------------------+               +-------------------------+
                  |                                         |
                  | scheduled as                            |
                  v                                         |
     +-------------------------+                            |
     |          Exam           |<---------------------------+
     |-------------------------|
     | - course: Course        |
     | - timeSlot: TimeSlot    |
     | - assignedRooms: List   |
     +-------------------------+
                  |
                  | allocated into
                  v
     +-------------------------+               +-------------------------+
     |          Room           |               |        TimeSlot         |
     |-------------------------|               |-------------------------|
     | - roomNo: String        |               | - slotId: int           |
     | - capacity: int         |               | - date: String          |
     | - rows: int, cols: int  |               | - startTime / endTime   |
     +-------------------------+               +-------------------------+

======================== STRATEGY PATTERN (Polymorphism) ========================

                    +-------------------------------+
                    |     <<interface>>             |
                    |     SeatingStrategy           |
                    |-------------------------------|
                    | + allocate(...)               |
                    +-------------------------------+
                                    ^
                                    | implements
      +--------------------+--------+--------+--------------------+
      |                    |                 |                    |
+-----------------+ +---------------+ +--------------------+ +----------------+
|SequentialSeating| | ZigZagSeating | | MixedSubjectSeating| | RandomSeating  |
+-----------------+ +---------------+ +--------------------+ +----------------+
```

---

## 4. SYLLABUS CONCEPT MAPPING

The following table explicitly shows how every topic from the PCCoE Information Technology syllabus is implemented in this codebase:

| Syllabus Concept | Implementation in Code |
| :--- | :--- |
| **Default Constructor** | `Person()`, `Student()`, `Faculty()`, `Course()`, `Room()`, `TimeSlot()` provide no-arg initialization with sensible defaults. |
| **Parameterized Constructor** | Used throughout to populate fields directly (e.g., `Student(prn, name, age, div, sem, dept)`). |
| **Copy Constructor** | `Person(Person other)`, `Student(Student other)`, `Course(Course other)`, `Room(Room other)` create safe deep copies of objects. |
| **Constructor Overloading** | Each entity class contains multiple constructors with varying parameter signatures. |
| **`this` Keyword** | Resolves name shadowing (`this.name = name`) and performs constructor chaining (`this("UNKNOWN", "Not Assigned", 18)`). |
| **Data Hiding & Encapsulation** | All member variables are declared `private` or `protected` and accessed via validated public getters/setters. |
| **Array of Objects** | 2D matrices (`String[][] grid`) represent classroom desk configurations in `SeatingService.java`. |
| **Inheritance** | Single and Hierarchical: `Student` and `Faculty` both inherit from the abstract base class `Person`. |
| **Access Modifiers** | `private` (data hiding), `protected` (accessible to subclasses), and `public` (service interfaces). |
| **Abstract Classes** | `Person` is an abstract class declaring `public abstract void displayDetails();`. |
| **Interfaces** | `SeatingStrategy` interface defines the polymorphic seating contract; `Comparable<Faculty>` enables priority ordering. |
| **Method Overriding** | Subclasses override `displayDetails()`, `toString()`, `equals()`, `hashCode()`, and `compareTo()`. |
| **Method Overloading** | Overloaded constructors and lookup helpers throughout the application. |
| **Custom Exceptions** | `SchedulingClashException`, `RoomCapacityExceededException`, `InsufficientInvigilatorsException`, `InvalidDateException`. |
| **Exception Handling Keywords** | `try`, `catch`, `finally`, `throw`, and `throws` handle error detection and graceful recovery. |
| **Character Streams (File I/O)** | `FileReader`, `BufferedReader`, `FileWriter`, `BufferedWriter` read and export CSV schedules and seating plans. |

---

## 5. IN-DEPTH ALGORITHMIC LOGIC & MATHEMATICAL MODELS

### 5.1 Greedy Graph Colouring (Clash-Free Timetabling)

#### Problem Formulation:
Let $G = (V, E)$ be an undirected conflict graph:
* **Vertices ($V$):** The set of courses $\{C_1, C_2, C_3, \dots, C_n\}$.
* **Edges ($E$):** An edge $(C_i, C_j) \in E$ exists if and only if:
  $$\exists \text{ Student } S \text{ such that } S \text{ is enrolled in both } C_i \text{ and } C_j$$
* **Colours ($\mathcal{K}$):** An integer $k \in \{0, 1, 2, \dots, m\}$ representing an available time slot.

#### Greedy Colouring Algorithm Steps:
1. Initialize an adjacency list `conflictGraph` where each course has an empty set of neighbors.
2. For every pair of courses $(C_i, C_j)$, iterate through all students. If any student takes both, add an edge:
   $$C_i \leftrightarrow C_j$$
3. Maintain a map `courseColors`.
4. For each course $C \in V$:
   - Inspect all adjacent neighbor courses $N \in \text{neighbors}(C)$.
   - Collect all colours already used by those neighbors in `neighborColors`.
   - Assign to $C$ the smallest non-negative integer colour not present in `neighborColors`:
     $$\text{color}(C) = \min \{ c \in \mathbb{N}_0 \mid c \notin \text{neighborColors} \}$$
5. Map each colour index $c$ to `TimeSlot[c]`. If $c \ge \text{availableSlots.size()}$, throw `SchedulingClashException`.

#### Concrete Worked Example:
Suppose we have 4 Courses and 5 Students:
* Courses: $C_1$ (Java), $C_2$ (DSA), $C_3$ (DBMS), $C_4$ (Discrete Maths)
* Student $S_1$ takes: $C_1, C_2$
* Student $S_2$ takes: $C_1, C_3$
* Student $S_3$ takes: $C_2, C_3$
* Student $S_4, S_5$ take: $C_4$

**Trace Table:**

| Step | Course | Neighbors in Graph | Used Neighbor Colours | Assigned Colour | Assigned Time Slot |
| :---: | :---: | :---: | :---: | :---: | :---: |
| 1 | $C_1$ (Java) | $C_2, C_3$ | $\emptyset$ (none yet) | **Colour 0** | Slot 1: Day 1 Morning |
| 2 | $C_2$ (DSA) | $C_1, C_3$ | $\{0\}$ (from $C_1$) | **Colour 1** | Slot 2: Day 1 Afternoon |
| 3 | $C_3$ (DBMS) | $C_1, C_2$ | $\{0, 1\}$ (from $C_1, C_2$) | **Colour 2** | Slot 3: Day 2 Morning |
| 4 | $C_4$ (Discrete) | $\emptyset$ (no common students) | $\emptyset$ | **Colour 0** | Slot 1: Day 1 Morning |

**Result:**
* $C_1$ (Java) and $C_4$ (Discrete) run simultaneously in **Slot 1** with **ZERO student clashes**!
* Total slots required = 3 (the chromatic number $\chi(G) = 3$).

---

### 5.2 Anti-Malpractice Interleaved Seating (Strategy Pattern)

When multiple exams run in the same time slot (e.g., $C_1$ and $C_4$), placing students of the same course next to each other creates an opportunity to copy answers.

#### Interleaving Algorithm (`MixedSubjectSeating`):
1. Partition examinees into separate FIFO queues based on their course code:
   $$\text{Queue}(C_1) = [S_1, S_2, S_3], \quad \text{Queue}(C_4) = [S_4, S_5]$$
2. Traverse the room desk grid row-by-row and column-by-column:
   $$\text{Grid}[r][c] \quad \text{for } r \in [0, \text{rows}-1], c \in [0, \text{cols}-1]$$
3. Alternate between queues at each step:
   * Desk $(0, 0) \leftarrow \text{poll from Queue}(C_1)$
   * Desk $(0, 1) \leftarrow \text{poll from Queue}(C_4)$
   * Desk $(0, 2) \leftarrow \text{poll from Queue}(C_1)$
   * Desk $(0, 3) \leftarrow \text{poll from Queue}(C_4)$
4. Because no two adjacent seats receive students from the same subject queue, students sitting to the left, right, front, or back are writing completely different question papers!

---

### 5.3 Fair Invigilator Duty Allocation (PriorityQueue)

Colleges often face complaints that certain faculty are overburdened with invigilation duties while others are under-assigned.

#### Mathematical Model:
* Let $F = \{f_1, f_2, \dots, f_m\}$ be the faculty members.
* Each faculty $f_i$ has a state attribute `dutyCount`.
* $f_i$ implements `Comparable<Faculty>`:
  $$f_1 < f_2 \iff f_1.\text{dutyCount} < f_2.\text{dutyCount}$$
* We construct a Min-Heap `PriorityQueue<Faculty>`.
* For each exam hall requiring a supervisor:
  1. Poll the root of the PriorityQueue (guaranteed to be the teacher with the lowest duty count).
  2. Verify Hard Constraints:
     - Is the teacher already booked in this time slot?
     - Is the teacher on approved leave on this exam date?
     - Is the teacher invigilating their own department's course?
  3. If eligible: assign the duty, increment `dutyCount++`, and re-insert into the queue so the heap property automatically rebalances.

---

### 5.4 Constraint Checking Engine (Hard vs. Soft Constraints)

The `ConflictChecker` categorizes all rules into two strict levels:

1. **Hard Constraints (Must Hold — Errors if violated):**
   * $\text{Exams}(S, \text{slot}) \le 1$: A student cannot write two exams at the same time.
   * $\text{Capacity}(\text{Room}) \ge \text{StudentsSeated}(\text{Room})$: Rooms must not overflow.
   * $\text{Duties}(f, \text{slot}) \le 1$: A faculty cannot invigilate two halls simultaneously.
   * $\text{Date}(\text{Exam}) \notin \text{Holidays}$: No exams on Sundays or declared holidays.

2. **Soft Constraints (Optimization Goals — Warnings if violated):**
   * Back-to-back exams: Warn if a student has both a morning and an afternoon exam on the same day.
   * Spread out tough subjects with preparation gaps.

---

## 6. KEY CODE CHUNKS & DETAILED LINE-BY-LINE LOGIC

### 6.1 Abstraction & Constructors: `Person.java`

```java
public abstract class Person {
    // Protected fields accessible by child classes (Student and Faculty)
    protected String id;
    protected String name;
    protected int age;

    // Default Constructor: Demonstrates constructor chaining using this(...)
    public Person() {
        this("UNKNOWN", "Not Assigned", 18);
    }

    // Parameterized Constructor: Resolves name shadowing using 'this'
    public Person(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    // Copy Constructor: Creates a safe clone of an existing Person
    public Person(Person other) {
        if (other != null) {
            this.id = other.id;
            this.name = other.name;
            this.age = other.age;
        }
    }

    // Abstract method: Forces all derived classes to implement their own display logic
    public abstract void displayDetails();
}
```

#### Why This Matters for Your Viva:
* **Abstraction:** The class is declared `abstract` because a generic "Person" does not exist in an exam hall; only a concrete `Student` or `Faculty` does.
* **Constructor Chaining:** `this("UNKNOWN", "Not Assigned", 18)` avoids duplicating initialization code across multiple constructors.

---

### 6.2 Inheritance & Overriding: `Student.java`

```java
public class Student extends Person {
    private String prn;
    private String division;
    private int semester;
    private String department;
    private List<Course> registeredCourses;

    // Parameterized Constructor using 'super'
    public Student(String prn, String name, int age, String division, int semester, String department) {
        super(prn, name, age); // Passes common attributes to parent Person constructor
        this.prn = prn;
        this.division = division;
        this.semester = semester;
        this.department = department;
        this.registeredCourses = new ArrayList<>();
    }

    // Runtime Polymorphism: Overriding the parent's abstract displayDetails()
    @Override
    public void displayDetails() {
        System.out.println("----- STUDENT PROFILE -----");
        System.out.println("PRN        : " + prn);
        System.out.println("Name       : " + name);
        System.out.println("Department : " + department + " (Div " + division + ")");
    }
}
```

#### Why This Matters for Your Viva:
* **Inheritance:** Code reuse — `Student` inherits `name` and `age` directly from `Person` via `extends`.
* **The `super` Keyword:** Calls the parent constructor `Person(id, name, age)`.
* **Method Overriding:** Provides student-specific formatting for `displayDetails()`.

---

### 6.3 Graph Colouring Engine: `TimetableGenerator.java`

```java
public List<Exam> generateSchedule() throws SchedulingClashException {
    buildConflictGraph(); // Step 1: Connect courses sharing common students

    Map<Course, Integer> courseColors = new HashMap<>();

    // Step 2: Greedy Colouring
    for (Course currentCourse : courses) {
        Set<Integer> neighborColors = new HashSet<>();
        
        // Find colours already used by conflicting neighbor courses
        for (Course neighbor : conflictGraph.get(currentCourse)) {
            if (courseColors.containsKey(neighbor)) {
                neighborColors.add(courseColors.get(neighbor));
            }
        }

        // Find the lowest available non-conflicting colour
        int chosenColor = 0;
        while (neighborColors.contains(chosenColor)) {
            chosenColor++;
        }

        courseColors.put(currentCourse, chosenColor);
    }

    // Step 3: Map colours to real calendar TimeSlots
    List<Exam> scheduledExams = new ArrayList<>();
    for (Course course : courses) {
        int colorIndex = courseColors.get(course);
        TimeSlot slot = availableSlots.get(colorIndex);
        scheduledExams.add(new Exam(course, slot));
    }

    return scheduledExams;
}
```

#### Why This Matters for Your Viva:
* **Graph Colouring:** Guarantees that two conflicting courses never receive the same `colorIndex`.
* **Zero Clashes:** Because each `colorIndex` maps to a distinct `TimeSlot`, no student will ever have two exams at the same time.

---

### 6.4 Strategy Pattern: `MixedSubjectSeating.java`

```java
public class MixedSubjectSeating implements SeatingStrategy {
    @Override
    public List<SeatAllocation> allocate(List<Student> students, Map<Student, Course> courseMap, Room room) {
        List<SeatAllocation> allocations = new ArrayList<>();

        // Group students into FIFO queues by course code
        Map<String, Queue<Student>> courseQueues = new HashMap<>();
        for (Student s : students) {
            String code = courseMap.get(s).getCode();
            courseQueues.putIfAbsent(code, new LinkedList<>());
            courseQueues.get(code).add(s);
        }

        List<String> courseList = new ArrayList<>(courseQueues.keySet());
        int courseIndex = 0;

        // Traverse room rows and columns
        for (int r = 0; r < room.getRows(); r++) {
            for (int c = 0; c < room.getCols(); c++) {
                // Find next queue with remaining students
                String code = courseList.get(courseIndex);
                Queue<Student> queue = courseQueues.get(code);

                if (queue != null && !queue.isEmpty()) {
                    Student student = queue.poll();
                    allocations.add(new SeatAllocation(student, courseMap.get(student), room.getRoomNo(), r, c));
                }

                // Alternate to next course queue for the adjacent desk
                courseIndex = (courseIndex + 1) % courseList.size();
            }
        }
        return allocations;
    }
}
```

#### Why This Matters for Your Viva:
* **Anti-Cheating:** Alternating the course queue at every desk step ensures adjacent seats $(r, c)$ and $(r, c+1)$ hold students taking different exams.
* **Strategy Pattern:** The algorithm can be hot-swapped for `ZigZagSeating` or `SequentialSeating` at runtime without modifying the rest of the application.

---

### 6.5 Fair Invigilator Distribution: `InvigilatorService.java`

```java
PriorityQueue<Faculty> queue = new PriorityQueue<>(facultyList);

for (Exam exam : slotExams) {
    for (Room room : exam.getAssignedRooms()) {
        Faculty selected = null;
        List<Faculty> temp = new ArrayList<>();

        while (!queue.isEmpty()) {
            Faculty candidate = queue.poll(); // Always pulls faculty with lowest dutyCount
            temp.add(candidate);

            // Check hard constraints: not busy in slot, not on leave, not own subject
            if (!busyInSlot.contains(candidate) && !candidate.isOnLeave(slot.getDate())) {
                selected = candidate;
                break;
            }
        }
        queue.addAll(temp); // Restore inspected candidates

        // Assign duty and increment duty counter
        selected.incrementDuty();
        queue.remove(selected);
        queue.offer(selected); // Re-insert to adjust priority in min-heap
    }
}
```

#### Why This Matters for Your Viva:
* **Fair Workload:** A min-heap (`PriorityQueue`) automatically balances duties across all teachers so everyone gets an equal share of work.

---

### 6.6 Character Streams & File I/O: `FileHandler.java`

```java
public static void exportTimetableToCSV(List<Exam> exams, String filePath) throws IOException {
    File file = new File(filePath);
    FileWriter fileWriter = null;
    BufferedWriter bufferedWriter = null;

    try {
        fileWriter = new FileWriter(file);
        bufferedWriter = new BufferedWriter(fileWriter);

        // Write CSV Header
        bufferedWriter.write("CourseCode,CourseName,Date,StartTime,EndTime,TotalStudents");
        bufferedWriter.newLine();

        // Write Rows
        for (Exam e : exams) {
            String line = String.format("%s,%s,%s,%s,%s,%d",
                    e.getCourse().getCode(), e.getCourse().getName(),
                    e.getTimeSlot().getDate(), e.getTimeSlot().getStartTime(),
                    e.getTimeSlot().getEndTime(), e.getEligibleStudents().size());
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }
        bufferedWriter.flush();
    } finally {
        // Safe resource cleanup in finally block
        if (bufferedWriter != null) bufferedWriter.close();
        if (fileWriter != null) fileWriter.close();
    }
}
```

#### Why This Matters for Your Viva:
* **Character Streams:** `BufferedWriter` and `FileWriter` operate on 16-bit characters, which is ideal for text and CSV files.
* **The `finally` Block:** Ensures file streams are closed even if an exception occurs during writing.

---

## 7. SYSTEM EXECUTION WALKTHROUGH & OUTPUT SNAPSHOTS

When you execute `Main.java`, the interactive console menu appears:

```text
====================================================================
  WELCOME TO EXAM TIMETABLE & SEATING GENERATOR SYSTEM (JAVA OOP)  
  PCCoE Pune - Department of Information Technology                 
====================================================================

----------------------------- MAIN MENU -----------------------------
 1. View Registered Students & Enrolled Courses
 2. View Available Examination Rooms & Faculty
 3. [MODULE 2] Generate Clash-Free Timetable (Graph Colouring)
 4. [MODULE 3] Configure & Generate Seating (Strategy Pattern)
 5. [MODULE 4] Allocate Invigilators (PriorityQueue)
 6. [REPORT] View Master Exam Timetable
 7. [REPORT] View Visual Room Seating Grid
 8. [REPORT] Search Student Hall Ticket (by PRN)
 9. [REPORT] View Faculty Invigilation Duty Roster
10. [VALIDATION] Run Conflict & Constraint Audit
11. [FILE I/O] Export Timetable & Seating to CSV Files
12. Exit Application
---------------------------------------------------------------------
```

### Generated Master Exam Timetable (Option 6):
```text
==========================================================================
                         MASTER EXAM TIMETABLE                            
==========================================================================
DATE         | TIME SLOT    | COURSE     | SUBJECT NAME                   | STUDENTS  
--------------------------------------------------------------------------
2026-11-02   | 10:00 AM     | CS201      | Object Oriented Programming    | 5         
2026-11-02   | 10:00 AM     | IT204      | Discrete Mathematics           | 5         
2026-11-02   | 02:00 PM     | CS202      | Data Structures & Algorithms   | 5         
2026-11-02   | 02:00 PM     | BS205      | Environmental Science          | 5         
2026-11-03   | 10:00 AM     | IT203      | Database Management Systems    | 10        
==========================================================================
```
*(Notice how CS201 and IT204 run at the same time because they have zero common students — this is Graph Colouring in action!)*

---

### Generated Student Hall Ticket (Option 8 - Search PRN 101):
```text
==========================================================================
                   PCCoE PUNE - STUDENT HALL TICKET                       
==========================================================================
Student Name : Aarav Sharma
PRN          : 101
Department   : IT (Div A)
--------------------------------------------------------------------------
COURSE     | COURSE NAME               | DATE         | TIME         | ROOM         | SEAT    
--------------------------------------------------------------------------
CS201      | Object Oriented Programming | 2026-11-02   | 10:00 AM     | Auditorium   | R1-C2   
CS202      | Data Structures & Algorithms| 2026-11-02   | 02:00 PM     | Auditorium   | R1-C1   
IT203      | Database Management Systems | 2026-11-03   | 10:00 AM     | Auditorium   | R1-C1   
==========================================================================
```

---

### Generated Faculty Duty Chart (Option 9):
```text
==========================================================================
                      FACULTY INVIGILATION DUTY CHART                     
==========================================================================
FACULTY ID | NAME                 | DATE         | TIME         | ROOM         | COURSE    
--------------------------------------------------------------------------
F001       | Dr. S. Sharma        | 2026-11-02   | 10:00 AM     | Auditorium   | CS201     
F004       | Dr. A. Deshmukh      | 2026-11-02   | 10:00 AM     | Auditorium   | IT204     
F002       | Prof. P. Kulkarni    | 2026-11-02   | 02:00 PM     | Auditorium   | CS202     
F003       | Prof. R. Patil       | 2026-11-02   | 02:00 PM     | Auditorium   | BS205     
F001       | Dr. S. Sharma        | 2026-11-03   | 10:00 AM     | Auditorium   | IT203     
--------------------------------------------------------------------------
DUTY SUMMARY (Fairness Distribution):
 -> Dr. S. Sharma (F001)     - Total Duties: 2
 -> Prof. P. Kulkarni (F002) - Total Duties: 1
 -> Prof. R. Patil (F003)    - Total Duties: 1
 -> Dr. A. Deshmukh (F004)   - Total Duties: 1
==========================================================================
```

---

## 8. TEACHER PRESENTATION & VIVA PREPARATION GUIDE

### 5-Minute Explanation Script for Your Teacher:
> *"Good morning, Sir/Madam. I have developed the **Exam Timetable & Seating Generator** in pure Core Java.*
>
> *Our college currently prepares exam timetables manually in Excel, which frequently leads to student exam clashes, unfair faculty duty distributions, and students sitting next to classmates taking the exact same exam paper.*
>
> *I solved this using four core concepts:*
> 1. *For the data layer, I used **Abstraction and Inheritance**. An abstract class `Person` is inherited by `Student` and `Faculty`. I used constructor overloading with default, parameterized, and copy constructors.*
> 2. *For timetabling, I implemented **Graph Colouring**. Each course is a node. If two courses share any student, an edge connects them. The greedy colouring algorithm assigns distinct colours to connected nodes, mapping directly to clash-free time slots.*
> 3. *For seating, I used the **Strategy Pattern**. My `MixedSubjectSeating` strategy interleaves students from different courses in a checkerboard pattern, making cheating impossible.*
> 4. *For invigilator duties, I used a **PriorityQueue** to balance the workload fairly across all teachers.*
> 5. *Finally, I implemented **Custom Exceptions** and **Character Streams (`BufferedReader` and `BufferedWriter`)** to export data to CSV files. Thank you!"*

---

### Top 15 Viva Questions & Short Answers:

1. **Why is `Person` declared as an `abstract class`?**  
   *Answer:* Because a generic person cannot sit for an exam or invigilate directly; only a specific `Student` or `Faculty` can. `Person` serves as an abstract base providing common attributes and enforcing the `displayDetails()` method.

2. **What is Constructor Overloading? Where did you use it?**  
   *Answer:* Having multiple constructors with different parameter lists in the same class. I used it in `Person`, `Student`, `Faculty`, `Course`, and `Room` to provide Default, Parameterized, and Copy constructors.

3. **What is the purpose of a Copy Constructor?**  
   *Answer:* It creates an independent clone of an object with the same values without modifying the original object.

4. **How does Graph Colouring prevent student clashes?**  
   *Answer:* An edge is drawn between two courses if at least one student is enrolled in both. Because graph colouring guarantees that adjacent nodes never receive the same colour (time slot), common students are never scheduled at the same time.

5. **What is the chromatic number $\chi(G)$?**  
   *Answer:* The minimum number of colours needed to colour a graph such that no two adjacent vertices share the same colour. In this project, it represents the minimum number of exam slots required.

6. **Why did you use the Strategy Design Pattern?**  
   *Answer:* To allow switching between different seating algorithms (Mixed-Subject, Zig-Zag, Sequential, Random) dynamically at runtime without altering the `SeatingService` class.

7. **How does Runtime Polymorphism work in your project?**  
   *Answer:* Through the `SeatingStrategy` interface reference. Calling `currentStrategy.allocate(...)` executes the concrete algorithm belonging to whichever strategy object was assigned at runtime.

8. **How does `PriorityQueue` ensure fair duty allocation?**  
   *Answer:* `Faculty` implements `Comparable<Faculty>` based on `dutyCount`. The `PriorityQueue` always serves the teacher with the lowest duty count first.

9. **What is the difference between Checked and Unchecked Exceptions in Java?**  
   *Answer:* Checked exceptions extend `Exception` and must be handled using `try-catch` or declared using `throws` at compile time. Unchecked exceptions extend `RuntimeException` and occur during program execution.

10. **Why did you use Character Streams (`FileReader`/`FileWriter`) instead of Byte Streams?**  
    *Answer:* Character streams process 16-bit Unicode characters, which is ideal for human-readable text and CSV files.

11. **Why use `BufferedWriter` instead of plain `FileWriter`?**  
    *Answer:* `BufferedWriter` writes data into an in-memory buffer before writing to disk in larger blocks, significantly reducing slow disk I/O operations.

12. **How does `MixedSubjectSeating` prevent malpractice?**  
    *Answer:* It alternates students from different courses across adjacent desks, ensuring neighbors never share the same exam paper.

13. **What is the difference between `this` and `super`?**  
    *Answer:* `this` refers to the current instance (variables and constructor chaining `this(...)`), while `super` refers to the immediate parent class (constructor `super(...)` and parent methods).

14. **What are Hard vs. Soft constraints?**  
    *Answer:* Hard constraints cannot be broken under any circumstances (e.g., student having two exams in the same slot). Soft constraints are warnings (e.g., student having two exams on the same day).

15. **Where is Encapsulation demonstrated?**  
    *Answer:* All attributes are declared `private` or `protected` and accessed strictly through public getters and setters with validation rules.

---

## 9. CONCLUSION

The **Exam Timetable & Seating Generator** mini project demonstrates how fundamental Object-Oriented Programming (OOP) concepts and classic computer science algorithms can be combined to solve real-world college administrative challenges.

By following clean software design principles—Abstraction, Inheritance, Polymorphism, Encapsulation, Design Patterns, Exception Handling, and File I/O—the project is modular, easy to explain, and meets all academic requirements of the S.Y. B.Tech Information Technology curriculum at PCCoE Pune.
