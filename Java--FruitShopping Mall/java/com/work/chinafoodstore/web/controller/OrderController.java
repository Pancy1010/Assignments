package com.work.chinafoodstore.web.controller;

import com.work.chinafoodstore.dommain.Food;
import com.work.chinafoodstore.service.FoodService;
import com.work.chinafoodstore.service.OrderService;
import com.work.chinafoodstore.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * Order controller
 */
@Controller
public class OrderController {

    @Resource
    private FoodService foodService;

    @Resource
    private UserService userService;
    @Resource
    private OrderService orderService;

    @GetMapping("/pay")
    public String pay(@RequestParam("id") Integer id, Model model) {
        Food food = foodService.findFoodById(id);
        model.addAttribute("food", food);
        return "pay";
    }

    @GetMapping("/order/save")
    public String saveOrder(@RequestParam("foodId") Integer foodId, @RequestParam("uid") Integer uid) {
        if (uid == null) {
            return "fruits";
        }
        orderService.saveOrder(foodId, uid);
        return "fruits";
    }

    @GetMapping("/order")
    public String order() {
        return "order";
    }
}
