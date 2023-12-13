package com.hua.college;

import java.util.Random;

public class GenerateInsertStatements {

    public static void main(String[] args) {
        int numOfRecords = 100;

        StringBuilder sqlStatements = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < numOfRecords; i++) {
            int courseId = random.nextInt(101) + 1;
            int studentId = random.nextInt(5000) + 1;

            sqlStatements.append("INSERT INTO course_sel (course_id, student_id) VALUES (")
                    .append(courseId)
                    .append(", ")
                    .append(studentId)
                    .append(");\n");
        }

        System.out.println(sqlStatements.toString());
    }
}
