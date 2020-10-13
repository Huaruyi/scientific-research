package com.ljj.controller;

import com.ljj.dao.SchoolRepository;
import com.ljj.entity.School;
import com.ljj.util.CommonResult;
import com.ljj.util.DataGridUtil;
import com.ljj.util.TablePageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController
public class SchoolController {
    @Resource
    private SchoolRepository schoolRepository;
    /**
     * 查询所有部门
     * @param pageable 分页参数
     * @return 自定义
     */
    @GetMapping("/schools")
    public Object selectAll(TablePageable pageable){
        try {
            PageRequest pageRequest = pageable.bulidPageRequest();
            Page<School> schools = schoolRepository.findAll(pageRequest);
            return DataGridUtil.buildResult(schools);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"查询失败");
        }
    }
}
