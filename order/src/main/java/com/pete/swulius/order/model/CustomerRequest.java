package com.pete.swulius.order.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.UUID;


public class CustomerRequest implements Serializable {

    private static final long serialVersionUID = 5237647000249960260L;

    @ApiModelProperty(value = "Customer email address", example = "email@domain.com")
    private String email;

    @ApiModelProperty(value = "Customer name", example = "Mario")
    private String name;

    @ApiModelProperty(value = "Customer phone number", example = "111-222-3333")
    private String phone;

    @ApiModelProperty(value = "Customer physical address")
    private Address address;


    public CustomerRequest() {
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
