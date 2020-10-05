package com.ljj.dao;

import com.ljj.entity.School;
import com.ljj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School,Integer> {

}
