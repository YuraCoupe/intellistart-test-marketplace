CREATE EXTENSION "uuid-ossp";

CREATE TABLE users(
	id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
	username VARCHAR(100) NOT NULL UNIQUE,
	password VARCHAR(200) NOT NULL,
	first_name VARCHAR(200) NOT NULL,
	last_name VARCHAR(200) NOT NULL,
	money numeric

);

CREATE TABLE roles (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name varchar(50)
);

CREATE TABLE users_roles (
	id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
	user_id UUID REFERENCES users(id),
	role_id UUID REFERENCES roles(id)
);

CREATE TABLE products(
	id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
	name VARCHAR(200) NOT NULL,
    price numeric
);

CREATE TABLE checkout(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    user_id UUID REFERENCES users(id),
    product_id UUID REFERENCES products(id)
);