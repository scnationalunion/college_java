package com.hua.college;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentInsertGenerator {

    public static void main(String[] args) {
        List<String> studentNames = generateStudentNames(5000);

        for (String name : studentNames) {
            int year = generateYear(2015, 2022);
            int schoolId = generateSchoolId(1, 101);
            int majorId = generateMajorId(1, 101);
            int instructorId = generateInstructorId(1, 101);
            double credit = generateCredit(1, 60);

            String insertStatement = generateInsertStatement(name, year, schoolId, majorId, instructorId, credit);
            System.out.println(insertStatement);
        }
    }

    private static List<String> generateStudentNames(int count) {
        List<String> studentNames = new ArrayList<>();
        Random random = new Random();

        String[] firstNames = {"John", "Emma", "Liam", "Olivia", "Noah", "Ava", "William", "Sophia", "James", "Isabella"};
        String[] lastNames = {"Smith", "Johnson", "Brown", "Taylor", "Miller", "Anderson", "Wilson", "Clark", "Lee", "Walker"};

        for (int i = 0; i < count; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String studentName = firstName + " " + lastName;
            studentNames.add(studentName);
        }

        return studentNames;
    }

    private static int generateYear(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    private static int generateSchoolId(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    private static int generateMajorId(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    private static int generateInstructorId(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    private static double generateCredit(double min, double max) {
        Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }

    private static String formatDecimal(double value, int decimalPlaces) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.0");
        decimalFormat.setMaximumFractionDigits(decimalPlaces);
        return decimalFormat.format(value);
    }

    private static String generateInsertStatement(String name, int year, int schoolId, int majorId, int instructorId, double credit) {
        return "INSERT INTO `student` (`name`, `year`, `school_id`, `major_id`, `stu_instructor_id`, `credit`) VALUES ('" + name + "', " + year + ", " + schoolId + ", " + majorId + ", " + instructorId + ", " + formatDecimal(credit, 1) + ");";
    }
}
