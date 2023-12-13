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
@TableName("school")
public class SchoolEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 学院id
	 */
	@TableId
	private Integer id;
	/**
	 * 学院名字
	 */
	private String name;
//	/**
//	 * 专业名字：为了联表查询学生的学院和专业
//	 */
//	private String majorName;
}
