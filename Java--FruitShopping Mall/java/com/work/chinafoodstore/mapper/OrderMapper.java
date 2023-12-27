package com.work.chinafoodstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.work.chinafoodstore.dommain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Order database interface
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    /**
     * Check the order against the food id
     * @param foodId
     * @return
     */
    List<Order> findOrderByFoodId(@Param("foodId") Integer foodId);

    /**
     * Look up orders by user
     * @param uid
     * @return
     */
    List<Order> findOrderByUid(@Param("uid") Integer uid);
}
