package com.hua.college.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author 
 * @email 
 * @date 2023-11-13 15:57:22
 */
@Data
@TableName("student")
public class StudentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 名字
	 */
	private String name;
	/**
	 * 入学年份
	 */
	private Integer year;
	/**
	 * 学院id
	 */
	private Integer schoolId;
	/**
	 * 专业id
	 */
	private Integer majorId;
	/**
	 * 辅导员id
	 */
	private Integer stuInstructorId;
	/**
	 * 学分
	 */
	private Double credit;

}
