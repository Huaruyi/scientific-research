package com.ljj.controller;

import com.ljj.dao.SchoolRepository;
import com.ljj.entity.School;
import com.ljj.util.DataGridUtil;
import com.ljj.util.TablePageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SchoolController {
    @Resource
    private SchoolRepository schoolRepository;
    /**
     * 查询所有部门
     * @param pageable 分页参数
     * @return
     */
    @GetMapping("/schools")
    public Object selectAll(TablePageable pageable){
        PageRequest pageRequest = pageable.bulidPageRequest();
        Page<School> schools = schoolRepository.findAll(pageRequest);
        return DataGridUtil.buildResult(schools);
    }
}
