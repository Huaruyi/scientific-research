package com.ljj.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.ljj.dao.ResearchNumRepository;
import com.ljj.dao.SchoolRepository;
import com.ljj.dao.UserRepository;
import com.ljj.entity.ResearchNum;
import com.ljj.entity.School;
import com.ljj.entity.User;
import com.ljj.util.CommonResult;
import com.ljj.util.DataGridUtil;
import com.ljj.util.TablePageable;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {

    @Resource
    private UserRepository userRepository;
    @Resource
    private SchoolRepository schoolRepository;
    @Resource
    private ResearchNumRepository researchNumRepository;

    private final static int ZERO = 0;
    private final static int ONE = 1;


    /**
     * 修改密码
     * @param user 前台传来的数据
     * @param session user
     * @return 自定义
     */
    @PutMapping("/userSetPass")
    public CommonResult updatePass(User user, HttpSession session){
        try {
            //根据工号拿到用户信息
            User user1 = userRepository.getOne(user.getUserId());
            //设置新密码
            user1.setUserPassword(user.getUserPassword());
            //改密之后设置状态为1
            //0为初始密码状态 需要改密 1为正常
            if (user1.getUserState() == ZERO){
                user1.setUserState(ONE);
            }
            //设置最近更新时间为当前时间
            user1.setUserLastUpdateTime(LocalDateTime.now());
            //更新用户信息
            User user2 = userRepository.save(user1);
            //移除之前保存旧信息的session
            session.removeAttribute("user");
            //重新设置session
            session.setAttribute("user",user2);
            return new CommonResult<>(200,true,"改密成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"改密失败");
        }
    }


    /**
     * 更新
     * @param user 前台传来的数据
     * @return 自定义
     */
    @PutMapping("/user")
    public CommonResult update(User user){
        try {
            User user1 = userRepository.getOne(user.getUserId());
            //密码不为空则更新密码
            if (StrUtil.isNotBlank(user.getUserPassword())){
                user1.setUserPassword(user.getUserPassword());
            }
            //状态不为空则更新状态
            if (ObjectUtil.isNotNull(user.getUserState())){
                user1.setUserState(user.getUserState());
            }
            //生日不为空则更新生日
            if(ObjectUtil.isNotNull(user.getUserBirth())){
                user1.setUserBirth(user.getUserBirth());
            }
            //性别不为空则更新性别
            if (ObjectUtil.isNotNull(user.getUserGender())){
                user1.setUserGender(user.getUserGender());
            }
            //编制不为空则更新
            if (StrUtil.isNotBlank(user.getUserTitle())){
                user1.setUserTitle(user.getUserTitle());
            }
            //学历不为空则更新学历
            if (StrUtil.isNotBlank(user.getUserEducationBackground())){
                user1.setUserEducationBackground(user.getUserEducationBackground());
            }
            //姓名不为空则更新姓名
            if (StrUtil.isNotBlank(user.getUserRealName())){
                user1.setUserRealName(user.getUserRealName());
            }
            //设置最近更新时间为当前时间
            user1.setUserLastUpdateTime(LocalDateTime.now());
            userRepository.save(user1);
            return new CommonResult<>(200,true,"更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"更新失败");
        }
    }


    /**
     * 添加一个
     * @param user 前台传来的数据
     * @return 自定义
     */
    @PostMapping("/user")
    public CommonResult add(User user){
        try {
            //设置创建时间为当前时间
            user.setUserCreateTime(LocalDateTime.now());
            //设置用户状态为0
            user.setUserState(0);
            //设置初始密码为123456
            user.setUserPassword("123456");
            //添加用户
            User user1 = userRepository.saveAndFlush(user);
            //创建用户同时创建科研成果数量
            ResearchNum researchNum = new ResearchNum();
            //不是自增主键，用保存前的user和数据库插入后返回的user1都可以
            //使用user1更保险，因为原user可能会插入失败
            researchNum.setUser(user1);
            researchNumRepository.save(researchNum);
            return new CommonResult<>(200,true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"添加失败");

        }
    }


    /**
     * 批量添加
     * @param file 上传的文件
     * @return 自定义
     */
    @RequestMapping("/saves")
    public CommonResult saves(@RequestParam(value = "file", required = false) MultipartFile file) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            //调用hutool工具包的ExcelReader读取excel表格
            //需要导入maven坐标 org.apache.poi 配合
            ExcelReader excelReader = ExcelUtil.getReader(inputStream);
            //从第1行读到最后(第0行是表头不读进来)
            List<List<Object>> read = excelReader.read(1, excelReader.getRowCount());
            //用于保存excel传读取到的user集合
            List<User> users = new ArrayList<>(20);
            //保存科研成果
            List<ResearchNum> researchNums = new ArrayList<>(20);
            //拿到所有学院信息，方便下面与excel传来的数据做判断
            List<School> schools = schoolRepository.findAll();
            //处理数据
            for (List<Object> objects : read) {
                //创建一个User对象保存一个user的信息
                User user = new User();
                //创建一个ResearchNum对象保存一个researchNum的信息
                ResearchNum researchNum = new ResearchNum();
                //设置工号
                String id = objects.get(0).toString();
                //根据工号查询，若返回的对象不为空则说明该工号已存在
                //这里不能使用getOne(id)去查询，getOne查询无结果会抛出异常
                //orElse  如果findById没有找到，则返回orElse里给定的默认值
                if (ObjectUtil.isNotNull(userRepository.findById(id).orElse(null))){
                    //直接return并携带错误信息，避免下面插入时报错
                    return new CommonResult<>(444,false,"批量添加失败，工号：[" + id + "]已存在");
                }else {
                    user.setUserId(id);
                }
                //设置密码  无论给定什么默认都应该是123456
                user.setUserPassword("123456");
                //设置姓名
                user.setUserRealName(objects.get(2).toString());
                //设置性别 传入数字或者汉字都可以解析
                String gender = objects.get(3).toString();
                if (StrUtil.equals("男", gender)
                        || StrUtil.equals("1", gender)){
                    user.setUserGender(1);
                }else if (StrUtil.equals("女", gender)
                        || StrUtil.equals("0", gender)){
                    user.setUserGender(0);
                }
                //设置生日
                LocalDate localDate = LocalDate.parse(objects.get(4).toString().substring(0, 10));
                user.setUserBirth(localDate);
                //设置编制
                user.setUserTitle((String) objects.get(5));
                //设置所在学院 接收学院的ID或者学院名两种
                String schoolId = (String) objects.get(6);
                for (School school : schools) {
                    if (StrUtil.equals(schoolId, school.getSchoolId().toString())
                            || StrUtil.equals(schoolId, school.getSchoolName())){
                        user.setSchool(school);
                    }
                }
                //设置学历
                user.setUserEducationBackground((String) objects.get(7));
                //设置状态为0
                user.setUserState(0);
                //设置录入时间为当前时间
                user.setUserCreateTime(LocalDateTime.now());
                //设置角色
                String role = objects.get(8).toString();
                if (StrUtil.equals("教师", role)
                        || StrUtil.equals("2", role)){
                    user.setUserRole(2);
                }else if (StrUtil.equals("科研管理员", role)
                        || StrUtil.equals("1", role)){
                    user.setUserRole(1);
                }
                //将封装好的user对象放入list中
                users.add(user);
                //将封装好的user对象保存进科研成果数量
                researchNum.setUser(user);
                //将封装好的researchNum对象放入list中
                researchNums.add(researchNum);
            }
            //批量保存users
            userRepository.saveAll(users);
            //批量保存researchNums
            researchNumRepository.saveAll(researchNums);
            return new CommonResult<>(200,true,"批量添加成功");
        } catch (IOException e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"批量添加失败");
        }
    }


    /**
     * 查询所有用户 不包括1001管理员
     * @param pageable 分页参数
     * @param user 前台传来的对象
     * @return 自定义分页
     */
    @GetMapping("/users")
    public Object selectAll(TablePageable pageable, User user){
        try {
            PageRequest pageRequest = pageable.bulidPageRequest();
            Page<User> users = userRepository.findAll(new Specification<T>() {
                @Override
                public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                   List<Predicate> predicates = new ArrayList<>();
                    //字符串是否为空白 空白的定义如下：
                    //1、为null
                    //2、为不可见字符（如空格）
                    //3、""
                   if (StrUtil.isNotBlank(user.getUserId())){
                       //模糊搜索工号
                       predicates.add(cb.like(root.get("userId"), "%" + user.getUserId() + "%"));
                   }
                   if (StrUtil.isNotBlank(user.getUserRealName())){
                       predicates.add(cb.like(root.get("userRealName"), "%" + user.getUserRealName() + "%"));
                   }
                   //不查询自己
                   predicates.add(cb.notEqual(root.get("userId"),1001));
                   cq.where(predicates.toArray(new Predicate[predicates.size()]));
                   return null;
                }
            },pageRequest);
            return DataGridUtil.buildResult(users);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"查询失败");
        }
    }


    /**
     * 查询所有教师
     * @param pageable 分页参数
     * @return 自定义分页
     */
    @GetMapping("/teachers")
    public Object selectAllteachers(TablePageable pageable){
        try {
            PageRequest pageRequest = pageable.bulidPageRequest();
            //2表示教师，查询所有教师
            Page<User> teachers = userRepository.findByUserRole(2, pageRequest);
            return DataGridUtil.buildResult(teachers);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"查询失败");
        }
    }

}
