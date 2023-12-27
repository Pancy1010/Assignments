package com.work.chinafoodstore.service;

import com.work.chinafoodstore.dommain.Food;
import com.work.chinafoodstore.dommain.ShoppingBasket;
import com.work.chinafoodstore.mapper.ShoppingBasketMapper;
import com.work.chinafoodstore.web.dto.FoodDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Basket logic processing
 */
@Service
public class BasketService {
    @Resource
    private ShoppingBasketMapper basketMapper;

    private ModelMapper modelMapper = new ModelMapper();
    @Resource
    private FoodService foodService;

    /**
     * Add basket
     *
     * @param foodId
     */
    public void addBasket(Integer foodId, Integer userId) {
        ShoppingBasket basket = new ShoppingBasket();
        basket.setFoodId(foodId);
        basket.setUserId(userId);
        basketMapper.insert(basket);
    }

    /**
     * Queries the items in the user's basket
     *
     * @param uid
     * @return
     */
    public List<FoodDto> queryBasketByUid(Integer uid) {
        List<ShoppingBasket> shoppingBaskets = basketMapper.queryBasketByUid(uid);
        List<FoodDto> foodDtos = new ArrayList<>();
        for (ShoppingBasket basket : shoppingBaskets) {
            Food food = foodService.findFoodById(basket.getFoodId());
            FoodDto foodDto = modelMapper.map(food, FoodDto.class);
            foodDtos.add(foodDto);
        }
        return foodDtos;
    }
}
