package com.work.chinafoodstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.work.chinafoodstore.dommain.ShoppingBasket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Basket database interface
 */
@Mapper
public interface ShoppingBasketMapper extends BaseMapper<ShoppingBasket> {

    /**
     * Look up basket information according to the user
     * @param uid
     * @return
     */
    List<ShoppingBasket> queryBasketByUid(@Param("uid") Integer uid);
}
