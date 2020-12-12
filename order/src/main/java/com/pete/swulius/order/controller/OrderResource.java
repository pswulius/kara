/*
 * Copyright (C) 2017-2020 Jeff Carpenter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pete.swulius.order.controller;

import com.pete.swulius.order.model.Customer;
import com.pete.swulius.order.repository.OrderRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
@RequestMapping("/api/v1/orders")
@Api(value = "/api/v1/orders", description = "Order service rest resources")
public class OrderResource {

    private static final Logger logger = LoggerFactory.getLogger(OrderResource.class);
    private OrderRepository orderService;


    public OrderResource(OrderRepository orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(method = GET, value = "/customer", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List all customers available", response = Customer.class)
    @ApiResponse(code = 200, message = "Return all Customers")
    public ResponseEntity<List<Customer>> findAllCustomers() {

        logger.debug("Fetching all customers");
        List<Customer> customerById = orderService.findAllCustomers();
        return ResponseEntity.ok(customerById);
    }

    @RequestMapping(method = GET, value = "/customer/{customerId}", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find customer with matching id", response = Customer.class)
    @ApiParam(name="customerid", value="id for a customer", example = "4391e230-3c26-11eb-9680-3f1ca592bc5a", required=true )
    @ApiResponse(code = 200, message = "Return a Customer")
    public ResponseEntity<Customer> findCustomerById(
            @ApiParam(name="customerId",
            value="id for a customer",
            example = "4391e230-3c26-11eb-9680-3f1ca592bc5a",
            required=true )
            @PathVariable(value = "customerId") String aCustomerId ) {

        logger.debug("Fetching customer by id: " + aCustomerId);
        Optional<Customer> customerById = orderService.findCustomerById(UUID.fromString(aCustomerId));

        if( customerById.isEmpty() ) {
            logger.warn("Customer not found with id [{}]", aCustomerId);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(orderService.findCustomerById(UUID.fromString(aCustomerId)).get());
    }
}
