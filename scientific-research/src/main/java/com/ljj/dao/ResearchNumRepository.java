package com.ljj.dao;

import com.ljj.entity.Research;
import com.ljj.entity.ResearchNum;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ResearchNumRepository extends JpaRepository<ResearchNum,Long> {
    /*Page<T> findAll(@Nullable Specification<T> var1, Pageable var2);
    List<Research> findAll(@Nullable Specification<T> var1);*/
    ResearchNum findByUserUserId(String userId);
    Page<ResearchNum> findAll(Pageable var1);
}
