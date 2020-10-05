package com.ljj.entity.vo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ResearchNumVO implements Serializable {


    private String userId;

    private String userRealName;

    private String userGender;

    private LocalDate userBirth;

    private String userTitle;

    private String schoolName;

    private String userEducationBackground;

    private LocalDateTime userCreateTime;

    private Integer patentNum;

    private Integer paperNum;

    private Integer taskNum;

}
