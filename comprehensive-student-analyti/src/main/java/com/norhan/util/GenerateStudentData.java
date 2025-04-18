/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.norhan.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerateStudentData {

    private static final String[] SUBJECTS = {"Math", "Science", "English"};
    private static final String[] FIRST_NAMES = {"Ahmed", "Fatma", "Omar", "Sara", "Mona", "Ali", "Hassan", "Nour", "Yara", "Khaled"};
    private static final String[] LAST_NAMES = {"Mohamed", "Ali", "Hassan", "Ibrahim", "Khaled", "Salem", "Nasser", "Fathy", "Gamal", "Eid"};
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        for (String subject : SUBJECTS) {
            generateDataForSubject(subject);
        }
    }

    private static void generateDataForSubject(String subject) {
        String fileName = "src/main/resources/data/" + subject.toLowerCase() + ".csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write CSV header
            writer.write("studentId,name,subject,marks\n");

            // Generate 1000 records per subject
            for (int i = 0; i < 1000; i++) {
                String studentId = "2023" + String.format("%03d", i + 1);
                String name = generateRandomName();
                int marks = generateRandomMarks(i);

                // Introduce some invalid data for testing
                if (i % 50 == 0) name = ""; // Empty name every 50th record
                if (i % 100 == 0) marks = -10; // Invalid marks every 100th record
                if (i % 150 == 0) marks = 110; // Invalid marks every 150th record

                writer.write(String.format("%s,%s,%s,%d\n", studentId, name, subject, marks));
            }
            System.out.println(subject + ".csv generated successfully!");
        } catch (IOException e) {
            System.err.println("Error generating " + subject + ".csv: " + e.getMessage());
        }
    }

    private static String generateRandomName() {
        String firstName = FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];
        return firstName + " " + lastName;
    }

    private static int generateRandomMarks(int index) {
        // Generate marks with a normal distribution around 80, but allow some outliers
        int marks = (int) (RANDOM.nextGaussian() * 15 + 80);
        if (index % 200 == 0) marks = RANDOM.nextInt(10); // Low outlier
        if (index % 300 == 0) marks = 95 + RANDOM.nextInt(5); // High outlier
        return Math.max(0, Math.min(100, marks));
    }
}
