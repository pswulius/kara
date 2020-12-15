package com.pete.swulius.device.demo;

import com.pete.swulius.device.conf.KaraProperties;
import com.pete.swulius.device.model.CityState;
import com.pete.swulius.device.model.CityStateLog;
import com.pete.swulius.device.repository.CityStateLogRepository;
import com.pete.swulius.device.repository.CityStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Configuration
@EnableAsync
@EnableScheduling
@Component
public class DeviceSimulation
{
    private static final Logger logger = LoggerFactory.getLogger(DeviceSimulation.class);
    private static List<CityState> cities;


    @Autowired
    private CityStateRepository csr;

    @Autowired
    private CityStateLogRepository cslr;

    @Autowired
    private KaraProperties kp;


    @Scheduled(initialDelay=5000, fixedRate=5000)
    public void doSomething() {

        if(!SampleDataInitializer.isReady || kp.run == false) return;
        if(cslr.count().block() > kp.maxLogs) return;

        if( cities == null)
        {
            cities = csr.findAll().collectList().block();
        }


        long start = System.currentTimeMillis();
        List<CityStateLog> logs = createLogs();
        cslr.saveAll(logs).subscribe();
        long end = System.currentTimeMillis();
        long diff = end-start;

        logger.info("Generated [" + kp.logsPer + "] device logs in [" + diff + "ms]");
    }


    private List<CityStateLog> createLogs()
    {
        List<CityStateLog> list = new ArrayList<CityStateLog>();

        for( int i=0; i<kp.logsPer; i++ )
        {
            CityStateLog each = createLog();
            list.add(each);
        }

        return list;
    }


    private CityStateLog createLog() {
        CityState cs = getRandomCityState();
        logger.debug("saving city: " + cs);

        CityStateLog log = new CityStateLog();
        log.setDeviceid(UUID.randomUUID());
        log.setAdded(Instant.now());
        log.setCity(cs.getCity());
        log.setState(cs.getState());
        log.setValue(getRandomNumberInRange(0,200)); // fake glucose reading
        return log;
    }


    private CityState getRandomCityState() {
        return cities.get(getRandomNumberInRange(0,cities.size()-1));
    }


    private static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.ints(min, (max + 1)).findFirst().getAsInt();
    }




}

