package com.pete.swulius.device.repository;

import com.pete.swulius.device.model.CityState;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CityStateRepository extends ReactiveCrudRepository<CityState, MapId> {

}