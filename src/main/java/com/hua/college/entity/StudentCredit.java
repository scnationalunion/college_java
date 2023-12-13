package com.hua.college.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("student_total_credit")
public class StudentCredit {
    Integer studentId;
    Double totalCredit;
}
