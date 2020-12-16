package com.pete.swulius.order.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.UUID;

public class Address implements Serializable {

    /**
     * Serial.
     */
    private static final long serialVersionUID = -3392237616230919281L;

    @ApiModelProperty(value = "Street address", example = "1234 Fake St.")
    private String street;

    @ApiModelProperty(value = "City", example = "dallas")
    private String city;

    @ApiModelProperty(value = "State", example = "TX")
    private String state;

    @ApiModelProperty(value = "Zipcoe", example = "75214")
    private String zipcode;


    public Address() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return "Address = " + getStreet() +
                ", City: " + getCity() +
                ", State: " + getState() +
                ", Zipcode = " + getZipcode();
    }
}
