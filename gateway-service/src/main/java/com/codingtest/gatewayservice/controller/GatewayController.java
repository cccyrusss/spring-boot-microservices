package com.codingtest.gatewayservice.controller;

import com.codingtest.gatewayservice.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {

    @Autowired
    GatewayService gatewayService;

    // Use get request, and return boolean for simplicity
    @GetMapping(value = "/gateway", produces = "application/json")
    public boolean acknowledgeOrder(@RequestParam(name="orderId") long orderId) throws Exception {
        return gatewayService.acknowledge(orderId);
    }
}
