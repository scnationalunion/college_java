package com.hua.college.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.college.dao.SchoolDao;
import org.springframework.stereotype.Service;

import com.hua.college.entity.SchoolEntity;
import com.hua.college.service.SchoolService;


@Service("schoolService")
public class SchoolServiceImpl extends ServiceImpl<SchoolDao, SchoolEntity> implements SchoolService {


}
