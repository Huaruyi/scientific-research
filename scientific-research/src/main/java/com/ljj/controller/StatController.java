package com.ljj.controller;

import com.ljj.dao.ResearchRepository;
import com.ljj.entity.Research;
import com.ljj.entity.School;
import com.ljj.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StatController {
    @Resource
    private ResearchRepository researchRepository;
    Map<String ,Integer> map = new HashMap<>();


    /**
     * 统计通过率
     * @param userId 作者
     * @param range 用于确认范围，1代表上半年，2代表下半年
     * @param year 年份
     * @return map
     */
    @GetMapping("/passingRate")
    public Object passingRate(String userId, Integer range, Integer year){
        map.clear();
        //开始时间，用于最终sql查询参数
        LocalDateTime startDateTime;
        //终止时间，用于最终sql查询参数
        LocalDateTime endDateTime;
        //开始月份，1月或者7月
        int startMonth;
        //结束月份，6月或者12月
        int endMonth;
        //结束日期，30号或者31号
        int endDay;
        //判断前台查询参数，1上半年，2是下半年
        if (range == 1){
            //如果是上半年，则为1.1~6.30
            startMonth = 1;
            endMonth = 6;
            endDay = 30;
        }else{
            //如果是下半年，则为7.1~12.31
            startMonth = 7;
            endMonth = 12;
            endDay = 31;
        }
        //构造开始年月日
        startDateTime = LocalDateTime.of(year, startMonth, 1, 0, 0, 0);
        //构造终止年月日
        endDateTime = LocalDateTime.of(year, endMonth, endDay, 0, 0, 0);
        //条件查询
        List<Research> all = researchRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //查询工号
                predicates.add(cb.equal(root.<User>get("user").get("userId"), userId));
                //查询状态为1的 状态1代表通过审核的
                predicates.add(cb.equal(root.get("researchState"), 1));
                //查询开始~结束时间段内的
                //查询日期大于或等于开始时间的
                predicates.add(cb.greaterThanOrEqualTo(root.get("researchCommitTime"),startDateTime));
                //查询日期小于或等于终止时间的
                predicates.add(cb.lessThanOrEqualTo(root.get("researchCommitTime"),endDateTime));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        });
        List<Research> all1 = researchRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //查询工号
                predicates.add(cb.equal(root.<User>get("user").get("userId"), userId));
                //查询状态为2的 状态2代表未通过审核的
                predicates.add(cb.equal(root.get("researchState"), 2));
                //查询开始~结束时间段内的
                //查询日期大于或等于开始时间的
                predicates.add(cb.greaterThanOrEqualTo(root.get("researchCommitTime"),startDateTime));
                //查询日期小于或等于终止时间的
                predicates.add(cb.lessThanOrEqualTo(root.get("researchCommitTime"),endDateTime));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        });
        //拿到数量
        int size = all.size();
        int size1 = all1.size();
        //存入map
        map.put("通过数",size);
        map.put("未通过数",size1);
        return map;
    }


    /**
     * 按学期（半年）统计，按状态显示
     * @param range 用于确认范围，1代表上半年，2代表下半年
     * @param year 年份
     * @return
     */
    @GetMapping("/halfYear/state")
    public Object halfYearAndState(Integer range, Integer year){
        map.clear();
        //开始时间，用于最终sql查询参数
        LocalDateTime startDateTime;
        //终止时间，用于最终sql查询参数
        LocalDateTime endDateTime;
        //开始月份，1月或者7月
        int startMonth;
        //结束月份，6月或者12月
        int endMonth;
        //结束日期，30号或者31号
        int endDay;
        //判断前台查询参数，1上半年，2是下半年
        if (range == 1){
            //如果是上半年，则为1.1~6.30
            startMonth = 1;
            endMonth = 6;
            endDay = 30;
        }else{
            //如果是下半年，则为7.1~12.31
            startMonth = 7;
            endMonth = 12;
            endDay = 31;
        }
        //构造开始年月日
        startDateTime = LocalDateTime.of(year, startMonth, 1, 0, 0, 0);
        //构造终止年月日
        endDateTime = LocalDateTime.of(year, endMonth, endDay, 0, 0, 0);
        //条件查询
        List<Research> all = researchRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //查询状态为1的 状态1代表通过审核的
                predicates.add(cb.equal(root.get("researchState"), 1));
                //查询开始~结束时间段内的
                //查询日期大于或等于开始时间的
                predicates.add(cb.greaterThanOrEqualTo(root.get("researchCommitTime"),startDateTime));
                //查询日期小于或等于终止时间的
                predicates.add(cb.lessThanOrEqualTo(root.get("researchCommitTime"),endDateTime));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        });
        List<Research> all1 = researchRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //查询状态为2的 状态2代表未通过审核的
                predicates.add(cb.equal(root.get("researchState"), 2));
                //查询开始~结束时间段内的
                //查询日期大于或等于开始时间的
                predicates.add(cb.greaterThanOrEqualTo(root.get("researchCommitTime"),startDateTime));
                //查询日期小于或等于终止时间的
                predicates.add(cb.lessThanOrEqualTo(root.get("researchCommitTime"),endDateTime));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        });
        //拿到数量
        int size = all.size();
        int size1 = all1.size();
        //存入map
        map.put("通过数",size);
        map.put("未通过数",size1);
        return map;
    }


    /**
     * 按学期（半年）统计
     * @param range 用于确认范围，1代表上半年，2代表下半年
     * @param year 年份
     * @return
     */
    @GetMapping("/halfYear/type")
    public Object halfYearAndType(Integer range, Integer year){
        map.clear();
        //开始时间，用于最终sql查询参数
        LocalDateTime startDateTime;
        //终止时间，用于最终sql查询参数
        LocalDateTime endDateTime;
        //开始月份，1月或者7月
        int startMonth;
        //结束月份，6月或者12月
        int endMonth;
        //结束日期，30号或者31号
        int endDay;
        //判断前台查询参数，1上半年，2是下半年
        if (range == 1){
            //如果是上半年，则为1.1~6.30
            startMonth = 1;
            endMonth = 6;
            endDay = 30;
        }else{
            //如果是下半年，则为7.1~12.31
            startMonth = 7;
            endMonth = 12;
            endDay = 31;
        }
        //构造开始年月日
        startDateTime = LocalDateTime.of(year, startMonth, 1, 0, 0, 0);
        //构造终止年月日
        endDateTime = LocalDateTime.of(year, endMonth, endDay, 0, 0, 0);
        //条件查询
        List<Research> all = researchRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //查询论文
                predicates.add(cb.equal(root.get("researchType"), "论文"));
                //查询开始~结束时间段内的
                //查询日期大于或等于开始时间的
                predicates.add(cb.greaterThanOrEqualTo(root.get("researchCommitTime"),startDateTime));
                //查询日期小于或等于终止时间的
                predicates.add(cb.lessThanOrEqualTo(root.get("researchCommitTime"),endDateTime));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        });
        List<Research> all1 = researchRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //查询专利
                predicates.add(cb.equal(root.get("researchType"), "专利"));
                //查询开始~结束时间段内的
                //查询日期大于或等于开始时间的
                predicates.add(cb.greaterThanOrEqualTo(root.get("researchCommitTime"),startDateTime));
                //查询日期小于或等于终止时间的
                predicates.add(cb.lessThanOrEqualTo(root.get("researchCommitTime"),endDateTime));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        });
        List<Research> all2 = researchRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //查询专利
                predicates.add(cb.equal(root.get("researchType"), "课题"));
                //查询开始~结束时间段内的
                //查询日期大于或等于开始时间的
                predicates.add(cb.greaterThanOrEqualTo(root.get("researchCommitTime"),startDateTime));
                //查询日期小于或等于终止时间的
                predicates.add(cb.lessThanOrEqualTo(root.get("researchCommitTime"),endDateTime));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        });
        //拿到数量
        int size = all.size();
        int size1 = all1.size();
        int size2 = all2.size();
        //存入map
        map.put("论文",size);
        map.put("专利",size1);
        map.put("课题",size2);
        return map;
    }


    /**
     * 按院系统计
     *
     */
    @GetMapping("/halfYear/school")
    public Object halfYearAndSchool(Integer range, Integer year){
        map.clear();
        //开始时间，用于最终sql查询参数
        LocalDateTime startDateTime;
        //终止时间，用于最终sql查询参数
        LocalDateTime endDateTime;
        //开始月份，1月或者7月
        int startMonth;
        //结束月份，6月或者12月
        int endMonth;
        //结束日期，30号或者31号
        int endDay;
        //判断前台查询参数，1上半年，2是下半年
        if (range == 1){
            //如果是上半年，则为1.1~6.30
            startMonth = 1;
            endMonth = 6;
            endDay = 30;
        }else{
            //如果是下半年，则为7.1~12.31
            startMonth = 7;
            endMonth = 12;
            endDay = 31;
        }
        //构造开始年月日
        startDateTime = LocalDateTime.of(year, startMonth, 1, 0, 0, 0);
        //构造终止年月日
        endDateTime = LocalDateTime.of(year, endMonth, endDay, 0, 0, 0);

        List<Object[]> all = researchRepository.halfYearAndSchool(startDateTime, endDateTime);

        map.put("信息工程学院",0);
        map.put("工学院",0);
        map.put("外语学院",0);
        map.put("音乐学院",0);
        map.put("体育学院",0);
        map.put("经济与管理学院",0);
        map.put("文法学院",0);
        map.put("食品工程学院",0);

        for (Object[] objects : all) {
            for (int i = 0; i < objects.length; i++) {
                if (String.valueOf(objects[0]).equals("1")){
                    map.put("信息工程学院",Integer.parseInt(String.valueOf(objects[1])));

                }else if (String.valueOf(objects[0]).equals("2")){
                    map.put("工学院",Integer.parseInt(String.valueOf(objects[1])));

                }else if (String.valueOf(objects[0]).equals("3")){
                    map.put("外语学院",Integer.parseInt(String.valueOf(objects[1])));

                }else if (String.valueOf(objects[0]).equals("4")){
                    map.put("音乐学院",Integer.parseInt(String.valueOf(objects[1])));

                }else if (String.valueOf(objects[0]).equals("5")){
                    map.put("体育学院",Integer.parseInt(String.valueOf(objects[1])));

                } else if (String.valueOf(objects[0]).equals("6")){
                    map.put("经济与管理学院",Integer.parseInt(String.valueOf(objects[1])));

                } else if (String.valueOf(objects[0]).equals("7")){
                    map.put("文法学院",Integer.parseInt(String.valueOf(objects[1])));

                } else if (String.valueOf(objects[0]).equals("8")){
                    map.put("食品工程学院",Integer.parseInt(String.valueOf(objects[1])));

                }
            }
        }

        return map;
    }
}

