create table auto_post
(
   id serial primary key,
   description varchar,
   created timestamp,
   auto_user_id integer not null references auto_user,
   car_id  int not null references car(id)
);