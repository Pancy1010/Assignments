package com.work.chinafoodstore.web.controller;

import com.work.chinafoodstore.service.FoodService;
import com.work.chinafoodstore.web.dto.FoodDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Home page
 */
@Controller
public class IndexController {

    @Resource
    private FoodService foodService;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        Map<String, List<FoodDto>> top = foodService.top();
        model.addAttribute("top", top);
        return "index";
    }


}
