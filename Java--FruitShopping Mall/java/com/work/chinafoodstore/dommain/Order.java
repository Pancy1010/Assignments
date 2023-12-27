package com.work.chinafoodstore.dommain;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * Order
 */
@TableName(value = "food_order")
public class Order {

    private Integer id;

    private Integer userId;

    private Integer foodId;

    private Date createdTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
