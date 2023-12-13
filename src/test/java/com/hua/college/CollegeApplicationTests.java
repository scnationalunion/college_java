package com.hua.college;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class CollegeApplicationTests {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Test
    public void contextLoads() {
        // 写入一条String数据
        stringRedisTemplate.opsForValue().set("verify:phone:13600527634", "124143");
        // 获取string数据
        Object name = stringRedisTemplate.opsForValue().get("verify:phone:13600527634");
        System.out.println("name = " + name);
    }

    public static void main(String[] args) {
        List<String> schoolNames = generateSchoolNames(100);

        for (String name : schoolNames) {
            String insertStatement = generateInsertStatement(name);
            System.out.println(insertStatement);
        }
    }

    private static List<String> generateSchoolNames(int count) {
        List<String> schoolNames = new ArrayList<>();
        Random random = new Random();

        String[] prefixes = {"School of", "College of", "Department of", "Institute of"};
        String[] subjects = {"Art", "Science", "Engineering", "Business", "Medicine", "Law", "Education"};

        for (int i = 0; i < count; i++) {
            String prefix = prefixes[random.nextInt(prefixes.length)];
            String subject = subjects[random.nextInt(subjects.length)];
            String schoolName = prefix + " " + subject;
            schoolNames.add(schoolName);
        }

        return schoolNames;
    }

    private static String generateInsertStatement(String schoolName) {
        return "INSERT INTO `school` (`name`) VALUES ('" + schoolName + "');";
    }
}
