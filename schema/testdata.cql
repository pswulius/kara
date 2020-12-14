truncate customers;
insert into customers ( customerid, email, name, phone, address ) values ( now(), 'mario@gmail.com', 'Mario', '123-456-7890', { street:'1234 Fake St.', city:'dallas', state:'tx', zipcode:'75214' } );
insert into customers ( customerid, email, name, phone, address ) values ( now(), 'peach@gmail.com', 'Peach', '123-456-7890', { street:'7789 Fake St.', city:'dallas', state:'tx', zipcode:'75214' } );
insert into customers ( customerid, email, name, phone, address ) values ( now(), 'luigi@gmail.com', 'Luigi', '123-456-7890', { street:'2231 Fake St.', city:'austin', state:'tx', zipcode:'75214' } );


truncate city_states;
insert into city_states ( city, state ) values ( 'dallas',   'tx' );
insert into city_states ( city, state ) values ( 'austin',   'tx' );
insert into city_states ( city, state ) values ( 'houston',  'tx' );
insert into city_states ( city, state ) values ( 'phoenix',  'az' );
insert into city_states ( city, state ) values ( 'cheyenne', 'wy' );

truncate orders;
insert into orders ( orderid, added, customerid, quantity ) values ( now(), toTimestamp(now()), 7b4bb620-3ca2-11eb-9680-3f1ca592bc5a, 3 );