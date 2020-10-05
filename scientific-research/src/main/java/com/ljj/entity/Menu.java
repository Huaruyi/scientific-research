package com.ljj.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "menu")
public class Menu implements Serializable {
    /**
     * ID
     */
    @Id
    @GeneratedValue
    private Integer menuId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 父ID
     */
    private Integer menuParentId;
}
