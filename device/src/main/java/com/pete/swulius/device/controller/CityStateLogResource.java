package com.pete.swulius.device.controller;

import com.pete.swulius.device.model.CityStateLog;
import com.pete.swulius.device.repository.CityStateLogRepository;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
@RequestMapping("/api/v1/")
@Api(value = "/api/v1/", description = "CityState Log service rest resources", tags = "Log (citystate)" )
public class CityStateLogResource {

    private static final Logger logger = LoggerFactory.getLogger(CityStateLogResource.class);

    @Autowired
    private CityStateLogRepository service;



    // ------------------------
    // CityState Log Endpoints
    // ------------------------
    @RequestMapping(method = GET, value = "/citystatelog", produces = APPLICATION_JSON_VALUE)
    public Flux<CityStateLog> findAll() {
        return service.findAll();
    }

    @GetMapping("/citystatelog/{city}/{state}/{deviceid}/{added}")
    public Mono<ResponseEntity<CityStateLog>> getById(
            @PathVariable(name="city") String aCity,
            @PathVariable(name="state") String aState,
            @PathVariable(name="deviceid") UUID aDeviceId,
            @PathVariable(name="added") Instant anAdded) {
        MapId id = BasicMapId.id("city",aCity ).with("state", aState).with("deviceid",aDeviceId).with("added", anAdded );
        return service.findById(id)
                .map(saved -> ResponseEntity.ok(saved))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/citystatelog")
    public Mono<CityStateLog> create(@RequestBody CityStateLog aLog) {
        return service.save(aLog);
    }

    @PutMapping("/citystatelog/{id}")
    @Deprecated
    public Mono<ResponseEntity<CityStateLog>> update(
            @PathVariable(value = "city") String aCity,
            @PathVariable(value = "state") String aState,
            @RequestBody CityStateLog aLog) {
        MapId id = BasicMapId.id("city",aCity ).with("state", aState);
        return service.findById(id)
                .flatMap(existingDevice -> {
                    // update fields
                    // update fields
                    return service.save(existingDevice);
                })
                .map(updateDevice -> new ResponseEntity<>(updateDevice, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
