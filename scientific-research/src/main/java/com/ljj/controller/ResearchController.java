package com.ljj.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ljj.dao.ResearchNumRepository;
import com.ljj.dao.ResearchRepository;
import com.ljj.entity.Research;
import com.ljj.entity.ResearchNum;
import com.ljj.entity.User;
import com.ljj.util.CommonResult;
import com.ljj.util.DataGridUtil;
import com.ljj.util.TablePageable;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@Slf4j
public class ResearchController {
    @Resource
    private ResearchRepository researchRepository;
    @Resource
    private ResearchNumRepository researchNumRepository;


    /**
     * 成果上传
     *
     * @param file 上传的文件
     * @return 文件路径（名）
     */
    @PostMapping("/upload")
    @ResponseBody
    public CommonResult uploadPath(@RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // 获取前台上传的文件名
            String originalFilename = file.getOriginalFilename();
            log.info("原始文件名:" + originalFilename);
            // 获取服务器上传真实路径
            String filePath = ("D://temp/");
            // 生成服务器存储的文件名
            //String fileName = IdUtil.simpleUUID() + ".docx";
            String path = filePath + originalFilename;
            log.info("最终上传的路径"+path);
            File dest = new File(path);
            log.info("服务器路径"+dest);
            // 检查目录是否存在
            if (!dest.getParentFile().exists()) {
                //不存在就新建
                boolean mkdirs = dest.getParentFile().mkdirs();
                if (mkdirs){
                    log.info("创建目录成功");
                }else {
                    log.info("创建目录失败");
                }
            }
            // 创建上传文件对象
            try {
                // 复制上传文件到指定上传目录
                log.info("开始传递文件");
                file.transferTo(dest);
            } catch (Exception e) {
                log.info("文件传递异常");
                e.printStackTrace();
            }
            return new CommonResult<>(200, true,"上传成功", originalFilename);

        } catch (Exception e) {
            System.out.println("文件上传异常");
            return new CommonResult<>(444,false, "上传失败");
        }
    }


    private final static String PATENT = "专利";
    private final static String PAPER = "论文";
    private final static String TASK = "课题";
    /**
     * 提交成果表单
     * @param research 前台传来的对象
     * @return 返回操作结果
     */
    @PostMapping("/research")
    @ResponseBody
    public CommonResult add(Research research){
        try {
            //判断是第一次传还是重传
            //如果researchId不为空则说明是重传
            if (ObjectUtil.isNotNull(research.getResearchId())){
                //设置状态为待审核
                research.setResearchState(0);
                //设置提交时间为当前时间
                research.setResearchCommitTime(LocalDateTime.now());
            //不是重传
            }else{
                //下面用来累计成果物数量
                String type = research.getResearchType();
                User user = research.getUser();
                //先查询，再根据情况+1
                ResearchNum researchNum = researchNumRepository.findByUserUserId(user.getUserId());
                if (StrUtil.equals(PAPER, type)){
                    researchNum.setPaperNum(researchNum.getPaperNum() + 1);
                }else if (StrUtil.equals(PATENT, type)){
                    researchNum.setPatentNum(researchNum.getPatentNum() + 1);
                }else if(StrUtil.equals(TASK, type)){
                    researchNum.setTaskNum(researchNum.getTaskNum() + 1);
                }
                //保存研究数量
                researchNumRepository.save(researchNum);
                //设置院系
                research.setSchool(research.getUser().getSchool());
                //设置状态为待审核
                research.setResearchState(0);
                //设置提交时间为当前时间
                research.setResearchCommitTime(LocalDateTime.now());
            }
            researchRepository.save(research);
            return new CommonResult<>(200, true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false, "操作失败");
        }
    }


    /**
     * 审核
     * @param research 前台传来的对象
     * @return 操作结果
     */
    @PutMapping("/research")
    @ResponseBody
    public CommonResult update(Research research){
        try {
            Research research1 = researchRepository.getOne(research.getResearchId());
            //有参数则替换
            if (ObjectUtil.isNotNull(research.getResearchState())){
                research1.setResearchState(research.getResearchState());
            }
            //状态为1或者2通过或者不通过都需要设置审核时间
            research1.setResearchAuditTime(LocalDateTime.now());
            researchRepository.save(research1);
            return new CommonResult<>(200, true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false, "操作失败");
        }
    }


    /**
     * 删除
     * @param researchId 前台传来的Id
     * @return 操作结果
     */
    @DeleteMapping("/research/{researchId}")
    @ResponseBody
    public CommonResult delete(@PathVariable Long researchId){
        try {
            Research research = researchRepository.getOne(researchId);
            String type = research.getResearchType();
            //先查询，再根据情况-1
            ResearchNum researchNum = researchNumRepository.findByUserUserId(research.getUser().getUserId());
            if (StrUtil.equals(PAPER, type)){
                researchNum.setPaperNum(researchNum.getPaperNum() - 1);
            }else if (StrUtil.equals(PATENT, type)){
                researchNum.setPatentNum(researchNum.getPatentNum() - 1);
            }else if(StrUtil.equals(TASK, type)){
                researchNum.setTaskNum(researchNum.getTaskNum() - 1);
            }
            //保存研究数量
            researchNumRepository.save(researchNum);
            //最后删除
            researchRepository.deleteById(researchId);
            return new CommonResult<>(200, true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false, "操作失败");
        }
    }


    /**
     * 来到编辑界面
     * @param researchId 前台传来的Id
     * @return 操作结果
     */
    @GetMapping("/research/{researchId}")
    public String gotoEdit(@PathVariable Long researchId , Model model){
        Research research = researchRepository.getOne(researchId);
        model.addAttribute("research",research);
        return "addResearch";
    }


    /**
     * 研究成果列表，带模糊搜索
     * @param userId id
     * @param pageable 分页参数
     * @param research 前台传来的对象
     * @return 分页结果
     */
    @GetMapping("/researches")
    @ResponseBody
    public Object selectAll(String userId, Integer schoolId, TablePageable pageable, Research research){
        try {
            PageRequest pageRequest = pageable.bulidPageRequest();
            Page<Research> researches = researchRepository.findAll(new Specification<T>() {
                @Override
                public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<>();
                    //字符串是否为空白 空白的定义如下：
                    //1、为null
                    //2、为不可见字符（如空格）
                    //3、""
                    if (StrUtil.isNotBlank(userId)){
                        predicates.add(cb.equal(root.<User>get("user").get("userId"),userId));
                    }
                    //拼接搜索条件：成果类型
                    String researchType = research.getResearchType();
                    if (StrUtil.isNotBlank(researchType)){
                        predicates.add(cb.like(root.get("researchType"),"%" + researchType + "%"));
                    }
                    //拼接搜索条件：成果提交（上传）时间
                    LocalDateTime researchCommitTime = research.getResearchCommitTime();
                    if (ObjectUtil.isNotNull(researchCommitTime)){
                        predicates.add(cb.like(root.get("researchCommitTime").as(String.class),"%" + researchCommitTime.toLocalDate() + "%"));
                    }
                    //拼接搜索条件：成果审核时间
                    LocalDateTime researchAuditTime = research.getResearchAuditTime();
                    if (ObjectUtil.isNotNull(researchAuditTime)){
                        predicates.add(cb.like(root.get("researchCommitTime").as(String.class),"%" + researchAuditTime.toLocalDate() + "%"));
                    }
                    //拼接搜索条件：成果审核状态
                    Integer researchState = research.getResearchState();
                    if (ObjectUtil.isNotNull(researchState)){
                        predicates.add(cb.equal(root.get("researchState"), researchState));
                    }
                    cq.where(predicates.toArray(new Predicate[predicates.size()]));
                    return null;
                }
            },pageRequest);
            return DataGridUtil.buildResult(researches);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"查询失败");
        }
    }
}
