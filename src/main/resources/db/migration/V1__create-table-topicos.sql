create table topicos(

    id bigint not null auto_increment,
    titulo varchar(100) not null unique,
    mensaje varchar(400) not null unique,
    fecha varchar(100) not null,
    estatus varchar(100) not null,
    autor bigint not null,
    curso varchar(100) not null,

    primary key (id)
);
