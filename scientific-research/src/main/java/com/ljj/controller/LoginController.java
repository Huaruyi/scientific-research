package com.ljj.controller;

import cn.hutool.core.util.ObjectUtil;
import com.ljj.dao.UserRepository;
import com.ljj.entity.User;
import com.ljj.util.CommonResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private final static int THREE = 3;


    /**
     * 登录
     * @param user 前台传来的user
     * @param session session
     * @return 自定义
     */
    @PostMapping("/login")
    @ResponseBody
    public CommonResult login(User user, HttpSession session){
        try {
            String userId = user.getUserId();
            String userPassword = user.getUserPassword();
            //根据工号查询用户
            User user1 = userRepository.findByUserIdAndUserPassword(userId, userPassword);
            //查到用户，如果不为null
            if (ObjectUtil.isNotNull(user1)){
                //将用户信息存入session
                session.setAttribute("user",user1);
                //判断用户状态，被删除的用户无法登录成功
                if (user1.getUserState() == TWO || user1.getUserState() == THREE){
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


    /**
     * 判断登录状态并跳转到controller页面
     * @param session session
     * @param model model
     * @return 返回controller或index页面
     */
    @GetMapping("/controller")
    public String gotoController(HttpSession session, Model model){
        Object user = session.getAttribute("user");
        if (user == null){
            model.addAttribute("msg", "请先登录");
            return "index.html";
        }else {
            return "controller";
        }

    }


}
