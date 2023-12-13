package com.hua.college.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class StudentExcelVo implements Serializable {
    /**
     * id
     */
    @ExcelProperty("学号")
    private Integer id;
    /**
     * 名字
     */
    @ExcelProperty("名字")
    private String name;
    /**
     * 入学年份
     */
    @ExcelProperty("入学年份")
    private Integer year;
    /**
     * 学院
     */
    @ExcelProperty("学院")
    private String schoolName;
    /**
     * 专业
     */
    @ExcelProperty("专业")
    private String  majorName;
    /**
     * 辅导员
     */
    @ExcelProperty("辅导员")
    private String  stuInstructorName;
    /**
     * 辅导员电话
     */
    @ExcelProperty("辅导员电话")
    private Integer stuInstructorTel;
    /**
     * 学分
     */
    @ExcelProperty("学分")
    private Double credit;
}
