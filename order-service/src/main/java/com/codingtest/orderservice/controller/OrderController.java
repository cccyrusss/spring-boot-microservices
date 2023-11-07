package com.codingtest.orderservice.controller;

import com.codingtest.orderservice.dto.OrderRequestDto;
import com.codingtest.orderservice.model.Order;
import com.codingtest.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    // Can separate create and send in the future
    @PostMapping(value = "/order", produces = "application/json")
    public ResponseEntity<?> createOrSend(@RequestBody OrderRequestDto request) throws Exception {
        if (request.getId() == null) {
            // create order
            return orderService.create(request);
        }

        // send order if order id is present
        Order order = orderService.send(request.getId());
        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/order", produces = "application/json")
    public ResponseEntity<?> getOrder(@RequestParam(name="id") long id) throws Exception {
        Order order = orderService.getOrder(id);
        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }
}
