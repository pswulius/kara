package com.pete.swulius.device.repository;

import com.pete.swulius.device.model.CustomerDevice;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerDeviceRepository extends ReactiveCrudRepository<CustomerDevice, String> {


}