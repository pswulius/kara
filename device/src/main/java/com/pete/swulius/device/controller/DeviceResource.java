package com.pete.swulius.device.controller;

import com.pete.swulius.device.model.Device;
import com.pete.swulius.device.repository.DeviceRepository;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
@RequestMapping("/api/v1/")
@Api(value = "/api/v1/", description = "Device service rest resources", tags = "Device")
public class DeviceResource {

    private static final Logger logger = LoggerFactory.getLogger(DeviceResource.class);

    @Autowired
    private DeviceRepository service;


    // -----------------
    // Device Endpoints
    // -----------------
    @RequestMapping(method = GET, value = "/device", produces = APPLICATION_JSON_VALUE)
    public Flux<Device> findAll() {
        logger.debug("Fetching all devices");
        return service.findAll();
    }

    @GetMapping("/device/{id}")
    public Mono<ResponseEntity<Device>> getById(@PathVariable(value = "id") String aDeviceId) {
        return service.findById(aDeviceId)
                .map(savedDevice -> ResponseEntity.ok(savedDevice))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/device")
    public Mono<Device> create(@RequestBody Device aDevice) {
        return service.save(aDevice);
    }

    @PutMapping("/device/{id}")
    public Mono<ResponseEntity<Device>> update(@PathVariable(value = "id") String aDeviceId, @RequestBody Device aDevice) {
        return service.findById(aDeviceId)
                .flatMap(existingDevice -> {
                    existingDevice.setAssignedName(aDevice.getAssignedName());
                    existingDevice.setBarcode(aDevice.getBarcode());
                    return service.save(existingDevice);
                })
                .map(updateDevice -> new ResponseEntity<>(updateDevice, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
