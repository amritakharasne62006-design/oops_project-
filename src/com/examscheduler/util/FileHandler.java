package com.examscheduler.util;

import com.examscheduler.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for File Input/Output operations using Character Streams.
 * Demonstrates FileReader, BufferedReader, FileWriter, BufferedWriter, and Exception Handling.
 */
public class FileHandler {

    /**
     * Exports the master timetable to a formatted CSV file using BufferedWriter.
     */
    public static void exportTimetableToCSV(List<Exam> exams, String filePath) throws IOException {
        File file = new File(filePath);
        // Ensure parent directories exist
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);

            // Write CSV Header
            bufferedWriter.write("CourseCode,CourseName,Date,StartTime,EndTime,TotalStudents");
            bufferedWriter.newLine();

            // Write Exam Rows
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
            System.out.println("[SUCCESS] Master Timetable exported to: " + file.getAbsolutePath());
        } finally {
            // Clean up resources in finally block (demonstrates syllabus standard)
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

    /**
     * Exports seating allocation to a CSV file.
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
            System.out.println("[SUCCESS] Seating chart exported to: " + file.getAbsolutePath());
        }
    }

    /**
     * Reads a text file line-by-line using BufferedReader and displays its content.
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
