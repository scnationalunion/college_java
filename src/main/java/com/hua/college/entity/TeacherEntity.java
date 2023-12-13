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
@TableName("teacher")
public class TeacherEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 教师id
	 */
	@TableId
	private Integer id;
	/**
	 * 教师名字
	 */
	private String name;
	/**
	 * 教师电话
	 */
	private Integer telephoneNumber;

}
