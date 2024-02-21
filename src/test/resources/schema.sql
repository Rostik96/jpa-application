drop table if exists person;

create table person (
    id          uuid primary key,
    name varchar,
    random_uuid uuid default uuid_in(md5(random()::text || random()::text)::cstring) -- gen_random_uuid() introduced since `Postgre 13`
);