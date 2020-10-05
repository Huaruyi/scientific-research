package com.ljj.util;


import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

/**
 * 用于JPA分页，也可以通过形参注解直接给出
 */
@Data
public class TablePageable{
    /**
     * 每页几个
     */
    private Integer limit;

    /**
     * 排序字段
     */
    private String sort;

    /**
     * 顺序：顺序，逆序
     */
    private String order;

    /**
     * 页数
     */
    private Integer page;


    public PageRequest bulidPageRequest() {
        int page=(this.page!=null)?this.page-1:0;
        int size=limit!=null?limit:10;
        if(sort==null) {
            return PageRequest.of(page, size);
        }else {
            Order order2=new Order(Direction.fromString(order), sort);
            Sort sort2= Sort.by(order2);
            return PageRequest.of(page,size,sort2 );
        }

    }

    public PageRequest bulidPageable(Sort sort) {
        int page=(this.page!=null)?this.page-1:0;
        int size=limit!=null?limit:10;
        return PageRequest.of(page, size, sort);
    }

    public Sort bulidSort() {
        Order order2=new Order(Direction.fromString(order), sort);
        Sort sort2= Sort.by(order2);
        return sort2;
    }
}