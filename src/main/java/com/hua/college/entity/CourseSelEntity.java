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
@TableName("course_sel")
public class CourseSelEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 选课id
	 */
	@TableId
	private Integer id;
	/**
	 * 课程id
	 */
	private Integer courseId;
	/**
	 * 学生id
	 */
	private Integer studentId;

}
