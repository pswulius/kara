package com.pete.swulius.order.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Customer implements Serializable {

    /**
     * Serial.
     */
    private static final long serialVersionUID = -3392237616280919281L;

    private UUID id;
    private String email;
    private String name;
    private String phone;
    private Address address;


    public Customer() {
    }

    public Customer(UUID anId, CustomerRequest aRequest) {
        this(aRequest);
        id = anId;
    }

    public Customer(CustomerRequest aRequest) {
        setEmail(aRequest.getEmail());
        setName(aRequest.getName());
        setPhone(aRequest.getPhone());
        setAddress(aRequest.getAddress());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Customer = " + getId() +
                ", Name: " + getName() +
                ", Email: " + getEmail() +
                ", Phone = " + getPhone() +
                ", Address = " + getAddress();
    }
}
