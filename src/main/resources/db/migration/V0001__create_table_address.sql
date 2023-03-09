create table address(
	id binary(32) not null,
	address varchar(100) not null,
	zip_code varchar(20) not null,
	number int(10) not null,
	city varchar(50) not null,
    
	primary key(id)
);