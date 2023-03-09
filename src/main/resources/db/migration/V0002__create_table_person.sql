create table person(
	id binary(32) not null,
	name varchar(50) not null,
	date_birth date not null,
	address_id binary(32),
    
    primary key(id)
);

alter table person add constraint fk_address_person foreign key (address_id) references address(id);