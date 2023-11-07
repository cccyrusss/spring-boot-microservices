package com.codingtest.orderservice.service;

import com.codingtest.orderservice.dto.OrderRequestDto;
import com.codingtest.orderservice.model.Order;
import com.codingtest.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderServiceTest {

    @Mock
    private OrderRepository mockedOrderRepository;

    @Mock
    private WebClient.Builder mockedWebClientBuilder;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getNonExistingOrderReturnsNull() {
        when(mockedOrderRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(orderService.getOrder(2));
    }

    @Test
    public void getExistingOrderReturnsOrder() {
        Order expectedOrder = createOrder(1L, Order.Status.ACCEPTED, 1);
        when(mockedOrderRepository.findById(anyLong())).thenReturn(Optional.of(expectedOrder));
        assertOrderEquals(orderService.getOrder(2), 1L, Order.Status.ACCEPTED, 1);
    }

    @Test
    public void validateNormalOrder() {
        OrderRequestDto validRequest = new OrderRequestDto();
        validRequest.setStatus(Order.Status.CREATED);
        validRequest.setAccountType(1);
        assertTrue(orderService.validate(validRequest));
    }

    @Test
    public void nullOrderStatusInvalid() {
        OrderRequestDto invalidRequest = createOrderDto(null, 1);
        assertFalse(orderService.validate(invalidRequest));
    }

    @Test
    public void accountTypeBelowZeroInvalid() {
        OrderRequestDto invalidRequest =createOrderDto(Order.Status.CREATED, -1);
        assertFalse(orderService.validate(invalidRequest));
    }

    @Test
    public void accountTypeAboveEightInvalid() {
        OrderRequestDto invalidRequest = createOrderDto(Order.Status.CREATED, 9);
        assertFalse(orderService.validate(invalidRequest));
    }

    @Test
    public void createInvalidOrderReturnsBadStatusCode() {
        OrderRequestDto invalidRequest = createOrderDto(null, -1);
        ResponseEntity<Order> res = orderService.create(invalidRequest);

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    public void createValidOrderReturnsOkStatusCode() {
        OrderRequestDto validRequest = createOrderDto(Order.Status.CREATED, 1);
        ResponseEntity<Order> res = orderService.create(validRequest);

        assertEquals(HttpStatus.CREATED, res.getStatusCode());
    }

    @Test
    public void createValidOrderReturnsAcceptedOrder() {
        OrderRequestDto validRequest = createOrderDto(Order.Status.CREATED, 1);
        ResponseEntity<Order> res = orderService.create(validRequest);

        assertEquals(Order.Status.ACCEPTED, res.getBody().getStatus());
        assertEquals(1, res.getBody().getAccountType());
    }

    @Test
    public void sendNonExistingOrderReturnsNull() {
        when(mockedOrderRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(orderService.send(1));
    }

    @Test
    public void sendExistingOrderReturnsSentOrder() {
        mockWebClientBuilder(Boolean.TRUE);

        Order expectedOrder = createOrder(3L, Order.Status.ACCEPTED, 2);
        when(mockedOrderRepository.findById(3L)).thenReturn(Optional.of(expectedOrder));

        assertOrderEquals(orderService.send(3L), 3L, Order.Status.SENT, 2);
    }

    private Order createOrder(long id, Order.Status status, int accountType) {
        Order order = new Order();
        order.setId(id);
        order.setStatus(status);
        order.setAccountType(accountType);
        return order;
    }

    private OrderRequestDto createOrderDto(Order.Status status, int accountType) {
        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setStatus(status);
        requestDto.setAccountType(accountType);
        return requestDto;
    }

    private void assertOrderEquals(Order order, long expectedId, Order.Status expectedStatus, int expectedAccountType) {
        assertEquals(expectedId, order.getId());
        assertEquals(expectedStatus, order.getStatus());
        assertEquals(expectedAccountType, order.getAccountType());
    }

    private void mockWebClientBuilder(Boolean apiReturnValue) {
        WebClient mockedWebClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec mockedUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec mockedHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec mockedResponseSpec = mock(WebClient.ResponseSpec.class);

        when(mockedWebClientBuilder.build()).thenReturn(mockedWebClient);
        when(mockedWebClient.get()).thenReturn(mockedUriSpec);
        when(mockedUriSpec.uri(anyString())).thenReturn(mockedHeadersSpec);
        when(mockedHeadersSpec.retrieve()).thenReturn(mockedResponseSpec);
        when(mockedResponseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(apiReturnValue));
    }
}
