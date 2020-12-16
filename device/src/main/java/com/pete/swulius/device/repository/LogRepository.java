package com.pete.swulius.device.repository;

import com.pete.swulius.device.model.Log;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LogRepository extends ReactiveCrudRepository<Log, String> {

}