package com.work.chinafoodstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.work.chinafoodstore.dommain.Food;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
 * Food database interface
 */
@Mapper
public interface FoodMapper extends BaseMapper<Food> {

    /**
     * Query all foods
     * @return
     */
    List<Food> selectAll();
}
