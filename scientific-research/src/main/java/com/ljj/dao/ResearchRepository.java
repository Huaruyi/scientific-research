package com.ljj.dao;

import com.ljj.entity.Research;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public interface ResearchRepository extends JpaRepository<Research,Long> {
    Page<Research> findAll(@Nullable Specification<T> var1, Pageable var2);
    List<Research> findAll(@Nullable Specification<T> var1);
    @Query(value = "SELECT school_school_id,COUNT(school_school_id) FROM `research` WHERE research_commit_time >= ?1 AND research_commit_time <= ?2 GROUP BY school_school_id HAVING COUNT(school_school_id)", nativeQuery = true)
    List<Object[]> halfYearAndSchool(LocalDateTime l1, LocalDateTime l2);

}
