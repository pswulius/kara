package com.pete.swulius.order.controller;

import com.pete.swulius.order.model.Customer;
import com.pete.swulius.order.model.CustomerRequest;
import com.pete.swulius.order.model.Order;
import com.pete.swulius.order.model.OrderRequest;
import com.pete.swulius.order.repository.OrderRepository;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;


@RestController
@RequestMapping("/api/v1/customer")
@Api(value = "/api/v1/customer", description = "Customer service rest resources", tags = "Customer")
public class CustomerResource {

    private static final Logger logger = LoggerFactory.getLogger(CustomerResource.class);
    private OrderRepository orderService;

    public CustomerResource(OrderRepository orderService) {
        this.orderService = orderService;
    }


    // -------------------
    // Customer Endpoints
    // -------------------
    @RequestMapping(method = GET, value = "", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List all customers available", response = Customer.class)
    @ApiResponse(code = 200, message = "Return all Customers")
    public ResponseEntity<List<Customer>> findAllCustomers() {

        logger.debug("Fetching all customers");
        List<Customer> customers = orderService.findAllCustomers();
        return ResponseEntity.ok(customers);
    }


    @RequestMapping(method = GET, value = "/{customerId}", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find customer with matching id", response = Customer.class)
    @ApiParam(name = "customerId", value = "id for a customer", example = "4391e230-3c26-11eb-9680-3f1ca592bc5a", required = true)
    @ApiResponse(code = 200, message = "Return a Customer")
    public ResponseEntity<Customer> findCustomerById(
            @ApiParam(name = "customerId",
                    value = "id for a customer",
                    example = "4391e230-3c26-11eb-9680-3f1ca592bc5a",
                    required = true)
            @PathVariable(value = "customerId") String aCustomerId) {

        logger.debug("Fetching customer by id: " + aCustomerId);
        Optional<Customer> customerById = orderService.findCustomerById(UUID.fromString(aCustomerId));

        if (customerById.isEmpty()) {
            logger.warn("Customer not found with id [{}]", aCustomerId);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(orderService.findCustomerById(UUID.fromString(aCustomerId)).get());
    }

    @RequestMapping(
            method = POST,
            value = "",
            consumes = APPLICATION_JSON_VALUE,
            produces = TEXT_PLAIN_VALUE)
    @ApiOperation(value = "Create a Customer and generate an id", response = String.class)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Customer has been created"),
            @ApiResponse(code = 400, message = "Invalid CustomerRequest provided")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "customerRequest",
                    value = "A JSON value representing a customer.",
                    required = true, dataType = "CustomerRequest", paramType = "body")
    })
    public ResponseEntity<String> createCustomer(
            HttpServletRequest request,
            @RequestBody CustomerRequest customerRequest) {
        UUID customerId = orderService.upsertCustomer(new Customer(customerRequest));
        URI location = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath("/api/v1/orders/customer/{customerId}")
                .buildAndExpand(customerId)
                .toUri();
        return ResponseEntity.created(location).body(customerId.toString());
    }


    @RequestMapping(
            method = PUT,
            value = "/{customerId}",
            produces = APPLICATION_JSON_VALUE)
    @ApiOperation(
            value = "Create or update a Customer",
            response = ResponseEntity.class)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Customer has been created"),
            @ApiResponse(code = 204, message = "No content, customer has been updated"),
            @ApiResponse(code = 400, message = "Customer id is blank or contains invalid characters (expecting alphanumeric)")
    })
    public ResponseEntity<Void> upsertCustomer(
            @ApiParam(name = "customerId",
                    example = "4391e230-3c26-11eb-9680-3f1ca592bc5a",
                    value = "Id for a customer",
                    required = true)
            @PathVariable(value = "customerId") String customerId,
            @RequestBody CustomerRequest aCustomerRequest) {
        validateId(customerId);
        logger.debug("Request to update customer {}", customerId);
        HttpStatus returnedStatus = orderService.exists(UUID.fromString(customerId)) ? HttpStatus.NO_CONTENT : HttpStatus.CREATED;
        orderService.upsertCustomer(new Customer(UUID.fromString(customerId), aCustomerRequest));
        return new ResponseEntity<>(returnedStatus);
    }


    // ---------------
    // private methods
    // ---------------
    private void validateId(String cf) {
        if (null == cf || cf.isEmpty()) {
            throw new IllegalArgumentException("id should not be null nor empty");
        }
        // Should be a valid uuid AAAAAAAA-BBBB-CCCC-DDDD-EEEEEEEEEEEE or IllegalArgumentException
        UUID.fromString(cf);
    }
}
