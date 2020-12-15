package com.pete.swulius.device.demo;

import com.pete.swulius.device.model.CityState;
import com.pete.swulius.device.repository.CityStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

@Component
//@Profile("demo")
class SampleDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(SampleDataInitializer.class);
    private final CityStateRepository repository;

    public SampleDataInitializer(CityStateRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Path csvPath = Paths.get("schema/cities.csv");
        logger.info( "Adding sample date from: " + csvPath );

        Flux<String> lFlux = Flux.using(
                () -> Files.lines(csvPath),
                Flux::fromStream,
                Stream::close
        );

        repository
                .deleteAll()
                .thenMany(
                        Flux
                                .from(lFlux)
                                .map(line -> new CityState(line))
                                .flatMap(repository::save)
                )
                .subscribe();
    }
}
