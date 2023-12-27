package com.work.chinafoodstore.web.controller;

import com.work.chinafoodstore.service.*;
import com.work.chinafoodstore.web.dto.CommentDto;
import com.work.chinafoodstore.web.dto.FoodDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Food controller
 */
@Controller
public class FoodController {

    @Resource
    private FoodService foodService;



    @Resource
    private CommentService commentService;



    @Resource
    private UserService userService;

    @GetMapping("/fruits")
    public String fruits(Model model) {
        List<FoodDto> foods = foodService.findAllFood();
        List<List<FoodDto>> allFood = new ArrayList<>();
        for (int i = 0; i < foods.size(); i += 4) {
            int endIndex = Math.min(i + 4, foods.size());
            List<FoodDto> row = foods.subList(i, endIndex);
            allFood.add(row);
        }
        model.addAttribute("allFood", allFood);
        return "fruits";
    }


    @GetMapping("/sort")
    public String addBasket(@RequestParam("type") String type, Model model) {
        List<FoodDto> foods = foodService.findAllFood();
        if ("BUYCOUNT".equals(type)) { //Sales ranking
            foods.sort(Comparator.comparing(FoodDto::getCount).reversed());
        } else if ("COMMENTCOUNT".equals(type)) { //feedback
            foods.sort(Comparator.comparing(FoodDto::getCommentCount).reversed());
        } else { //Price ranking
            foods.sort(Comparator.comparing(FoodDto::getFoodPrice));
        }
        List<List<FoodDto>> allFood = new ArrayList<>();
        for (int i = 0; i < foods.size(); i += 4) {
            int endIndex = Math.min(i + 4, foods.size());
            List<FoodDto> row = foods.subList(i, endIndex);
            allFood.add(row);
        }
        model.addAttribute("allFood", allFood);
        return "fruits";
    }

    @PostMapping("/save/comment")
    public String commentSubmit(Model model, @RequestBody CommentDto commentDto) {
        commentService.saveComment(commentDto);
        List<FoodDto> foods = foodService.findAllFood();
        List<List<FoodDto>> allFood = new ArrayList<>();
        for (int i = 0; i < foods.size(); i += 4) {
            int endIndex = Math.min(i + 4, foods.size());
            List<FoodDto> row = foods.subList(i, endIndex);
            allFood.add(row);
        }
        model.addAttribute("allFood", allFood);
        return "fruits";
    }
}
