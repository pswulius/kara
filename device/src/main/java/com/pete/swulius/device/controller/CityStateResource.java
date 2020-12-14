package com.pete.swulius.device.controller;

import com.pete.swulius.device.model.CityState;
import com.pete.swulius.device.repository.CityStateRepository;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
@RequestMapping("/api/v1/")
@Api(value = "/api/v1/", description = "CityState service rest resources", tags = "City/State" )
public class CityStateResource {

    private static final Logger logger = LoggerFactory.getLogger(CityStateResource.class);

    @Autowired
    private CityStateRepository service;



    // --------------------
    // CityState Endpoints
    // --------------------
    @RequestMapping(method = GET, value = "/citystate", produces = APPLICATION_JSON_VALUE)
    public Flux<CityState> findAll() {
        return service.findAll();
    }

    @PostMapping("/citystate")
    @Deprecated
    public Mono<CityState> create(@RequestBody CityState aCityState) {
        return service.save(aCityState);
    }

}
