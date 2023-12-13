package com.hua.college;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MajorInsertGenerator {

    public static void main(String[] args) {
        List<String> majorNames = generateMajorNames(1000);

        for (String name : majorNames) {
            int schoolId = generateSchoolId(1, 101);
            String insertStatement = generateInsertStatement(name, schoolId);
            System.out.println(insertStatement);
        }
    }

    private static List<String> generateMajorNames(int count) {
        List<String> majorNames = new ArrayList<>();
        Random random = new Random();

        String[] prefixes = {"Computer Science", "Mechanical Engineering", "Electrical Engineering", "Business Administration", "Biology"};
        String[] suffixes = {"", " Technology", " Studies", " and Management"};

        for (int i = 0; i < count; i++) {
            String prefix = prefixes[random.nextInt(prefixes.length)];
            String suffix = suffixes[random.nextInt(suffixes.length)];
            String majorName = prefix + suffix;
            majorNames.add(majorName);
        }

        return majorNames;
    }

    private static int generateSchoolId(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    private static String generateInsertStatement(String majorName, int schoolId) {
        return "INSERT INTO `major` (`name`, `school_id`) VALUES ('" + majorName + "', " + schoolId + ");";
    }
}
