create table user_created
(
    user_id    serial      not null primary key,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    email      varchar(64) not null,
    birth_date date not null,
    address text,
    phone_number text
);
