package com.hua.college.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hua.college.dao.TeacherDao;
import com.hua.college.entity.TeacherEntity;
import com.hua.college.service.TeacherService;


@Service("teacherService")
public class TeacherServiceImpl extends ServiceImpl<TeacherDao, TeacherEntity> implements TeacherService {


}
