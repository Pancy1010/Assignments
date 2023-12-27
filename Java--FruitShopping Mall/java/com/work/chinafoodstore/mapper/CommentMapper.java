package com.work.chinafoodstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.work.chinafoodstore.dommain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Comment database interface
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * Check reviews by food id
     * @param foodId
     * @return
     */
    List<Comment> findCommentByFoodId(@Param("foodId") Integer foodId);
}
