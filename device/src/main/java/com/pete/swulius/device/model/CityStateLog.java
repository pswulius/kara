package com.pete.swulius.device.model;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;


@Table("logs_by_citystate")
public class CityStateLog {

    @PrimaryKeyColumn(name = "city", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String city;

    @PrimaryKeyColumn(name = "state", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String state;

    @PrimaryKeyColumn(name = "deviceid", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private UUID deviceid;

    @PrimaryKeyColumn(name = "added", ordinal = 3, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private Instant added;

    private Integer value;

    public CityStateLog() {

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

    public Instant getAdded() {
        return added;
    }

    public void setAdded(Instant added) {
        this.added = added;
    }

    public UUID getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(UUID deviceid) {
        this.deviceid = deviceid;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
