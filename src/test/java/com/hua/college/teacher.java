package com.hua.college;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class teacher {

    public static void main(String[] args) {
        List<String> teacherNames = generateTeacherNames(100);

        for (String name : teacherNames) {
            int telephoneNumber = generateTelephoneNumber();
            String insertStatement = generateInsertStatement(name, telephoneNumber);
            System.out.println(insertStatement);
        }
    }

    private static List<String> generateTeacherNames(int count) {
        List<String> teacherNames = new ArrayList<>();
        Random random = new Random();

        String[] firstNames = {"John", "Mary", "David", "Lisa", "Michael", "Sarah", "Daniel", "Emily"};
        String[] lastNames = {"Smith", "Johnson", "Brown", "Taylor", "Miller", "Wilson", "Clark", "Lee"};

        for (int i = 0; i < count; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String teacherName = firstName + " " + lastName;
            teacherNames.add(teacherName);
        }

        return teacherNames;
    }

    private static int generateTelephoneNumber() {
        Random random = new Random();
        int minNumber = 100000000;
        int maxNumber = 999999999;
        return random.nextInt(maxNumber - minNumber + 1) + minNumber;
    }

    private static String generateInsertStatement(String teacherName, int telephoneNumber) {
        return "INSERT INTO `teacher` (`name`, `telephone_number`) VALUES ('" + teacherName + "', " + telephoneNumber + ");";
    }
}
