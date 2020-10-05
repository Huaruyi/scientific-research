package com.ljj.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "researchNum")
public class ResearchNum implements Serializable {
    /**
     * ID
     */
    @Id
    @OrderBy
    @GeneratedValue
    private Long researchNumId;
    /**
     * 专利数
     */
    private Integer patentNum = 0;
    /**
     * 论文数
     */
    private Integer paperNum = 0;
    /**
     * 课题数
     */
    private Integer taskNum = 0;
    /**
     * 作者
     */
    @OneToOne
    private User user;

}
