package com.codingtest.orderservice.service;

import com.codingtest.orderservice.dto.OrderRequestDto;
import com.codingtest.orderservice.model.Order;
import com.codingtest.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    WebClient.Builder webClientBuilder;

    // creates and saves the order and send the order, return http response
    public ResponseEntity<Order> create(OrderRequestDto request) {
        if (!validate(request)) {
            return ResponseEntity.badRequest().build();
        }
        Order order = convertToOrder(request);
        save(order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    // sends out the order through gateway service
    public Order send(long orderId) {
        Order order = getOrder(orderId);
        if (order == null) {
            System.out.println("Cannot find order " + orderId + " , cannot send order");
            return null;
        }
        if (forwardToGateway(orderId)) {
            order.setStatus(Order.Status.SENT);
            orderRepository.save(order);
        }
        return order;
    }

    public Order getOrder(long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        return order;
    }

    // Saves and change the order status
    private void save(Order order) {
        order.setStatus(Order.Status.ACCEPTED);
        orderRepository.save(order);
    }

    // Simple validation which validates the status and the account type, 1-8 is considered valid, in the future, can validate MsgType, Account, Currency etc.
    // Returns true if succeeded, false if failed
    protected boolean validate(OrderRequestDto request) {
        if (request.getStatus() == null) {
            return false;
        }
        return request.getAccountType() != null && request.getAccountType() >= 0 && request.getAccountType() <= 8;
    }

    private boolean forwardToGateway(long orderId) {
        // gateway service is on localhost:8081
        Boolean result = webClientBuilder.build().get().uri("http://localhost:8081/gateway?orderId=" + orderId).retrieve().bodyToMono(Boolean.class).block();
        if (result == null) {
            return false;
        }
        return result;
    }

    private Order convertToOrder(OrderRequestDto request) {
        Order order = new Order();
        order.setStatus(request.getStatus());
        order.setAccountType(request.getAccountType());
        return order;
    }
}
