package com.telusko.springecom.controller;

import com.telusko.springecom.model.Order;
import com.telusko.springecom.model.dto.OrderRequest;
import com.telusko.springecom.model.dto.OrderResponse;
import com.telusko.springecom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @PostMapping("/orders")
    public ResponseEntity<?> placeOrder(@RequestBody Order order) {
        // Your logic to save the order to the database goes here
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orderResponseList = orderService.getAllOrderResponses();
        return new ResponseEntity<>(orderResponseList, HttpStatus.OK);
    }

    // ✦ NEW: Endpoint to delete all orders ✦
    @DeleteMapping("/orders")
    public ResponseEntity<String> deleteAllOrders() {
        try {
            orderService.deleteAllOrders();
            return new ResponseEntity<>("All orders have been successfully deleted.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete orders.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}