package com.ljj.dao;

import com.ljj.entity.Notice;
import com.ljj.entity.Research;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NoticeRepository extends JpaRepository<Notice,Long> {
    Page<Notice> findByReceiverOrReceiver(String receiver,String receiver1, Pageable var1);
    Page<Notice> findByReceiver(String receiver, Pageable var1);
}
