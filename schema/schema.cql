DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customer_orders;
DROP TABLE IF EXISTS foobar;
DROP TABLE IF EXISTS city_states;
DROP TABLE IF EXISTS devices;
DROP TABLE IF EXISTS customer_devices;
DROP TABLE IF EXISTS logs;
DROP TABLE IF EXISTS logs_by_citystate;
DROP TYPE IF EXISTS address;


CREATE TYPE IF NOT EXISTS kara.address (
    street text,
    city text,
    state text,
    zipcode text
);

CREATE TABLE IF NOT EXISTS customers (
    customerid uuid,
    email text,
    name text,
    phone text,
    address FROZEN<kara.address>,
    PRIMARY KEY ( customerid )
);

CREATE TABLE IF NOT EXISTS orders (
    orderid uuid,
    added timestamp,
    customerid uuid,
    quantity tinyint,
    PRIMARY KEY( orderid, added )
);

CREATE TABLE IF NOT EXISTS customer_orders (
    customerid uuid,
    added timestamp,
    orderid uuid,
    quantity tinyint,
    PRIMARY KEY( customerid, added )
);

CREATE TABLE IF NOT EXISTS city_states (
    city text,
    state text,
    population int,
    density smallint,
    timzone text,
    PRIMARY KEY( (state, city) )
);

CREATE TABLE IF NOT EXISTS devices (
    deviceid uuid,
    barcode text,
    assigned_name text,
    PRIMARY KEY( deviceid )
);

CREATE TABLE IF NOT EXISTS customer_devices (
    customerid uuid,
    deviceid uuid,
    barcode text,
    assigned_name text,
    PRIMARY KEY( customerid, deviceid )
);

CREATE TABLE IF NOT EXISTS logs (
    deviceid uuid,
    added timestamp,
    city text,
    state text,
    value tinyint,
    PRIMARY KEY( deviceid, added )
);

CREATE TABLE IF NOT EXISTS logs_by_citystate (
    city text,
    state text,
    added timestamp,
    deviceid uuid,
    value tinyint,
    PRIMARY KEY( (city,state), deviceid, added )
);





















