package com.hua.college.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hua.college.dao.CourseDao;
import com.hua.college.entity.CourseEntity;
import com.hua.college.service.CourseService;


@Service("courseService")
public class CourseServiceImpl extends ServiceImpl<CourseDao, CourseEntity> implements CourseService {


}
