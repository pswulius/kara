-- order
select * from customers;
select * from customer_devices;
select * from customer_orders;

-- devic
select * from devices;
select * from logs;
select * from logs_by_citystate;
select * from logs_by_citystate where city = 'Quitman' and state = 'MO';

-- static data
select * from city_states;
select count(*) from city_states;

