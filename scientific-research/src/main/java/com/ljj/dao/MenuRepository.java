package com.ljj.dao;


import com.ljj.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu,Integer> {
    List<Menu> findByParentId(Integer parentId);
    List<Menu> findByParentIdAndAuthorityLike(Integer parentId, String authority);
    Menu findByTitleAndParentId(String title, Integer parentId);
}
