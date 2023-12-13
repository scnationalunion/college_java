package com.hua.college.dao;

import com.hua.college.entity.SchoolEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface SchoolDao extends BaseMapper<SchoolEntity> {
	@Select("SELECT s.id,s.`name`,m.`name` as `majorName` FROM school s join major m on s.id = m.school_id WHERE s.id = #{schoolId}")
    public List<SchoolEntity> getSchoolAndMajor(@Param("schoolId") Integer schoolId);
}
