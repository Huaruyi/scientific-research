package com.ljj.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user")
public class User implements Serializable {
    /**
     * 工号
     */
    @Id
    @OrderBy
    private String userId;
    /**
     * 密码
     */
    private String userPassword;
    /**
     * 姓名
     */
    private String userRealName;
    /**
     * 性别
     */
    private Integer userGender;
    /**
     * 生日（年龄）
     */
    private LocalDate userBirth;
    /**
     * 编制
     */
    private String userTitle;
    /**
     * 所属学院（部门）编号
     */
    @ManyToOne
    private School school;
    /**
     * 学历
     */
    private String userEducationBackground;

    /**
     * 状态
     */
    private Integer userState;
    /**
     * 录入时间
     */
    private LocalDateTime userCreateTime;
    /**
     * 最近修改时间
     */
    private LocalDateTime userLastUpdateTime;
    /**
     * 角色
     */
    private Integer userRole;
}
