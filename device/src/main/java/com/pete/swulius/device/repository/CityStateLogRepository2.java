package com.pete.swulius.device.repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.pete.swulius.device.model.CityStateLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;
import static com.datastax.oss.driver.api.querybuilder.relation.Relation.column;


@Repository
public class CityStateLogRepository2 {
    private static final Logger logger = LoggerFactory.getLogger(CityStateLogRepository2.class);

    private CqlSession cqlSession;
    private CqlIdentifier keyspaceName;

    private PreparedStatement psFindLogsByCityState;

    public static final CqlIdentifier TABLE_LOGS_BY_CITY_STATE = CqlIdentifier.fromCql("logs_by_citystate");
    public static final CqlIdentifier LBCS_CITY = CqlIdentifier.fromCql("city");
    public static final CqlIdentifier LBCS_STATE = CqlIdentifier.fromCql("state");
    public static final CqlIdentifier LBCS_ADDED = CqlIdentifier.fromCql("added");
    public static final CqlIdentifier LBCS_DEVICEID = CqlIdentifier.fromCql("deviceid");
    public static final CqlIdentifier LBCS_VALUE = CqlIdentifier.fromCql("value");


    public CityStateLogRepository2(
            @NonNull CqlSession cqlSession,
            @Qualifier("keyspace") @NonNull CqlIdentifier keyspaceName) {
        this.cqlSession = cqlSession;
        this.keyspaceName = keyspaceName;

        prepareStatements();
        logger.info("Device application repository2 initialized.");
    }

    public List<CityStateLog> getLogsForCityState( String aCity, String aState ) {
        return cqlSession.execute(psFindLogsByCityState.bind( aCity, aState))
                .all()
                .stream()
                .map(this::mapRowToCityStateLog)
                .collect(Collectors.toList());
    }


    // ---------------
    // private methods
    // ---------------
    private void prepareStatements() {
        if (psFindLogsByCityState == null) {
            psFindLogsByCityState = cqlSession.prepare(
                    selectFrom(keyspaceName, TABLE_LOGS_BY_CITY_STATE).all()
                            .where(column(LBCS_CITY).isEqualTo(bindMarker(LBCS_CITY)))
                            .where(column(LBCS_STATE).isEqualTo(bindMarker(LBCS_STATE)))
                            .build());
        }
    }

    private CityStateLog mapRowToCityStateLog(Row row) {
        CityStateLog log = new CityStateLog();
        log.setCity(row.getString(LBCS_CITY));
        log.setState(row.getString(LBCS_STATE));
        log.setAdded(row.getInstant(LBCS_ADDED));
        log.setDeviceid(row.getUuid(LBCS_DEVICEID));
        log.setValue((int)row.getShort(LBCS_VALUE));
        return log;
    }
}