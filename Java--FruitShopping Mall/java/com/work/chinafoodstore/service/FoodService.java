package com.work.chinafoodstore.service;

import com.work.chinafoodstore.dommain.Comment;
import com.work.chinafoodstore.dommain.Food;
import com.work.chinafoodstore.dommain.Order;
import com.work.chinafoodstore.mapper.CommentMapper;
import com.work.chinafoodstore.mapper.FoodMapper;
import com.work.chinafoodstore.mapper.OrderMapper;
import com.work.chinafoodstore.web.dto.FoodDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Logical processing of food
 */
@Service
public class FoodService {

    @Resource
    private FoodMapper foodMapper;
    private ModelMapper modelMapper = new ModelMapper();

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderService orderService;
    @Resource
    private CommentMapper commentMapper;

    private static final String HOT_SALE = "hotSale";
    private static final String HOT_RATE = "hotRate";

    /**
     * Query all goods
     *
     * @return
     */
    public List<FoodDto> findAllFood() {
        List<Food> foods = foodMapper.selectAll();
        List<FoodDto> foodDtos = new ArrayList<>();
        foods.forEach(food -> {
            FoodDto foodDto = modelMapper.map(food, FoodDto.class);
            List<Order> orderList = orderService.queryOrderByFoodId(food.getId());
            foodDto.setCount(orderList.size());
            List<Comment> commentByFoodId = commentMapper.findCommentByFoodId(food.getId());
            foodDto.setCommentCount(commentByFoodId.size());
            foodDto.setRate(calcRate(commentByFoodId));
            foodDtos.add(foodDto);
        });
        return foodDtos;
    }

    /**
     * Calculate the overall score of the product
     *
     * @return
     */
    private BigDecimal calcRate(List<Comment> commentList) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Comment comment : commentList) {
            sum = sum.add(BigDecimal.valueOf(comment.getRate()));
        }
        return sum.equals(BigDecimal.ZERO) ? sum : sum.divide(BigDecimal.valueOf(commentList.size()), 2, RoundingMode.HALF_UP);
    }

    /**
     * Query products by id
     *
     * @param id
     * @return
     */
    public Food findFoodById(Integer id) {
        return foodMapper.selectById(id);
    }

    /**
     * top data of home page
     *
     * @return
     */
    public Map<String, List<FoodDto>> top() {
        Map<String, List<FoodDto>> foodMaps = new HashMap<>();
        List<FoodDto> allFood = findAllFood();
        allFood.sort(new Comparator<FoodDto>() {
            @Override
            public int compare(FoodDto o1, FoodDto o2) {
                return o2.getCount() - o1.getCount();
            }
        });
        foodMaps.put(HOT_SALE, allFood);
        List<FoodDto> tempFood = new ArrayList<>(allFood);
        tempFood.sort(new Comparator<FoodDto>() {
            @Override
            public int compare(FoodDto o1, FoodDto o2) {
                return o2.getRate().compareTo(o1.getRate());
            }
        });
        foodMaps.put(HOT_RATE, tempFood);
        return foodMaps;
    }
}
