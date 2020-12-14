package com.pete.swulius.device.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;


@Table("logs")
public class Log {

    @PrimaryKey
    private UUID deviceid;
    private LocalDate added;
    private String city;
    private String state;
    private Integer value;

    public Log() {

    }

    public UUID getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(UUID deviceid) {
        this.deviceid = deviceid;
    }

    public LocalDate getAdded() {
        return added;
    }

    public void setAdded(LocalDate added) {
        this.added = added;
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

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
