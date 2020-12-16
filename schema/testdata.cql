truncate kara.customers;
insert into kara.customers (customerid, email, name, phone, address)
values (now(), 'mario@gmail.com', 'Mario', '123-456-7890',
        { street:'1234 Fake St.', city:'dallas', state:'tx', zipcode:'75214' });
insert into kara.customers (customerid, email, name, phone, address)
values (now(), 'peach@gmail.com', 'Peach', '123-456-7890',
        { street:'7789 Fake St.', city:'dallas', state:'tx', zipcode:'75214' });
insert into kara.customers (customerid, email, name, phone, address)
values (now(), 'luigi@gmail.com', 'Luigi', '123-456-7890',
        { street:'2231 Fake St.', city:'austin', state:'tx', zipcode:'75214' });


truncate kara.orders;
insert into kara.orders (orderid, added, customerid, quantity)
values (now(), toTimestamp(now()), 7b4bb620-3ca2-11eb-9680-3f1ca592bc5a, 3);