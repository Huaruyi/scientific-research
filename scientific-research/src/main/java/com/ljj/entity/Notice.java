package com.ljj.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notice")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Notice implements Serializable {
    /**
     * ID
     */
    @Id
    @OrderBy
    @GeneratedValue
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 接收者
     */
    private String receiver;
    /**
     * 发布时间(时间戳）
     */
    private Long time;
}
