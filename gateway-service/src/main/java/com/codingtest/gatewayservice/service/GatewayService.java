package com.codingtest.gatewayservice.service;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class GatewayService {

    private final int MAX_SLEEP_MILLIS = 5000;

    // Can make use of order id in the future
    public boolean acknowledge(long orderId) {
        System.out.println("Acknowledging order " + orderId);
        Random random = new Random();
        int sleepMillis = random.nextInt(MAX_SLEEP_MILLIS);
        try {
//            TimeUnit.MILLISECONDS.sleep(sleepMillis);
            Thread.sleep(sleepMillis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return false;
        }
        System.out.println("Acknowledged order " + orderId + " after " + sleepMillis + " millisecs");
        return true;
    }
}
