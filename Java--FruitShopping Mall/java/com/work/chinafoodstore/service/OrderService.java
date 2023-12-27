package com.work.chinafoodstore.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.work.chinafoodstore.dommain.Food;
import com.work.chinafoodstore.dommain.Order;
import com.work.chinafoodstore.mapper.OrderMapper;
import com.work.chinafoodstore.web.dto.FoodDto;
import com.work.chinafoodstore.web.dto.OrderDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Order logic processing
 */
@Service
public class OrderService {
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserService userService;

    @Resource
    private FoodService foodService;

    /**
     * Save item order information
     *
     * @param foodId
     * @param userId
     * @return
     */
    public int saveOrder(Integer foodId, Integer userId) {
        Order order = new Order();
        order.setFoodId(foodId);
        order.setUserId(userId);
        order.setCreatedTime(new Date());
        return orderMapper.insert(order);
    }

    /**
     * Check the order by the name of the fruit
     * @param foodId
     * @return
     */
    public List<Order> queryOrderByFoodId(Integer foodId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("food_id", foodId);
        return orderMapper.selectList(wrapper);
    }

    /**
     * Query all orders by user
     *
     * @param uid
     * @return
     */
    public List<OrderDto> queryOrderByUid(Integer uid) {
        List<Order> orderList = orderMapper.findOrderByUid(uid);
        List<OrderDto> orderDtos = new ArrayList<>();
        if (!orderList.isEmpty()) {
            OrderDto orderDto = new OrderDto();
            Order order = orderList.get(0);
            orderDto.setOrderId(order.getId());
            orderDto.setCreatedTime(fmtDate(order.getCreatedTime()));
            orderDto.setUsername(userService.findUserByUid(uid).getUserName());
            Food food = foodService.findFoodById(order.getFoodId());
            FoodDto foodDto=new FoodDto();
            foodDto.setDesc(food.getDesc());
            orderDto.setCount(1);
            orderDto.setFoodDto(foodDto);
            orderDto.setAmount(food.getFoodPrice());
            orderDtos.add(orderDto);
            return orderDtos;
        } else {
            return orderDtos;
        }
    }

    /**
     * Formatting time
     *
     * @param date
     * @return
     */
    private String fmtDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
