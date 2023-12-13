package com.hua.college.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

import com.hua.college.utils.PageUtils;
import com.hua.college.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.hua.college.entity.StudentEntity;
import com.hua.college.service.StudentService;


@RestController
@Slf4j
@RequestMapping("college/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @RequestMapping("/export")
    public R export(){
        String filename = studentService.exportStudentMsg();
//        ResponseEntity<Resource> resourceResponseEntity = studentService.downloadFile(filename);
        return R.ok().put("filename", filename)/*.put("response",resourceResponseEntity)*/;
    }
    @RequestMapping("/export/redis")
    public R exportredis(){
        String filename = studentService.exportStudentMsgWithRedis();
        return R.ok().put("filename", filename);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = studentService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		StudentEntity student = studentService.getById(id);

        return R.ok().put("student", student);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody StudentEntity student){
		studentService.save(student);

        return R.ok();
    }

    /**
     * 修改
     */
    @Transactional
    @RequestMapping("/update")
    public R update(@RequestBody StudentEntity student){
		studentService.updateById(student);
        if (student.getId() == 1){
            int i=1/0;
        }
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		studentService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile() {
//        String filename = "student_list.xlsx"; // 设置要下载的文件路径
        String filename = studentService.exportStudentMsgWithRedis();
        try {
            Path filePath = Paths.get(filename);
            Resource resource = new UrlResource(filePath.toUri());

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
