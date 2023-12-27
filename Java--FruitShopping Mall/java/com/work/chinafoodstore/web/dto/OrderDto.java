package com.work.chinafoodstore.web.dto;

import java.math.BigDecimal;

/**
 * Order information
 */
public class OrderDto {

    private Integer orderId;
 
    private String createdTime;

    private String username;

    private BigDecimal amount;

    private Integer count;

    private String productName;

    private FoodDto foodDto;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public FoodDto getFoodDto() {
        return foodDto;
    }

    public void setFoodDto(FoodDto foodDto) {
        this.foodDto = foodDto;
    }
}
