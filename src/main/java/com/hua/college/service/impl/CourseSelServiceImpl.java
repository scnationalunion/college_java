package com.hua.college.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hua.college.dao.CourseSelDao;
import com.hua.college.entity.CourseSelEntity;
import com.hua.college.service.CourseSelService;


@Service("courseSelService")
public class CourseSelServiceImpl extends ServiceImpl<CourseSelDao, CourseSelEntity> implements CourseSelService {


}
