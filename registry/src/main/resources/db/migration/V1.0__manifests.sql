create table manifests
(
    created_at timestamp(6) not null,
    id         bigint       not null primary key,
    length     bigint       not null,
    digest     varchar(255),
    media_type varchar(255),
    name       varchar(255),
    tag        varchar(255),
    content    jsonb        not null
);

create sequence manifest_seq increment by 50;


