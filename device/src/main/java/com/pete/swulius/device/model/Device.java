package com.pete.swulius.device.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;


@Table("devices")
public class Device {

    @PrimaryKey
    private UUID deviceid;
    private String barcode;
    private String assignedName;

    public Device() {

    }

    public UUID getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(UUID deviceid) {
        this.deviceid = deviceid;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getAssignedName() {
        return assignedName;
    }

    public void setAssignedName(String assignedName) {
        this.assignedName = assignedName;
    }
}
