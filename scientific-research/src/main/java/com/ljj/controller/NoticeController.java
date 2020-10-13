package com.ljj.controller;

import cn.hutool.core.util.StrUtil;
import com.ljj.dao.NoticeRepository;
import com.ljj.entity.Notice;
import com.ljj.util.CommonResult;
import com.ljj.util.DataGridUtil;
import com.ljj.util.TablePageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@Slf4j
public class NoticeController {
    @Resource
    private NoticeRepository noticeRepository;


    /**
     * 发公告
     * @param notice 前台传来的对象
     * @return 反馈
     */
    @PostMapping("/notice")
    public CommonResult save(Notice notice){
        try {
            //LocalDataTime转换时间戳
            notice.setTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
            noticeRepository.save(notice);
            return new CommonResult<>(200,true,"发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"发送失败");
        }
    }


    /**
     * 查询所有消息列表
     * @param pageable 分页参数
     * @return 分页结果
     */
    @GetMapping("/notices")
    public Object selectAll(TablePageable pageable){
        try {
            PageRequest pageRequest = pageable.bulidPageRequest();
            Page<Notice> notices = noticeRepository.findAll(pageRequest);
            return DataGridUtil.buildResult(notices);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"查询失败");
        }
    }


    /**
     * 按工号查询消息列表
     * @param pageable 分页参数
     * @param userId id
     * @param userId1 id
     * @return 分页结果
     */
    @GetMapping("/notice")
    public Object selectById(TablePageable pageable, String userId, String userId1){
        try {
            PageRequest pageRequest = pageable.bulidPageRequest();
            Page<Notice> notices;
            if (StrUtil.isNotBlank(userId1) && StrUtil.isNotBlank(userId)){
                //0表示所有，管理员发送消息可以是给个人和全体，因此0代表全体，userId代表自己，需要这两个条件
                notices = noticeRepository.findByReceiverOrReceiver(userId, userId1, pageRequest);
            }else {
                //只查全体或者个人的
                notices = noticeRepository.findByReceiver(userId, pageRequest);
            }
            return DataGridUtil.buildResult(notices);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"查询失败");
        }
    }


    /**
     * 公告详情
     * @param id id
     * @return 详情
     */
    @RequestMapping("/notice/detail/{id}")
    public CommonResult gotoDetail(@PathVariable Long id){
        try {
            Notice notice = noticeRepository.getOne(id);
            return new CommonResult<>(0,true,"",notice);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<>(444,false,"查询失败");
        }
    }

}
