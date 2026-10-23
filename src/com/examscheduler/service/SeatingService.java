package com.examscheduler.service;

import com.examscheduler.model.*;
import com.examscheduler.strategy.MixedSubjectSeating;
import com.examscheduler.strategy.SeatingStrategy;
import com.examscheduler.exception.RoomCapacityExceededException;

import java.util.*;

/**
 * Service to manage room allocation and seating plan generation.
 * Demonstrates Strategy Pattern, Collections (HashMap, ArrayList), and Exception Handling.
 */
public class SeatingService {

    private List<Room> rooms;
    private SeatingStrategy currentStrategy;

    /**
     * Parameterized constructor. Default strategy is MixedSubjectSeating.
     */
    public SeatingService(List<Room> rooms) {
        this.rooms = new ArrayList<>(rooms);
        this.currentStrategy = new MixedSubjectSeating();
    }

    /**
     * Changes the seating strategy dynamically at runtime (Polymorphism in action).
     */
    public void setStrategy(SeatingStrategy strategy) {
        if (strategy != null) {
            this.currentStrategy = strategy;
        }
    }

    public SeatingStrategy getStrategy() {
        return currentStrategy;
    }

    /**
     * Generates seating arrangement for a group of exams taking place in the SAME time slot.
     */
    public Map<String, List<SeatAllocation>> generateSeatingForSlot(List<Exam> slotExams) throws RoomCapacityExceededException {
        // Collect all students and their respective courses for this slot
        List<Student> allSlotStudents = new ArrayList<>();
        Map<Student, Course> studentCourseMap = new HashMap<>();

        for (Exam exam : slotExams) {
            for (Student s : exam.getEligibleStudents()) {
                allSlotStudents.add(s);
                studentCourseMap.put(s, exam.getCourse());
            }
        }

        // Sort rooms by capacity descending (fill largest rooms first)
        List<Room> availableRooms = new ArrayList<>();
        for (Room r : rooms) {
            if (r.isAvailable()) {
                availableRooms.add(r);
            }
        }
        availableRooms.sort((r1, r2) -> Integer.compare(r2.getCapacity(), r1.getCapacity()));

        // Calculate total available room capacity
        int totalCapacity = 0;
        for (Room r : availableRooms) {
            totalCapacity += r.getCapacity();
        }

        if (allSlotStudents.size() > totalCapacity) {
            throw new RoomCapacityExceededException(
                "Total students in slot (" + allSlotStudents.size() +
                ") exceeds total room capacity (" + totalCapacity + ")!"
            );
        }

        Map<String, List<SeatAllocation>> roomAllocations = new LinkedHashMap<>();
        int studentPointer = 0;

        for (Room room : availableRooms) {
            if (studentPointer >= allSlotStudents.size()) {
                break; // All students seated
            }

            // Slice out batch of students for this room
            int batchSize = Math.min(room.getCapacity(), allSlotStudents.size() - studentPointer);
            List<Student> roomBatch = new ArrayList<>();
            for (int i = 0; i < batchSize; i++) {
                roomBatch.add(allSlotStudents.get(studentPointer + i));
            }
            studentPointer += batchSize;

            // Apply selected seating strategy
            List<SeatAllocation> allocations = currentStrategy.allocate(roomBatch, studentCourseMap, room);
            roomAllocations.put(room.getRoomNo(), allocations);

            // Associate this room with the exams
            for (Exam exam : slotExams) {
                exam.addRoom(room);
            }
        }

        return roomAllocations;
    }

    /**
     * Prints an ASCII representation of the room seating grid.
     */
    public void printRoomGrid(Room room, List<SeatAllocation> allocations) {
        String[][] grid = new String[room.getRows()][room.getCols()];

        // Initialize grid as empty
        for (int r = 0; r < room.getRows(); r++) {
            for (int c = 0; c < room.getCols(); c++) {
                grid[r][c] = "[  EMPTY  ]";
            }
        }

        // Fill grid with student allocations
        for (SeatAllocation sa : allocations) {
            if (sa.getRow() < room.getRows() && sa.getCol() < room.getCols()) {
                String label = sa.getStudent().getPrn() + ":" + sa.getCourse().getCode();
                grid[sa.getRow()][sa.getCol()] = String.format("[%-9s]", label);
            }
        }

        System.out.println("\n=== SEATING GRID FOR ROOM: " + room.getRoomNo() + " ===");
        System.out.println("Strategy: " + currentStrategy.getStrategyName());
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
