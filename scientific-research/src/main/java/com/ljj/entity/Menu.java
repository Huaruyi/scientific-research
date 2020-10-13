package com.ljj.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "menu")
public class Menu implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue
    private Integer id;
    /**
     * 菜单名称
     */
    private String title;
    /**
     * 父ID
     */
    private Integer parentId;
    /**
     * 父名称
     */
    private String parentName;
    /**
     * url
     */
    private String url;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 子菜单
     */
    @OneToMany
    private List<Menu> children;
    /**
     * 权限
     */
    private String authority;



}
