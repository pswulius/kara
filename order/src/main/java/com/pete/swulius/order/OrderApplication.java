package com.pete.swulius.order;

import com.pete.swulius.order.repository.OrderRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class OrderApplication {

    private OrderRepository orderService;

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
