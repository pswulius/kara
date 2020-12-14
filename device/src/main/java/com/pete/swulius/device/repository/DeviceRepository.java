package com.pete.swulius.device.repository;

import com.pete.swulius.device.model.Device;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface DeviceRepository extends ReactiveCrudRepository<Device,String> {

    @Query("select * from tweet")
    Mono<Device> findDeviceById(String anId); // for demo purposes

}