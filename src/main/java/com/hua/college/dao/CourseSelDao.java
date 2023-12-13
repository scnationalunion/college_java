package com.hua.college.dao;

import com.hua.college.entity.CourseSelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hua.college.entity.StudentCredit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *
 *
 * @author
 * @email
 * @date 2023-11-13 15:57:22
 */
@Mapper
public interface CourseSelDao extends BaseMapper<CourseSelEntity> {
	@Select("SELECT SUM(c.credit) FROM course  c join course_sel cl on c.id = cl.course_id WHERE cl.student_id = #{stuId};")
    Double getCreditByStuId(@Param("stuId") Integer stuId);

    @Select("select * from student_total_credit")
    List<StudentCredit> getAllCredit();
}
