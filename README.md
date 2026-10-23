# Exam Timetable & Seating Generator (Java OOP Mini Project)

**Developed for:** S.Y. B.Tech Information Technology, PCCoE Pune  
**Language:** Pure Core Java (JDK 17+) — No external libraries or frameworks  
**Architecture:** Object-Oriented Design (Abstraction, Inheritance, Polymorphism, Encapsulation, Strategy Pattern, File I/O, Custom Exceptions)

---

## 1. Problem Statement & Solution

### The Problem
Colleges traditionally organize semester examinations using spreadsheets. This causes:
* **Student Clashes:** A student enrolled in two courses scheduled at the exact same hour.
* **Cheating / Malpractice:** Students taking the same exam paper sitting side-by-side or directly behind one another.
* **Inefficient Room Usage:** Booking a 100-seat hall for 15 students while smaller classrooms are overcrowded.
* **Unfair Invigilation Load:** Some professors getting overburdened with 8 duties while others get 1.

### The Solution
A clean, modular Java desktop console system that:
1. **Schedules Clash-Free Timetables** using the **Greedy Graph Colouring Algorithm**.
2. **Generates Anti-Cheating Seating Plans** using the **Strategy Pattern** (Mixed-Subject Interleaved, Zig-Zag, Sequential, Random).
3. **Distributes Invigilator Duties Fairly** using a **PriorityQueue** (fewest duties first).
4. **Validates Hard & Soft Constraints** (holiday exclusions, simultaneous exam checks).
5. **Exports Reports to CSV & Formatted Console** using character streams (`BufferedReader`, `BufferedWriter`).

---

## 2. PCCoE Pune Syllabus Alignment

| Syllabus Concept | Implementation in Code |
| :--- | :--- |
| **Constructors (Default, Parameterized, Copy)** | Implemented with constructor overloading in `Person`, `Student`, `Faculty`, `Course`, and `Room`. |
| **`this` Keyword & Data Hiding (Encapsulation)** | Private fields with validated public getters/setters, using `this` to resolve variable shadowing. |
| **Array of Objects** | Used in room grid matrices (`String[][] grid`), student arrays, and capacity mappings. |
| **Inheritance & Access Modifiers** | `Person` (abstract base class with `protected` members) extended by `Student` and `Faculty`. |
| **Abstract Classes** | `abstract class Person` defines mandatory abstract method `public abstract void displayDetails();`. |
| **Interfaces & Polymorphism** | `SeatingStrategy` interface with polymorphic `allocate()` method implemented by 4 distinct strategies. |
| **Method Overriding & Overloading** | Overridden `displayDetails()`, `toString()`, `compareTo()`. Overloaded constructors throughout. |
| **User-Defined Exceptions** | `SchedulingClashException`, `RoomCapacityExceededException`, `InsufficientInvigilatorsException`, `InvalidDateException`. |
| **File Handling (Character Streams)** | `FileReader`, `BufferedReader`, `FileWriter`, `BufferedWriter` used in `FileHandler.java` for CSV export/import. |

---

## 3. Core Algorithm: Graph Colouring for Clash-Free Timetables

### The Theory
* **Graph Definition:** $G = (V, E)$
  * **Vertices ($V$):** Each Course is a node.
  * **Edges ($E$):** An edge exists between Course $A$ and Course $B$ if and only if **at least one student is enrolled in both**.
* **Goal:** Assign each vertex a "colour" (representing an exam Time Slot) such that no two adjacent vertices share the same colour.
* **Minimum Colours ($k$):** The chromatic number $\chi(G)$ represents the absolute minimum number of exam slots needed.

### Worked Example (4 Courses, 5 Students)

#### Courses:
* $C_1$: Java OOP
* $C_2$: Data Structures (DSA)
* $C_3$: Database Systems (DBMS)
* $C_4$: Discrete Mathematics (DM)

#### Enrollments:
* Student $S_1$: Takes $C_1, C_2$
* Student $S_2$: Takes $C_1, C_3$
* Student $S_3$: Takes $C_2, C_3$
* Student $S_4$: Takes $C_4$
* Student $S_5$: Takes $C_4$

#### Conflict Graph Edges:
* Edge $(C_1, C_2)$ due to $S_1$
* Edge $(C_1, C_3)$ due to $S_2$
* Edge $(C_2, C_3)$ due to $S_3$
* $C_4$ shares no students with $C_1, C_2,$ or $C_3$.

#### Greedy Colouring Execution:
1. **Course $C_1$:** Assigned **Colour 0** (Slot 1: Day 1 Morning).
2. **Course $C_2$:** Adjacent to $C_1$ (Colour 0). Assigned **Colour 1** (Slot 2: Day 1 Afternoon).
3. **Course $C_3$:** Adjacent to $C_1$ (Colour 0) and $C_2$ (Colour 1). Assigned **Colour 2** (Slot 3: Day 2 Morning).
4. **Course $C_4$:** Has no edges to $C_1$. Smallest available non-conflicting colour is **Colour 0** (Slot 1: Day 1 Morning).

#### Result:
* **Slot 1:** $C_1$ (Java) and $C_4$ (Discrete Maths) run at the same time without any student clash!
* **Slot 2:** $C_2$ (DSA).
* **Slot 3:** $C_3$ (DBMS).

---

## 4. Project Structure

```text
com.examscheduler
│
├── Main.java                        // Interactive CLI console menu
│
├── model/
│   ├── Person.java                  // Abstract base class (id, name, age)
│   ├── Student.java                 // Derived class (PRN, division, enrolled courses)
│   ├── Faculty.java                 // Derived class (duty count, leave dates)
│   ├── Course.java                  // Course entity (code, name, duration)
│   ├── Room.java                    // Room entity (capacity, rows, cols)
│   ├── TimeSlot.java                // Slot entity (date, start/end time)
│   ├── Exam.java                    // Scheduled exam (course, slot, rooms, students)
│   ├── SeatAllocation.java          // Desk assignment (student, course, row, col)
│   └── InvigilatorDuty.java         // Duty record (faculty, exam, room, slot)
│
├── strategy/
│   ├── SeatingStrategy.java         // Strategy interface: allocate(...)
│   ├── SequentialSeating.java       // Row-by-row seat allocation
│   ├── ZigZagSeating.java           // Alternating snake pattern
│   ├── MixedSubjectSeating.java     // Anti-cheating interleaved pattern
│   └── RandomSeating.java           // Randomized desk shuffling
│
├── service/
│   ├── TimetableGenerator.java      // Graph Colouring scheduler
│   ├── SeatingService.java          // Room allocation & ASCII grid printer
│   └── InvigilatorService.java      // Fair allocation using PriorityQueue
│
├── validation/
│   └── ConflictChecker.java         // Hard and soft constraint audit engine
│
├── exception/
│   ├── SchedulingClashException.java
│   ├── RoomCapacityExceededException.java
│   ├── InsufficientInvigilatorsException.java
│   └── InvalidDateException.java
│
├── report/
│   └── ReportGenerator.java         // Master timetable, hall ticket, stats printer
│
└── util/
    └── FileHandler.java             // Character streams (BufferedReader/BufferedWriter)
```

---

## 5. How to Compile and Run

### 1. Compile:
From the root directory:
```bash
javac -d bin src/com/examscheduler/*.java src/com/examscheduler/*/*.java
```

### 2. Run:
```bash
java -cp bin com.examscheduler.Main
```

---

## 6. Five-Minute Viva Presentation Script for Your Teacher

> *"Good morning, Sir/Madam. My project is the **Exam Timetable & Seating Generator**.*  
>  
> *In colleges, exam scheduling is manually handled in spreadsheets, causing student clashes, cheating risks from adjacent seating, and unfair duty loads. My system automates this entire pipeline using Core Java and fundamental OOP concepts.*  
>  
> *First, for the data model, I used **Abstraction and Inheritance**. I have an abstract base class `Person` with protected attributes, default, parameterized, and copy constructors. `Student` and `Faculty` inherit from `Person` and override the abstract `displayDetails()` method.*  
>  
> *Second, for timetable generation, I implemented the **Greedy Graph Colouring Algorithm**. Each course is a node. If any student is enrolled in two courses, an edge connects them. The algorithm colours the graph greedily so conflicting courses receive different colours, which map to distinct time slots.*  
>  
> *Third, for seating arrangement, I used the **Strategy Design Pattern**. The `SeatingStrategy` interface defines a polymorphic `allocate()` method. I implemented four strategies: `MixedSubjectSeating`—which interleaves students of different courses to prevent malpractice—as well as `ZigZagSeating`, `SequentialSeating`, and `RandomSeating`.*  
>  
> *Fourth, for invigilator allocation, I utilized a **PriorityQueue** sorted by lowest duty count to guarantee fair work distribution across faculty.*  
>  
> *Finally, I implemented **Custom Checked Exceptions** for validation and used **Character Streams (`BufferedReader` and `BufferedWriter`)** to export timetables and seating plans to CSV files. Thank you!"*

---

## 7. Top 15 Viva Questions & Simple Answers

1. **Q: Why is `Person` an abstract class?**  
   *A:* Because a generic "Person" cannot appear for an exam or invigilate directly; only a specific `Student` or `Faculty` can. `Person` serves as a template enforcing common fields (`id`, `name`) and the abstract method `displayDetails()`.

2. **Q: Where have you used Constructor Overloading?**  
   *A:* In `Person`, `Student`, `Faculty`, `Course`, and `Room`. Each has a Default Constructor (no args), a Parameterized Constructor, and a Copy Constructor.

3. **Q: What is the purpose of the Copy Constructor?**  
   *A:* It creates a duplicate object with identical state without modifying the original object, ensuring safe cloning of student and course data.

4. **Q: How does the Graph Colouring algorithm ensure no clashes?**  
   *A:* Edges represent shared students. By giving adjacent nodes different colours (time slots), two courses with common students can never be scheduled in the same slot.

5. **Q: What is the chromatic number in Graph Colouring?**  
   *A:* The smallest number of colours needed to colour a graph such that no two adjacent vertices share a colour. In our project, it represents the minimum number of exam slots required.

6. **Q: Which Design Pattern did you use and why?**  
   *A:* The **Strategy Pattern** for seating arrangements. It allows switching seating algorithms (e.g., Mixed-Subject vs. Zig-Zag) dynamically at runtime without altering the `SeatingService` class.

7. **Q: How do you achieve Runtime Polymorphism?**  
   *A:* Through the `SeatingStrategy` reference. When `currentStrategy.allocate(...)` is invoked, Java resolves which concrete method to execute at runtime based on the actual object instantiated.

8. **Q: How does the PriorityQueue ensure fair invigilator duty allocation?**  
   *A:* `Faculty` implements `Comparable<Faculty>` based on `dutyCount`. The `PriorityQueue` always serves the faculty member with the lowest duty count first.

9. **Q: What is the difference between Checked and Unchecked Exceptions in your project?**  
   *A:* Our custom exceptions (`SchedulingClashException`, `RoomCapacityExceededException`) extend `Exception`, making them **Checked Exceptions**. The compiler enforces that they must be declared (`throws`) or handled (`try-catch`).

10. **Q: Why did you use Character Streams instead of Byte Streams for File I/O?**  
    *A:* Character streams (`FileReader`, `FileWriter`, `BufferedReader`, `BufferedWriter`) work directly with 16-bit Unicode characters, which is ideal for human-readable text and CSV files.

11. **Q: Why use `BufferedWriter` instead of plain `FileWriter`?**  
    *A:* `BufferedWriter` writes data into an in-memory buffer before writing to disk in batches, which significantly improves I/O performance.

12. **Q: How does `MixedSubjectSeating` prevent cheating?**  
    *A:* It checks student courses and assigns alternate seats to students writing different exam subjects, ensuring immediate neighbors never write the same exam paper.

13. **Q: What is the difference between `super` and `this` keywords?**  
    *A:* `this` references the current instance (used for shadowing resolution and constructor chaining `this(...)`), while `super` invokes the immediate parent class constructor (`super(...)`) or method.

14. **Q: What are Hard vs. Soft constraints in your Conflict Checker?**  
    *A:* A **Hard Constraint** cannot be violated (e.g., a student having 2 exams simultaneously). A **Soft Constraint** is desirable but not strictly invalid (e.g., warning if a student has two exams on the same day).

15. **Q: Where is Encapsulation demonstrated?**  
    *A:* All entity variables are declared `private` or `protected` and accessed solely via `getters` and `setters` with validation logic.
