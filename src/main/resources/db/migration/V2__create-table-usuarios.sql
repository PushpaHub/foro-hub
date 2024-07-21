create table usuarios(

    id bigint not null auto_increment,
    nombre varchar(100) not null,
    login varchar(100) not null unique, -- email
    clave varchar(300) not null,

    primary key (id)
);
