package com.ljj.controller;

import cn.hutool.core.util.ObjectUtil;
import com.ljj.dao.MenuRepository;
import com.ljj.entity.Menu;
import com.ljj.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@Slf4j
public class MenuController {
    @Resource
    private MenuRepository menuRepository;


    /**
     * 全部菜单
     * @return 返回菜单列表
     */
    @GetMapping("/menus")
    public CommonResult selectAll(){
        try {
            //一级菜单的pid=0
            List<Menu> menus = menuRepository.findByParentId(0);
            for (Menu menu : menus) {
                //遍历每个一级父菜单的子菜单存入Children
                List<Menu> menuList = menuRepository.findByParentId(menu.getId());
                menu.setChildren(menuList);
            }
            return new CommonResult<>(200,true,"",menus);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"查询失败");
        }
    }


    /**
     * 教师菜单
     * @return 返回教师的菜单列表
     */
    @GetMapping("/menus/teacher")
    public CommonResult select4Teacher(){
        try {
            //一级菜单的pid=0
            /*
             * 后台数据库菜单是拼串的，因此查询时使用%var%形式进行模糊搜素
             * %2%代表查教师
             * %1%代表查科研管理员
             * %0%代表查系统管理员
             */
            List<Menu> menus = menuRepository.findByParentIdAndAuthorityLike(0,"%2%");
            for (Menu menu : menus) {
                //遍历每个一级父菜单的子菜单存入Children
                List<Menu> menuList = menuRepository.findByParentIdAndAuthorityLike(menu.getId(), "%2%");
                menu.setChildren(menuList);
            }
            return new CommonResult<>(200,true,"",menus);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"查询失败");
        }
    }


    /**
     * 科研管理员菜单
     * @return 返回科研管理员菜单列表
     */
    @GetMapping("/menus/researchAdmin")
    public CommonResult select4researchAdmin(){
        try {
            //一级菜单的pid=0
            List<Menu> menus = menuRepository.findByParentIdAndAuthorityLike(0,"%1%");
            for (Menu menu : menus) {
                //遍历每个一级父菜单的子菜单存入Children
                List<Menu> menuList = menuRepository.findByParentIdAndAuthorityLike(menu.getId(), "%1%");
                menu.setChildren(menuList);
            }
            return new CommonResult<>(200,true,"",menus);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"查询失败");
        }
    }


    /**
     * 系统管理员菜单
     * @return 返回系统管理员菜单列表
     */
    @GetMapping("/menus/root")
    public CommonResult select4Root(){
        try {
            //一级菜单的pid=0
            List<Menu> menus = menuRepository.findByParentIdAndAuthorityLike(0,"%0%");
            for (Menu menu : menus) {
                //遍历每个一级父菜单的子菜单存入Children
                List<Menu> menuList = menuRepository.findByParentIdAndAuthorityLike(menu.getId(), "%0%");
                menu.setChildren(menuList);
            }
            return new CommonResult<>(200,true,"",menus);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"查询失败");
        }
    }

    /**
     * 编辑菜单的回显
     * @return 返回一个菜单的详细信息
     */
    @GetMapping("/menu")
    public CommonResult selectOne(Menu menu){
        try {
            //一级菜单parentId修正为0
            if (menu.getParentId() == -1){
                menu.setParentId(0);
            }
            //查询一个菜单
            Menu menu1 = menuRepository.findByTitleAndParentId(menu.getTitle(), menu.getParentId());
            return new CommonResult<>(200,true,"",menu1);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"查询失败");
        }
    }


    /**
     * 更新菜单
     * @return 返回一个菜单的详细信息
     */
    @PutMapping("/menu")
    public CommonResult update(Menu menu, String newTitle){
        try {
            //一级菜单parentId修正为0
            if (menu.getParentId() == -1){
                menu.setParentId(0);
            }
            //查询一个菜单  title和parentId
            Menu menu1 = menuRepository.findByTitleAndParentId(menu.getTitle(), menu.getParentId());
            //替换为新菜单名
            menu1.setTitle(newTitle);
            //更新页面地址
            menu1.setUrl(menu.getUrl());
            //更新权限
            menu1.setAuthority(menu.getAuthority());
            //id不为空 更新
            menuRepository.save(menu1);
            return new CommonResult<>(200,true,"");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"更新失败");
        }
    }


    /**
     * 新增菜单
     * @param menu 前台传来的数据
     * @return 自定义
     */
    @PostMapping("/menu")
    public CommonResult add(Menu menu){
        try {
            //一级菜单parentId修正为0
            if (menu.getParentId() == -1){
                menu.setParentId(0);
            }
            //查询一个菜单  title和parentId
            Menu menu1 = menuRepository.findByTitleAndParentId(menu.getTitle(), menu.getParentId());
            if (ObjectUtil.isNotNull(menu1)){
                return new CommonResult<>(444,false,"菜单["+menu.getTitle()+"]已存在，请勿重复添加！");
            }
            //id为空 新增
            menuRepository.save(menu);
            return new CommonResult<>(200,true,"");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"新增失败");
        }
    }


    /**
     * 删除菜单
     * @param menu 前台传来的数据
     * @return 自定义
     */
    @DeleteMapping("/menu")
    public CommonResult delete(Menu menu){
        try {
            //一级菜单parentId修正为0
            if (menu.getParentId() == -1){
                menu.setParentId(0);
            }
            //查询一个菜单  title和parentId
            Menu menu1 = menuRepository.findByTitleAndParentId(menu.getTitle(), menu.getParentId());
            menuRepository.deleteById(menu1.getId());
            return new CommonResult<>(200,true,"");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"删除失败");
        }
    }
}
