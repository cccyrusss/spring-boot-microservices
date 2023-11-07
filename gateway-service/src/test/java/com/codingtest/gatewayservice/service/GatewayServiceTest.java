package com.codingtest.gatewayservice.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GatewayServiceTest {
    @Test
    public void acknowledgeAlwaysWithin5Seconds() {
        GatewayService gatewayService = new GatewayService();
        // Can use multithreading in the future
        for (int i = 0; i < 10; ++i) {
            long start = System.currentTimeMillis();
            gatewayService.acknowledge(1);
            assertTrue(System.currentTimeMillis() - start <= 5000);
        }
    }

    @Test
    public void acknowledgeReturnsTrueWhenNoException() {
        GatewayService gatewayService = new GatewayService();
        assertTrue(gatewayService.acknowledge(1));
    }
}
