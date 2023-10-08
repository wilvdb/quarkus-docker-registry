create table blobs
(
    created_at timestamp(6) not null,
    id         bigint       not null primary key,
    length     bigint       not null,
    digest     varchar(255),
    name       varchar(255) not null,
    uuid       varchar(255) not null
);

create sequence blob_seq
    increment by 50;
