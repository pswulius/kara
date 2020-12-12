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
package com.pete.swulius.order.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Customer implements Serializable {

    /** Serial. */
    private static final long serialVersionUID = -3392237616280919281L;

    private UUID id;
    private String email;
    private String name;
    private String phone;
    private Address address;


    public Customer()
    {
    }


//    public Customer(ReservationRequest form) {
//        setStartDate(form.getStartDate());
//        setEndDate(form.getEndDate());
//        setHotelId(form.getHotelId());
//        setGuestId(form.getGuestId());
//        setRoomNumber(form.getRoomNumber());
//    }


//    public Customer(ReservationRequest form, String confirmationNumber) {
//        this(form);
//        this.confirmationNumber = confirmationNumber;
//    }


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
