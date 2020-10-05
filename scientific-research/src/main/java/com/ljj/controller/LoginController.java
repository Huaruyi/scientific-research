package com.ljj.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.ljj.dao.UserRepository;
import com.ljj.entity.School;
import com.ljj.entity.User;
import com.ljj.util.CommonResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;


/**
 * 登录相关
 */
@Controller
public class LoginController {
    @Resource
    private UserRepository userRepository;

    private final static int ZERO = 0;
    private final static int ONE = 1;
    private final static int TWO = 2;

    @PostMapping("/login")
    @ResponseBody
    public CommonResult login(User user, HttpSession session){
        try {
            String userId = user.getUserId();
            String userPassword = user.getUserPassword();
            //根据工号查询用户
            User user1 = userRepository.findByUserIdAndUserPassword(userId, userPassword);
            System.out.println(user1);
            //查到用户，如果不为null
            if (ObjectUtil.isNotNull(user1)){
                //将用户信息存入session
                session.setAttribute("user",user1);
                //判断用户状态，被删除的用户无法登录成功
                if (user1.getUserState() == TWO){
                    //返回错误代码与错误信息
                    return new CommonResult<>(444,false,"你已被永久删除，无法使用该系统");
                }
                return new CommonResult<>(200,true,"验证成功！");
            //没查到用户说明账号或者密码错误
            }else {
                //返回错误代码与错误信息
                return new CommonResult<>(444,false,"账号或者密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(404,false,"系统异常");
        }
    }
    /**
     * 退出登录
     * @param session session
     * @return 返回页面
     */
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/index.html";
    }

    @GetMapping("/controller")
    public String controller(HttpSession session, Model model){
        Object user = session.getAttribute("user");
        if (user == null){
            model.addAttribute("msg", "请先登录");
            return "index.html";
        }else {
            return "controller";
        }

    }


}
