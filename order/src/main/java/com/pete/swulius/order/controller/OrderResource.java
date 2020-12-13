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
@RequestMapping("/api/v1/order")
@Api(value = "/api/v1/order", description = "Order service rest resources", tags = "Order" )
public class OrderResource {

    private static final Logger logger = LoggerFactory.getLogger(OrderResource.class);
    private OrderRepository orderService;


    public OrderResource(OrderRepository orderService) {
        this.orderService = orderService;
    }



    // -----------------
    // Order Endpoints
    // -----------------
    @RequestMapping(method = GET, value = "", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List all orders available", response = Order.class)
    @ApiResponse(code = 200, message = "Return all Orders")
    public ResponseEntity<List<Order>> findAllOrders() {

        logger.debug("Fetching all orders");
        List<Order> orders = orderService.findAllOrders();
        return ResponseEntity.ok(orders);
    }

    @RequestMapping(method = GET, value = "/{orderId}", produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find order with matching id", response = Order.class)
    @ApiParam(name="orderId", value="id for an order", example = "4391e230-3c26-11eb-9680-3f1ca592bc5a", required=true )
    @ApiResponse(code = 200, message = "Return an Order")
    public ResponseEntity<Order> findOrderById(
            @ApiParam(name="orderId",
                    value="id for an order",
                    example = "4391e230-3c26-11eb-9680-3f1ca592bc5a",
                    required=true )
            @PathVariable(value = "orderId") String anOrderId ) {

        logger.debug("Fetching order by id: " + anOrderId);
        Optional<Order> order = orderService.findOrderById(UUID.fromString(anOrderId));

        if( order.isEmpty() ) {
            logger.warn("Order not found with id [{}]", anOrderId);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(orderService.findOrderById(UUID.fromString(anOrderId)).get());
    }

    @RequestMapping(
            method = POST,
            value = "",
            consumes = APPLICATION_JSON_VALUE,
            produces = TEXT_PLAIN_VALUE)
    @ApiOperation(value = "Create an Order and generate an id", response = String.class)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Order has been created"),
            @ApiResponse(code = 400, message = "Invalid OrderRequest provided")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "orderRequest",
                    value = "A JSON value representing an order.",
                    required = true, dataType = "OrderRequest", paramType = "body")
    })
    public ResponseEntity<String> createOrder(
            HttpServletRequest request,
            @RequestBody OrderRequest orderRequest) {
        UUID orderId = orderService.upsertOrder(new Order(orderRequest));
        URI location = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath("/api/v1/orders/{orderId}")
                .buildAndExpand(orderId)
                .toUri();
        return ResponseEntity.created(location).body(orderId.toString());
    }

    @RequestMapping(
            method = PUT,
            value = "/{orderId}",
            produces = APPLICATION_JSON_VALUE)
    @ApiOperation(
            value = "Create or update an Order",
            response = ResponseEntity.class)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Order has been created"),
            @ApiResponse(code = 204, message = "No content, order has been updated"),
            @ApiResponse(code = 400, message = "Order id is blank or contains invalid characters (expecting alphanumeric)")
    })
    public ResponseEntity<Void> upsertOrder(
            @ApiParam(name="orderId",
                    example = "4391e230-3c26-11eb-9680-3f1ca592bc5a",
                    value="Id for an order",
                    required=true )
            @PathVariable(value = "orderId") String orderId,
            @RequestBody OrderRequest anOrderRequest) {
        validateId(orderId);
        logger.debug("Request to update customer {}", orderId);
        HttpStatus returnedStatus = orderService.existsOrder(UUID.fromString(orderId)) ? HttpStatus.NO_CONTENT : HttpStatus.CREATED;
        orderService.upsertOrder(new Order(UUID.fromString(orderId),anOrderRequest));
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
