package com.codingtest.orderservice.dto;

import com.codingtest.orderservice.model.Order;

public class OrderRequestDto {

    private Long id;
    private Order.Status status;
    private Integer accountType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order.Status getStatus() {
        return status;
    }

    public void setStatus(Order.Status status) {
        this.status = status;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
}
