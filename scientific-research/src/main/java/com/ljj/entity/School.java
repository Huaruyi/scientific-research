package com.ljj.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "school")
public class School implements Serializable {

    /**
     * ID
     */
    @Id
    @GeneratedValue
    @OrderBy
    private Integer schoolId;
    /**
     * 学院名称
     */
    private String schoolName;
}
