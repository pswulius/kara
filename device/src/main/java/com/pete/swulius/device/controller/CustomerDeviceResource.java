package com.pete.swulius.device.controller;

import com.pete.swulius.device.model.CustomerDevice;
import com.pete.swulius.device.repository.CustomerDeviceRepository;
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
@Api(value = "/api/v1/", description = "Customer Device service rest resources", tags = "Device (customer)")
public class CustomerDeviceResource {

    private static final Logger logger = LoggerFactory.getLogger(CustomerDeviceResource.class);

    @Autowired
    private CustomerDeviceRepository service;


    // -------------------------
    // CustomerDevice Endpoints
    // -------------------------
    @RequestMapping(method = GET, value = "/customer-device", produces = APPLICATION_JSON_VALUE)
    public Flux<CustomerDevice> findAll() {
        logger.debug("Fetching all customer devices");
        return service.findAll();
    }

    @GetMapping("/customer-device/{id}")
    public Mono<ResponseEntity<CustomerDevice>> getById(@PathVariable(value = "id") String anId) {
        return service.findById(anId)
                .map(savedDevice -> ResponseEntity.ok(savedDevice))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/customer-device")
    public Mono<CustomerDevice> create(@RequestBody CustomerDevice aCustomerDevice) {
        return service.save(aCustomerDevice);
    }

    @PutMapping("/customer-device/{id}")
    public Mono<ResponseEntity<CustomerDevice>> update(@PathVariable(value = "id") String aDeviceId, @RequestBody CustomerDevice aCustomerDevice) {
        return service.findById(aDeviceId)
                .flatMap(existingDevice -> {
                    existingDevice.setAssignedName(aCustomerDevice.getAssignedName());
                    existingDevice.setBarcode(aCustomerDevice.getBarcode());
                    return service.save(existingDevice);
                })
                .map(updateCustomerDevice -> new ResponseEntity<>(updateCustomerDevice, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
