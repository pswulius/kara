package com.pete.swulius.order.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order implements Serializable {


    private static final long serialVersionUID = 1601683616281724960L;
    private UUID orderId;
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss:ssZ")
    private Instant added;
    private UUID customerId;
    private Integer quantity;


    public Order() {
    }

    public Order(OrderRequest aRequest) {
        setAdded(Instant.now());
        setCustomerId(aRequest.getCustomerId());
        setQuantity(aRequest.getQuantity());
    }

    public Order(UUID anId, OrderRequest aRequest) {
        this(aRequest);
        setOrderId(anId);
    }


    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public Instant getAdded() {
        return added;
    }

    public void setAdded(Instant added) {
        this.added = added;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order = " + getOrderId() +
                ", Added: " + getAdded() +
                ", Customer: " + getCustomerId() +
                ", Quantity = " + getQuantity();
    }
}
