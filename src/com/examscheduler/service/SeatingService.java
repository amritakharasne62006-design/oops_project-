package com.examscheduler.service;

import com.examscheduler.model.*;
import com.examscheduler.strategy.MixedSubjectSeating;
import com.examscheduler.strategy.SeatingStrategy;
import com.examscheduler.exception.RoomCapacityExceededException;

import java.util.*;

/**
 * ============================================================================
 * CLASS: SeatingService
 * ============================================================================
 * WHAT IT DOES:
 *   Manages classroom room allocation and desk seat arrangement.
 *   - Sorts rooms by capacity (fills largest rooms first for efficiency).
 *   - Applies the selected SeatingStrategy (Strategy Pattern / Polymorphism).
 *   - Generates visual 2D ASCII seat charts for examination halls.
 *
 * OOP CONCEPTS DEMONSTRATED:
 *   1. STRATEGY PATTERN: Holds a 'SeatingStrategy' reference, easily swappable at runtime.
 *   2. RUNTIME POLYMORPHISM: Invokes currentStrategy.allocate(...) without knowing concrete type.
 *   3. 2D ARRAYS (Array of Objects): Generates a matrix for room desk grids.
 * ============================================================================
 */
public class SeatingService {

    // ------------------------------------------------------------------------
    // 1. DATA FIELDS
    // ------------------------------------------------------------------------
    private List<Room> rooms;                  // All rooms in the college campus
    private SeatingStrategy currentStrategy;   // Currently active seating algorithm

    // ------------------------------------------------------------------------
    // 2. CONSTRUCTOR
    // ------------------------------------------------------------------------
    public SeatingService(List<Room> rooms) {
        this.rooms = new ArrayList<>(rooms);
        // Default to anti-cheating MixedSubjectSeating
        this.currentStrategy = new MixedSubjectSeating();
    }

    // ------------------------------------------------------------------------
    // 3. RUNTIME STRATEGY SWAPPING (Polymorphism)
    // ------------------------------------------------------------------------
    public void setStrategy(SeatingStrategy strategy) {
        if (strategy != null) {
            this.currentStrategy = strategy;
        }
    }

    public SeatingStrategy getStrategy() {
        return currentStrategy;
    }

    // ------------------------------------------------------------------------
    // 4. ROOM & SEATING ALLOCATION LOGIC
    // ------------------------------------------------------------------------

    /**
     * Generates seating allocations for all exams sharing the same time slot.
     */
    public Map<String, List<SeatAllocation>> generateSeatingForSlot(List<Exam> slotExams) throws RoomCapacityExceededException {
        // Step 1: Aggregate all examinee students across all courses in this slot
        List<Student> allSlotStudents = new ArrayList<>();
        Map<Student, Course> studentCourseMap = new HashMap<>();

        for (Exam exam : slotExams) {
            for (Student s : exam.getEligibleStudents()) {
                allSlotStudents.add(s);
                studentCourseMap.put(s, exam.getCourse());
            }
        }

        // Step 2: Filter available rooms and sort by capacity descending (Largest rooms first)
        List<Room> availableRooms = new ArrayList<>();
        for (Room r : rooms) {
            if (r.isAvailable()) {
                availableRooms.add(r);
            }
        }
        availableRooms.sort((r1, r2) -> Integer.compare(r2.getCapacity(), r1.getCapacity()));

        // Step 3: Validate total capacity against student count
        int totalCapacity = 0;
        for (Room r : availableRooms) {
            totalCapacity += r.getCapacity();
        }

        if (allSlotStudents.size() > totalCapacity) {
            throw new RoomCapacityExceededException(
                "Capacity Exceeded! Students in slot: " + allSlotStudents.size() +
                ", but total available room capacity: " + totalCapacity
            );
        }

        // Step 4: Fill rooms and execute the chosen seating strategy
        Map<String, List<SeatAllocation>> roomAllocations = new LinkedHashMap<>();
        int studentPointer = 0;

        for (Room room : availableRooms) {
            if (studentPointer >= allSlotStudents.size()) {
                break; // All students have been seated
            }

            // Determine how many students fit into this room
            int countForThisRoom = Math.min(room.getCapacity(), allSlotStudents.size() - studentPointer);
            List<Student> roomBatch = new ArrayList<>();
            for (int i = 0; i < countForThisRoom; i++) {
                roomBatch.add(allSlotStudents.get(studentPointer + i));
            }
            studentPointer += countForThisRoom;

            // Apply selected polymorphic seating strategy
            List<SeatAllocation> allocations = currentStrategy.allocate(roomBatch, studentCourseMap, room);
            roomAllocations.put(room.getRoomNo(), allocations);

            // Record this room inside each exam
            for (Exam exam : slotExams) {
                exam.addRoom(room);
            }
        }

        return roomAllocations;
    }

    // ------------------------------------------------------------------------
    // 5. 2D ASCII SEATING GRID VISUALIZER
    // ------------------------------------------------------------------------

    /**
     * Prints an ASCII desk grid representing the physical layout of the examination hall.
     */
    public void printRoomGrid(Room room, List<SeatAllocation> allocations) {
        // Create 2D Array of desk representations
        String[][] grid = new String[room.getRows()][room.getCols()];

        // Initialize all desks as EMPTY
        for (int r = 0; r < room.getRows(); r++) {
            for (int c = 0; c < room.getCols(); c++) {
                grid[r][c] = "[  EMPTY  ]";
            }
        }

        // Place students onto their assigned desk positions
        for (SeatAllocation sa : allocations) {
            if (sa.getRow() < room.getRows() && sa.getCol() < room.getCols()) {
                String label = sa.getStudent().getPrn() + ":" + sa.getCourse().getCode();
                grid[sa.getRow()][sa.getCol()] = String.format("[%-9s]", label);
            }
        }

        // Print header and layout
        System.out.println("\n=== PHYSICAL SEATING LAYOUT: " + room.getRoomNo() + " ===");
        System.out.println("Applied Strategy: " + currentStrategy.getStrategyName());
        System.out.println("-------------------------------------------------------------------");

        for (int r = 0; r < room.getRows(); r++) {
            System.out.print("Row " + (r + 1) + " | ");
            for (int c = 0; c < room.getCols(); c++) {
                System.out.print(grid[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println("-------------------------------------------------------------------");
    }
}
