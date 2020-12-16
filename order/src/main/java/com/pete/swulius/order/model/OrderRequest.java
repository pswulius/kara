package com.pete.swulius.order.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.UUID;


public class OrderRequest implements Serializable {

    private static final long serialVersionUID = 5237647000249960260L;

    @ApiModelProperty(value = "Customer Id for order", example = "4391e230-3c26-11eb-9680-3f1ca592bc5a")
    private UUID customerId;

    @ApiModelProperty(value = "Quantity of devices in order", example = "7")
    private Integer quantity;


    public OrderRequest() {
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
}
