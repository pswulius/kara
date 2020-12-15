
CREATE TYPE IF NOT EXISTS kara.address (
    street text,
    city text,
    state text,
    zipcode text
);

DROP TABLE IF EXISTS kara.customers;
CREATE TABLE IF NOT EXISTS kara.customers (
    customerid uuid,
    email text,
    name text,
    phone text,
    address FROZEN<kara.address>,
    PRIMARY KEY ( customerid )
);

DROP TABLE IF EXISTS kara.orders;
CREATE TABLE IF NOT EXISTS kara.orders (
    orderid uuid,
    added timestamp,
    customerid uuid,
    quantity tinyint,
    PRIMARY KEY( orderid )
);

DROP TABLE IF EXISTS kara.customer_orders;
CREATE TABLE IF NOT EXISTS kara.customer_orders (
    customerid uuid,
    added timestamp,
    orderid uuid,
    quantity tinyint,
    PRIMARY KEY( customerid, added )
);

DROP TABLE IF EXISTS kara.city_states;
CREATE TABLE IF NOT EXISTS kara.city_states (
     city text,
     state text,
     population int,
     density int,
     timezone text,
     PRIMARY KEY( (state, city) )
);

DROP TABLE IF EXISTS kara.devices;
CREATE TABLE IF NOT EXISTS kara.devices (
     deviceid uuid,
     barcode text,
     assigned_name text,
     PRIMARY KEY( deviceid )
);

DROP TABLE IF EXISTS kara.customer_devices;
CREATE TABLE IF NOT EXISTS kara.customer_devices (
     customerid uuid,
     deviceid uuid,
     barcode text,
     assigned_name text,
     PRIMARY KEY( customerid, deviceid )
);

DROP TABLE IF EXISTS kara.logs;
CREATE TABLE IF NOT EXISTS kara.logs (
     deviceid uuid,
     added timestamp,
     city text,
     state text,
     value smallint,
     PRIMARY KEY( deviceid, added )
);

DROP TABLE IF EXISTS kara.logs_by_citystate;
CREATE TABLE IF NOT EXISTS kara.logs_by_citystate (
    city text,
    state text,
    added timestamp,
    deviceid uuid,
    value smallint,
    PRIMARY KEY( (city,state), deviceid, added )
);

