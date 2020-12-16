package com.pete.swulius.device.controller;

import com.pete.swulius.device.model.Log;
import com.pete.swulius.device.repository.LogRepository;
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
@Api(value = "/api/v1/", description = "Log service rest resources", tags = "Log")
public class LogResource {

    private static final Logger logger = LoggerFactory.getLogger(LogResource.class);

    @Autowired
    private LogRepository service;


    // -----------------
    // Device Endpoints
    // -----------------
    @RequestMapping(method = GET, value = "/log", produces = APPLICATION_JSON_VALUE)
    public Flux<Log> findAll() {
        return service.findAll();
    }

    @GetMapping("/log/{id}")
    public Mono<ResponseEntity<Log>> getById(@PathVariable(value = "id") String anId) {
        return service.findById(anId)
                .map(saved -> ResponseEntity.ok(saved))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/log")
    public Mono<Log> create(@RequestBody Log aLog) {
        return service.save(aLog);
    }

    @PutMapping("/log/{id}")
    public Mono<ResponseEntity<Log>> update(@PathVariable(value = "id") String anId, @RequestBody Log aLog) {
        return service.findById(anId)
                .flatMap(existingDevice -> {
                    // update fields
                    // update fields
                    return service.save(existingDevice);
                })
                .map(updateDevice -> new ResponseEntity<>(updateDevice, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
