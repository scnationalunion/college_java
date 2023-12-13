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
@TableName("major")
public class MajorEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 专业id
	 */
	@TableId
	private Integer id;
	/**
	 * 专业名
	 */
	private String name;
	/**
	 * 学院id
	 */
	private Integer schoolId;

}
