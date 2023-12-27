package com.work.chinafoodstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.work.chinafoodstore.dommain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * User database interface
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * Query user information based on the user name
     * @param userName
     * @return
     */
    User queryUserByUserName(@Param("username") String userName);
}
