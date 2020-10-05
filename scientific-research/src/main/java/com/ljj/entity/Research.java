package com.ljj.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "research")
public class Research implements Serializable {
    /**
     * ID
     */
    @Id
    @OrderBy
    @GeneratedValue
    private Long researchId;
    /**
     * 存储路径
     */
    private String researchPath;
    /**
     * 成果类型：论文、专利、课题
     */
    private String researchType;
    /**
     * 提交时间
     * JsonFormat：数据库取到时间传回前端的格式
     *      pattern 格式
     *      timezone 时区
     * DateTimeFormat： 前端接收后转化为什么格式存到数据库
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime researchCommitTime;
    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime researchAuditTime;
    /**
     * 状态
     */
    private Integer researchState;
    /**
     * 成果作者
     */
    @ManyToOne
    private User user;
    /**
     * 所属院系
     */
    @ManyToOne
    private School school;

}
