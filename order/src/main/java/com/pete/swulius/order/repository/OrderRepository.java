package com.pete.swulius.order.repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.pete.swulius.order.model.Address;
import com.pete.swulius.order.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;
import static com.datastax.oss.driver.api.querybuilder.relation.Relation.column;


@Repository
public class OrderRepository {

    /** Logger for the class. */
    private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

    // Reservation Schema Constants
    public static final CqlIdentifier TYPE_ADDRESS          = CqlIdentifier.fromCql("address");
    public static final CqlIdentifier TABLE_CUSTOMERS       = CqlIdentifier.fromCql("customers");
    public static final CqlIdentifier CUSTOMER_ID           = CqlIdentifier.fromCql("customerid");
    public static final CqlIdentifier CUSTOMER_EMAIL        = CqlIdentifier.fromCql("email");
    public static final CqlIdentifier CUSTOMER_NAME         = CqlIdentifier.fromCql("name");
    public static final CqlIdentifier CUSTOMER_PHONE        = CqlIdentifier.fromCql("phone");
    public static final CqlIdentifier CUSTOMER_ADDRESS      = CqlIdentifier.fromCql("address");
    public static final CqlIdentifier ADDRESS_STREET        = CqlIdentifier.fromCql("street");
    public static final CqlIdentifier ADDRESS_CITY          = CqlIdentifier.fromCql("city");
    public static final CqlIdentifier ADDRESS_STATE         = CqlIdentifier.fromCql("state");
    public static final CqlIdentifier ADDRESS_ZIPCODE       = CqlIdentifier.fromCql("zipcode");

    private PreparedStatement psExistCustomer;
    private PreparedStatement psFindCustomer;
    private PreparedStatement psFindAllCustomers;

    /** CqlSession holding metadata to interact with Cassandra. */
    private CqlSession     cqlSession;
    private CqlIdentifier  keyspaceName;

    /** External Initialization. */
    public OrderRepository(
            @NonNull CqlSession cqlSession,
            @Qualifier("keyspace") @NonNull CqlIdentifier keyspaceName) {
        this.cqlSession   = cqlSession;
        this.keyspaceName = keyspaceName;
        
        // Will create tables (if they do not exist)
        //createReservationTables();
        
        // Prepare Statements of reservation
        prepareStatements();
        logger.info("Application initialized.");
    }
    
    /**
     * CqlSession is a stateful object handling TCP connections to nodes in the cluster.
     * This operation properly closes sockets when the application is stopped
     */
    @PreDestroy
    public void cleanup() {
        if (null != cqlSession) {
            cqlSession.close();
            logger.info("+ CqlSession has been successfully closed");
        }
    }
    

    public boolean exists(String anId) {
        return cqlSession.execute(psExistCustomer.bind(anId))
                         .getAvailableWithoutFetching() > 0;
    }


    // ----------
    // Customer
    // ----------
    public List<Customer> findAllCustomers() {
        return cqlSession.execute(psFindAllCustomers.bind())
                .all()                          // no paging we retrieve all objects
                .stream()                       // because we are good people
                .map(this::mapRowToCustomer)    // Mapping row as Customer
                .collect(Collectors.toList());  // Back to list objects
    }

    @NonNull
    public Optional<Customer> findCustomerById(@NonNull UUID anId) {
        
        ResultSet resultSet = cqlSession.execute(psFindCustomer.bind(anId));
        
        // Hint: an empty result might not be an error as this method is sometimes used to check whether a
        // reservation with this confirmation number exists
        Row row = resultSet.one();
        if (row == null) {
            logger.debug("Unable to load customer with id: " + anId);
            return Optional.empty();
        }
        
        // Hint: If there is a result, create a new reservation object and set the values
        // Bonus: factor the logic to extract a reservation from a row into a separate method
        // (you will reuse it again later in getAllReservations())
        return Optional.of(mapRowToCustomer(row));
    }

    private void prepareStatements() {
        if (psExistCustomer == null) {
            psExistCustomer = cqlSession.prepare(selectFrom(keyspaceName, TABLE_CUSTOMERS).column(CUSTOMER_ID)
                    .where(column(CUSTOMER_ID).isEqualTo(bindMarker(CUSTOMER_ID)))
                    .build());
            psFindCustomer = cqlSession.prepare(
                    selectFrom(keyspaceName, TABLE_CUSTOMERS).all()
                            .where(column(CUSTOMER_ID).isEqualTo(bindMarker(CUSTOMER_ID)))
                            .build());
            psFindAllCustomers = cqlSession.prepare(
                    selectFrom(keyspaceName, TABLE_CUSTOMERS).all()
                            .build());
            logger.info("Statements have been successfully prepared.");
        }
    }



    private Customer mapRowToCustomer(Row row) {
        Customer customer = new Customer();
        customer.setId(row.getUuid(CUSTOMER_ID));
        customer.setName(row.getString(CUSTOMER_NAME));
        customer.setEmail(row.getString(CUSTOMER_EMAIL));
        customer.setPhone(row.getString(CUSTOMER_PHONE));

        Address address = new Address();
        address.setStreet(row.getUdtValue(CUSTOMER_ADDRESS).getString(ADDRESS_STREET));
        address.setCity(row.getUdtValue(CUSTOMER_ADDRESS).getString(ADDRESS_CITY));
        address.setState(row.getUdtValue(CUSTOMER_ADDRESS).getString(ADDRESS_STATE));
        address.setZipcode(row.getUdtValue(CUSTOMER_ADDRESS).getString(ADDRESS_ZIPCODE));
        customer.setAddress(address);

        return customer;
    }
}