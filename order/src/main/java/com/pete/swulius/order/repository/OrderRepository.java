package com.pete.swulius.order.repository;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.pete.swulius.order.model.Address;
import com.pete.swulius.order.model.Customer;
import com.pete.swulius.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.time.Instant;
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

    public static final CqlIdentifier TABLE_ORDERS          = CqlIdentifier.fromCql("orders");
    public static final CqlIdentifier ORDER_ID              = CqlIdentifier.fromCql("orderid");
    public static final CqlIdentifier ORDER_ADDED           = CqlIdentifier.fromCql("added");
    public static final CqlIdentifier ORDER_CUSTOMER_ID     = CqlIdentifier.fromCql("customerid");
    public static final CqlIdentifier ORDER_QUANTITY        = CqlIdentifier.fromCql("quantity");


    private PreparedStatement psExistCustomer;
    private PreparedStatement psFindCustomer;
    private PreparedStatement psFindAllCustomers;
    private PreparedStatement psInsertCustomer;

    private PreparedStatement psExistOrder;
    private PreparedStatement psFindOrder;
    private PreparedStatement psFindAllOrders;
    private PreparedStatement psInsertOrder;

    private CqlSession     cqlSession;
    private CqlIdentifier  keyspaceName;

    public OrderRepository(
            @NonNull CqlSession cqlSession,
            @Qualifier("keyspace") @NonNull CqlIdentifier keyspaceName) {
        this.cqlSession   = cqlSession;
        this.keyspaceName = keyspaceName;

        prepareStatements();
        logger.info("Application initialized.");
    }
    

    @PreDestroy
    public void cleanup() {
        if (null != cqlSession) {
            cqlSession.close();
            logger.info("+ CqlSession has been successfully closed");
        }
    }
    




    // ----------
    // Customer
    // ----------
    public boolean exists(UUID anId) {
        return cqlSession.execute(psExistCustomer.bind(anId))
                .getAvailableWithoutFetching() > 0;
    }

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
        Row row = resultSet.one();

        if (row == null) {
            logger.debug("Unable to load customer with id: " + anId);
            return Optional.empty();
        }

        return Optional.of(mapRowToCustomer(row));
    }

    public UUID upsertCustomer(Customer aCustomer) {
        Objects.requireNonNull(aCustomer);

        if( aCustomer.getId() == null ) {
            aCustomer.setId(UUID.randomUUID());
        }

        UserDefinedType udt = (UserDefinedType) psInsertCustomer.getVariableDefinitions().get("address").getType();
        Address a = aCustomer.getAddress();
        UdtValue addressValue = udt.newValue()
                .setString(0,a.getStreet())
                .setString(1,a.getCity())
                .setString(2,a.getState())
                .setString(3,a.getZipcode());

        // Insert into 'customers'
        BoundStatement bsInsertCustomer =
                psInsertCustomer.bind(aCustomer.getId(), aCustomer.getEmail(),
                        aCustomer.getName(), aCustomer.getPhone(), addressValue);

        BatchStatement batchInsertReservation = BatchStatement
                .builder(DefaultBatchType.LOGGED)
                .addStatement(bsInsertCustomer)
                .build();
        cqlSession.execute(batchInsertReservation);
        return aCustomer.getId();
    }



    // -------
    // Orders
    // -------
    public boolean existsOrder(UUID anId) {
        return cqlSession.execute(psExistOrder.bind(anId))
                .getAvailableWithoutFetching() > 0;
    }

    public List<Order> findAllOrders() {
        return cqlSession.execute(psFindAllOrders.bind())
                .all()                          // no paging we retrieve all objects
                .stream()                       // because we are good people
                .map(this::mapRowToOrder)       // Mapping row as Order
                .collect(Collectors.toList());  // Back to list objects
    }

    @NonNull
    public Optional<Order> findOrderById(@NonNull UUID anId) {

        ResultSet resultSet = cqlSession.execute(psFindOrder.bind(anId));
        Row row = resultSet.one();

        if (row == null) {
            logger.debug("Unable to load order with id: " + anId);
            return Optional.empty();
        }

        return Optional.of(mapRowToOrder(row));
    }

    public UUID upsertOrder(Order anOrder) {
        Objects.requireNonNull(anOrder);

        if( anOrder.getOrderId() == null ) {
            anOrder.setOrderId(UUID.randomUUID());
        }

        // Insert into 'orders'
        BoundStatement bsInsertOrder =
                psInsertOrder.bind(anOrder.getOrderId(), anOrder.getAdded(),
                        anOrder.getCustomerId(), anOrder.getQuantity().byteValue());

        BatchStatement batchInsert = BatchStatement
                .builder(DefaultBatchType.LOGGED)
                .addStatement(bsInsertOrder)
                .build();
        cqlSession.execute(batchInsert);
        return anOrder.getOrderId();
    }


    // ---------------
    // private methods
    // ---------------
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
            psInsertCustomer = cqlSession.prepare(QueryBuilder.insertInto(keyspaceName, TABLE_CUSTOMERS)
                            .value(CUSTOMER_ID, bindMarker(CUSTOMER_ID))
                            .value(CUSTOMER_EMAIL, bindMarker(CUSTOMER_EMAIL))
                            .value(CUSTOMER_NAME, bindMarker(CUSTOMER_NAME))
                            .value(CUSTOMER_PHONE, bindMarker(CUSTOMER_PHONE))
                            .value(CUSTOMER_ADDRESS, bindMarker(CUSTOMER_ADDRESS))
                            .build());

            psExistOrder = cqlSession.prepare(selectFrom(keyspaceName, TABLE_ORDERS).column(ORDER_ID)
                    .where(column(ORDER_ID).isEqualTo(bindMarker(ORDER_ID)))
                    .build());
            psFindOrder = cqlSession.prepare(
                    selectFrom(keyspaceName, TABLE_ORDERS).all()
                            .where(column(ORDER_ID).isEqualTo(bindMarker(ORDER_ID)))
                            .build());
            psFindAllOrders = cqlSession.prepare(
                    selectFrom(keyspaceName, TABLE_ORDERS).all()
                            .build());
            psInsertOrder = cqlSession.prepare(QueryBuilder.insertInto(keyspaceName, TABLE_ORDERS)
                    .value(ORDER_ID, bindMarker(ORDER_ID))
                    .value(ORDER_ADDED, bindMarker(ORDER_ADDED))
                    .value(ORDER_CUSTOMER_ID, bindMarker(ORDER_CUSTOMER_ID))
                    .value(ORDER_QUANTITY, bindMarker(ORDER_QUANTITY))
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

    private Order mapRowToOrder(Row row) {
        Order order = new Order();
        order.setOrderId(row.getUuid(ORDER_ID));
        order.setCustomerId(row.getUuid(ORDER_CUSTOMER_ID));
        order.setAdded(row.getInstant(ORDER_ADDED));
        order.setQuantity((int)row.getByte(ORDER_QUANTITY));
        return order;
    }
}