package com.work.chinafoodstore.web.controller;

import com.work.chinafoodstore.service.BasketService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;


/**
 * Basket controller
 */
@Controller
public class BasketController {
    @Resource
    private BasketService basketService;

    @GetMapping("/addBasket")
    public String addBasket(@RequestParam("foodId") Integer foodId, @RequestParam("uid") Integer uid) {
        if (uid == null) {
            return "fruits";
        }
        basketService.addBasket(foodId, uid);
        return "fruits";
    }

    @GetMapping(value = {"/basket"})
    public String basket() {
//        List<FoodDto> foodDtos = basketService.queryBasketByUid(uid);
//        model.addAttribute("foodDtos", foodDtos);
        return "basket";
    }



}
