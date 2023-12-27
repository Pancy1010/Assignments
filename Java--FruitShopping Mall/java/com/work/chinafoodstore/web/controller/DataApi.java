package com.work.chinafoodstore.web.controller;

import com.work.chinafoodstore.common.HttpRsp;
import com.work.chinafoodstore.dommain.User;
import com.work.chinafoodstore.service.BasketService;
import com.work.chinafoodstore.service.OrderService;
import com.work.chinafoodstore.service.UserService;
import com.work.chinafoodstore.web.dto.FoodDto;
import com.work.chinafoodstore.web.dto.UserDto;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Data acquisition api
 */
@RestController
public class DataApi {

    @Resource
    private UserService userService;

    @Resource
    private BasketService basketService;

    @Resource
    private OrderService orderService;
    @PostMapping(value = "/user/login")
    public HttpRsp userLogin(@RequestBody UserDto userDto, HttpServletResponse rsp) {
        User user = userService.findUserByUserName(userDto.getUserName());
        if (user != null) {
            rsp.addHeader("Access-Control-Expose-Headers", "token");
            rsp.addHeader("token", user.getId() + "");
        } else {
            throw new RuntimeException("User does not exist");
        }
        System.out.println("===========Login success:"+userDto.getUserName());
        return HttpRsp.successRsp(user.getId());
    }

    @GetMapping("/userInfo")
    public HttpRsp getUserInfo(@RequestParam("uid") Integer uid){
        User user = userService.findUserByUid(uid);
        return HttpRsp.successRsp(user);
    }

    @GetMapping("/queryBasketBuyUid")
    public HttpRsp findBasket(@RequestParam("uid") Integer uid){
        List<FoodDto> foodDtos = basketService.queryBasketByUid(uid);
        return HttpRsp.successRsp(foodDtos);
    }

    @GetMapping("/queryOrderByUid")
    public HttpRsp findOrder(@RequestParam("uid") Integer uid){

        return HttpRsp.successRsp(orderService.queryOrderByUid(uid));
    }
}
