package com.hua.college;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CourseInsertGenerator {

    public static void main(String[] args) {
        List<String> courseNames = generateCourseNames(1000);

        for (String name : courseNames) {
            double credit = generateCredit();
            String formattedCredit = formatDecimal(credit, 1);
            String insertStatement = generateInsertStatement(name, formattedCredit);
            System.out.println(insertStatement);
        }
    }

    private static List<String> generateCourseNames(int count) {
        List<String> courseNames = new ArrayList<>();
        Random random = new Random();

        String[] prefixes = {"Introduction to", "Advanced", "Fundamentals of", "Applied", "Principles of"};
        String[] subjects = {"Mathematics", "Physics", "Chemistry", "Biology", "Computer Science", "Literature", "History"};

        for (int i = 0; i < count; i++) {
            String prefix = prefixes[random.nextInt(prefixes.length)];
            String subject = subjects[random.nextInt(subjects.length)];
            String courseName = prefix + " " + subject;
            courseNames.add(courseName);
        }

        return courseNames;
    }

    private static double generateCredit() {
        Random random = new Random();
        double minCredit = 1.0;
        double maxCredit = 4.0;
        return minCredit + (maxCredit - minCredit) * random.nextDouble();
    }

    private static String formatDecimal(double value, int decimalPlaces) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.0");
        decimalFormat.setMaximumFractionDigits(decimalPlaces);
        return decimalFormat.format(value);
    }

    private static String generateInsertStatement(String courseName, String credit) {
        return "INSERT INTO `course` (`name`, `credit`) VALUES ('" + courseName + "', " + credit + ");";
    }
}
