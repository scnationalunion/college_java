package com.hua.college.service.impl;

import com.hua.college.dao.MajorDao;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hua.college.entity.MajorEntity;
import com.hua.college.service.MajorService;


@Service("majorService")
public class MajorServiceImpl extends ServiceImpl<MajorDao, MajorEntity> implements MajorService {



}
