package com.ljj.dao;

import com.ljj.entity.User;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface UserRepository extends JpaRepository<User,String> {
    User findByUserIdAndUserPassword(String userId, String userPassword);

    Page<User> findByUserIdNot(String userId, Pageable var1);

    Page<User> findByUserRole(Integer userRole, Pageable var1);

    Page<User> findAll(@Nullable Specification<T> var1, Pageable var2);


}
