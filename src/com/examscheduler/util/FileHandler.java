package com.examscheduler.util;

import com.examscheduler.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * CLASS: FileHandler
 * ============================================================================
 * WHAT IT DOES:
 *   Handles file input and output operations for saving and exporting exam data.
 *
 * SYLLABUS TOPICS COVERED:
 *   1. CHARACTER STREAMS: Uses FileReader, BufferedReader, FileWriter, BufferedWriter.
 *   2. EXCEPTION HANDLING: Proper try-catch-finally blocks to safely close streams.
 *   3. CSV EXPORT: Saves generated timetable and seating plans to standard CSV format.
 * ============================================================================
 */
public class FileHandler {

    // ------------------------------------------------------------------------
    // 1. EXPORT MASTER TIMETABLE TO CSV
    // ------------------------------------------------------------------------

    /**
     * Writes the master exam timetable into a CSV file using BufferedWriter.
     */
    public static void exportTimetableToCSV(List<Exam> exams, String filePath) throws IOException {
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs(); // Create directories if needed
        }

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);

            // Step 1: Write CSV Column Headers
            bufferedWriter.write("CourseCode,CourseName,Date,StartTime,EndTime,TotalStudents");
            bufferedWriter.newLine();

            // Step 2: Write Each Exam Record
            for (Exam e : exams) {
                String line = String.format("%s,%s,%s,%s,%s,%d",
                        e.getCourse().getCode(),
                        e.getCourse().getName().replace(",", " "),
                        e.getTimeSlot().getDate(),
                        e.getTimeSlot().getStartTime(),
                        e.getTimeSlot().getEndTime(),
                        e.getEligibleStudents().size()
                );
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

            bufferedWriter.flush();
            System.out.println("[SUCCESS] Master Timetable saved to: " + file.getAbsolutePath());

        } finally {
            // Step 3: Explicitly close resources in finally block
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException ex) {
                    System.err.println("Error closing BufferedWriter: " + ex.getMessage());
                }
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException ex) {
                    System.err.println("Error closing FileWriter: " + ex.getMessage());
                }
            }
        }
    }

    // ------------------------------------------------------------------------
    // 2. EXPORT SEATING PLAN TO CSV
    // ------------------------------------------------------------------------

    /**
     * Exports desk allocations to CSV using try-with-resources.
     */
    public static void exportSeatingToCSV(List<SeatAllocation> allocations, String filePath) throws IOException {
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("RoomNo,SeatLabel,PRN,StudentName,CourseCode");
            bw.newLine();

            for (SeatAllocation sa : allocations) {
                String line = String.format("%s,%s,%s,%s,%s",
                        sa.getRoomNo(),
                        sa.getSeatLabel(),
                        sa.getStudent().getPrn(),
                        sa.getStudent().getName().replace(",", " "),
                        sa.getCourse().getCode()
                );
                bw.write(line);
                bw.newLine();
            }

            bw.flush();
            System.out.println("[SUCCESS] Seating Plan saved to: " + file.getAbsolutePath());
        }
    }

    // ------------------------------------------------------------------------
    // 3. READ TEXT FILE (BufferedReader demonstration)
    // ------------------------------------------------------------------------

    /**
     * Reads a text file line-by-line using FileReader and BufferedReader.
     */
    public static List<String> readTextFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            throw new FileNotFoundException("File not found at: " + filePath);
        }

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    System.err.println("Error closing BufferedReader: " + ex.getMessage());
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException ex) {
                    System.err.println("Error closing FileReader: " + ex.getMessage());
                }
            }
        }

        return lines;
    }
}
