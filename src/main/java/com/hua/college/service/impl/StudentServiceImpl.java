package com.hua.college.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hua.college.dao.*;
import com.hua.college.entity.*;
import com.hua.college.utils.PageUtils;
import com.hua.college.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hua.college.service.StudentService;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("studentService")
public class StudentServiceImpl extends ServiceImpl<StudentDao, StudentEntity> implements StudentService {

    @Autowired
    SchoolDao schoolDao;
    @Autowired
    TeacherDao teacherDao;
    @Autowired
    CourseDao courseDao;
    @Autowired
    CourseSelDao courseSelDao;
    @Autowired
    MajorDao majorDao;
    @Override
    public String exportStudentMsg() {
        List<StudentEntity> list = this.list();
        Map<Integer, Double> allCredit = courseSelDao.getAllCredit().stream()
                .collect(Collectors.toMap(StudentCredit::getStudentId, StudentCredit::getTotalCredit));
        List<StudentExcelVo> studentExcelVoList = list.parallelStream()
                .map(stu->{
                    return this.convertToStudentExcelVo(stu,allCredit);
                })
                .collect(Collectors.toList());
        String fileName = "student_list.xlsx"; // 指定导出的文件名

        // 导出Excel
        EasyExcel.write(fileName, StudentExcelVo.class)
                .sheet("学生列表")
                .doWrite(studentExcelVoList);
        return fileName;
    }

    private StudentExcelVo convertToStudentExcelVo(StudentEntity student, Map<Integer, Double> allCredit) {
        StudentExcelVo studentExcelVo = new StudentExcelVo();
        studentExcelVo.setId(student.getId());
        studentExcelVo.setName(student.getName());
        studentExcelVo.setYear(student.getYear());
        // TODO: 设置学院、专业、辅导员等属性

        SchoolEntity schoolEntity = schoolDao.selectById(student.getSchoolId());
        if(Objects.nonNull(schoolEntity)){
            studentExcelVo.setSchoolName(schoolEntity.getName());
        }

        MajorEntity majorEntity = majorDao.selectById(student.getMajorId());
        if(Objects.nonNull(majorEntity)){
            studentExcelVo.setMajorName(majorEntity.getName());
        }

        TeacherEntity teacherEntity = teacherDao.selectById(student.getStuInstructorId());
        if(Objects.nonNull(teacherEntity)){
            studentExcelVo.setStuInstructorName(teacherEntity.getName());
            studentExcelVo.setStuInstructorTel(teacherEntity.getTelephoneNumber());
        }

        //学分
//        Double creditByStuId = courseSelDao.getCreditByStuId(student.getId());
//        studentExcelVo.setCredit(Objects.nonNull(creditByStuId) ? creditByStuId : 0);
        studentExcelVo.setCredit(Objects.nonNull(allCredit.get(student.getId())) ? allCredit.get(student.getId()) : 0D);
        return studentExcelVo;
    }

    public String exportStudentMsgWithRedis() {
        List<StudentEntity> list = this.list();
//        Map<Integer, CourseEntity> courseMap = courseDao.selectList(null).stream()
//                .collect(Collectors.toMap(CourseEntity::getId, course -> course));
        Map<Integer, Double> allCredit = courseSelDao.getAllCredit().stream()
                .collect(Collectors.toMap(StudentCredit::getStudentId, StudentCredit::getTotalCredit));
        List<StudentExcelVo> studentExcelVoList = list.parallelStream()
                .map(stu->{
                    return this.convertToStudentExcelVoWithRedis(stu,allCredit);
                })
                .collect(Collectors.toList());
        String fileName = "student_list.xlsx"; // 指定导出的文件名

        // 导出Excel
        EasyExcel.write(fileName, StudentExcelVo.class)
                .sheet("学生列表")
                .doWrite(studentExcelVoList);
        return fileName;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String)params.get("key");
        IPage<StudentEntity> page = this.page(
                new Query<StudentEntity>().getPage(params),
                new QueryWrapper<StudentEntity>().likeRight("name",key)
        );
        return new PageUtils(page);
    }



    @Resource
    private StringRedisTemplate redis;
    private StudentExcelVo convertToStudentExcelVoWithRedis(StudentEntity student,Map<Integer, Double> allCredit) {
        StudentExcelVo studentExcelVo = new StudentExcelVo();
        studentExcelVo.setId(student.getId());
        studentExcelVo.setName(student.getName());
        studentExcelVo.setYear(student.getYear());

        SchoolEntity schoolEntity;
        String school = redis.opsForValue().get("school:" + student.getSchoolId());
        if(Objects.isNull(school)){
           schoolEntity= schoolDao.selectById(student.getSchoolId());
           if(Objects.nonNull(schoolEntity))
               redis.opsForValue().set("school:"+student.getSchoolId(), JSON.toJSONString(schoolEntity),30, TimeUnit.SECONDS);
       }else {
            schoolEntity= JSON.parseObject(school, SchoolEntity.class);
        }
        if(Objects.nonNull(schoolEntity)){
            studentExcelVo.setSchoolName(schoolEntity.getName());
        }


        MajorEntity majorEntity;
        String major = redis.opsForValue().get("major:" + student.getMajorId());
        if(Objects.isNull(major)){
            majorEntity=majorDao.selectById(student.getMajorId());
            if(Objects.nonNull(majorEntity)){
                redis.opsForValue().set("major:"+student.getMajorId(), JSON.toJSONString(majorEntity),30, TimeUnit.SECONDS);
            }
        }else {
            majorEntity=JSON.parseObject(major,MajorEntity.class);
        }
        if(Objects.nonNull(majorEntity)){
            studentExcelVo.setMajorName(majorEntity.getName());
        }

        TeacherEntity teacherEntity;
        String teacher = redis.opsForValue().get("teacher:" + student.getStuInstructorId());
        if(Objects.isNull(teacher)){
            teacherEntity=teacherDao.selectById(student.getStuInstructorId());
            if(Objects.nonNull(teacherEntity)){
                redis.opsForValue().set("teacher:"+student.getStuInstructorId(), JSON.toJSONString(teacherEntity),30, TimeUnit.SECONDS);
            }
        }else {
            teacherEntity= JSON.parseObject(teacher,TeacherEntity.class);
        }
        if(Objects.nonNull(teacherEntity)){
            studentExcelVo.setStuInstructorName(teacherEntity.getName());
            studentExcelVo.setStuInstructorTel(teacherEntity.getTelephoneNumber());
        }

        //学分
//        List<CourseSelEntity> courseSelEntities = courseSelDao.selectList
//                (new LambdaQueryWrapper<CourseSelEntity>().eq(CourseSelEntity::getStudentId, student.getId()));
//        Double credit= courseSelEntities.stream()
//                .map(courseSelEntity -> {
//                    return courseMap.get(courseSelEntity.getCourseId()).getCredit();
//                })
//                .reduce(0.0,Double::sum);
//        studentExcelVo.setCredit(credit);
        //学分
//        Double creditByStuId = courseSelDao.getCreditByStuId(student.getId());
//        studentExcelVo.setCredit(Objects.nonNull(creditByStuId) ? creditByStuId : 0);
        //学分
        studentExcelVo.setCredit(Objects.nonNull(allCredit.get(student.getId())) ? allCredit.get(student.getId()) : 0D);
        return studentExcelVo;
    }

    public ResponseEntity<org.springframework.core.io.Resource> downloadFile(String filename) {
        try {
            Path filePath = Paths.get(filename);
            org.springframework.core.io.Resource resource = new UrlResource(filePath.toUri());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (MalformedURLException e) {
            // 处理文件路径错误异常
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            // 处理文件读取异常
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
