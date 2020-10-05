package com.ljj.controller;

import com.ljj.dao.ResearchNumRepository;
import com.ljj.entity.ResearchNum;
import com.ljj.entity.vo.ResearchNumVO;
import com.ljj.util.CommonResult;
import com.ljj.util.DataGridUtil;
import com.ljj.util.TablePageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class ResearchNumController {
    @Resource
    ResearchNumRepository researchNumRepository;

    @GetMapping("/researchNums")
    public Object selectAll(TablePageable pageable){
        PageRequest pageRequest = pageable.bulidPageRequest();
        Page<ResearchNum> researchNums = researchNumRepository.findAll(pageRequest);
        return DataGridUtil.buildResult(researchNums);
    }

    /**
     * 提供下载所需数据
     * 需要手动封装VO对象
     * @return
     */
    @GetMapping("/researchNums/download")
    public CommonResult download(){
        List<ResearchNum> researchNums = researchNumRepository.findAll();
        List<ResearchNumVO> researchNumVOs= new ArrayList<>(20);
        for (ResearchNum researchNum : researchNums) {
            //创建对象用于存放
            ResearchNumVO researchNumVO = new ResearchNumVO();
            researchNumVO.setUserId(researchNum.getUser().getUserId());
            researchNumVO.setUserRealName(researchNum.getUser().getUserRealName());
            researchNumVO.setUserBirth(researchNum.getUser().getUserBirth());
            String gender = "男";
            if (researchNum.getUser().getUserGender() == 0){
                gender = "女";
            }
            researchNumVO.setUserGender(gender);
            researchNumVO.setUserEducationBackground(researchNum.getUser().getUserEducationBackground());
            researchNumVO.setSchoolName(researchNum.getUser().getSchool().getSchoolName());
            researchNumVO.setUserTitle(researchNum.getUser().getUserTitle());
            researchNumVO.setUserCreateTime(researchNum.getUser().getUserCreateTime());
            researchNumVO.setPaperNum(researchNum.getPaperNum());
            researchNumVO.setPatentNum(researchNum.getPatentNum());
            researchNumVO.setTaskNum(researchNum.getTaskNum());
            researchNumVOs.add(researchNumVO);
        }
        return new CommonResult<>(200,true,"OK",researchNumVOs);
    }
}
