package com.work.chinafoodstore.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.work.chinafoodstore.dommain.User;
import com.work.chinafoodstore.mapper.UserMapper;
import com.work.chinafoodstore.web.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * User login and registration services
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    private ModelMapper modelMapper = new ModelMapper();

    /**
     * Create a user
     *
     * @param userDto
     * @return
     */
    public UserDto saveUser(UserDto userDto) {
        userDto.setCreatedTime(new Date());
        User user = modelMapper.map(userDto, User.class);
        userMapper.insert(user);
        userDto.setId(user.getId());
        return userDto;
    }

    /**
     * Finds whether the user exists
     *
     * @param userDto
     * @return
     */
    public boolean findUser(UserDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userDto.getUserName());
        queryWrapper.eq("password", userDto.getPassword());
        return userMapper.exists(queryWrapper);
    }

    /**
     * Query user information based on the user name
     * @param userName
     * @return
     */
    public User findUserByUserName(String userName){
        return userMapper.queryUserByUserName(userName);
    }

    /**
     * Query user information based on the id
     * @param uid
     * @return
     */
    public User findUserByUid(Integer uid){
        return userMapper.selectById(uid);
    }
}
