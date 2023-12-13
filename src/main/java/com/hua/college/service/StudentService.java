package com.hua.college.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hua.college.entity.StudentEntity;
import com.hua.college.utils.PageUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.Map;


/**
 *
 *
 * @author
 * @email
 * @date 2023-11-13 15:57:22
 */
public interface StudentService extends IService<StudentEntity> {
    String exportStudentMsg();
    String exportStudentMsgWithRedis();
    PageUtils queryPage(Map<String, Object> params);
    ResponseEntity<Resource> downloadFile(String filename);
}

