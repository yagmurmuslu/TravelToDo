BEGIN TRANSACTION;

--Drop all data base in the proper order

DROP TABLE IF EXISTS wish_to_see;
DROP TABLE IF EXISTS users;

-- Create the tables and constraints

CREATE TABLE users(
	user_id SERIAL,
	name varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id),
	CONSTRAINT UC_user_name UNIQUE (name)
);

INSERT INTO users(name, password_hash) VALUES ('user1', 'user1password');
INSERT INTO users(name, password_hash) VALUES ('user2', 'user2password');

CREATE TABLE wish_to_see(
	wish_id SERIAL,
	city varchar(50) NOT NULL,
	place_name varchar(50) NOT NULL,
	address varchar(100),
	for_kids boolean,
	completed boolean,
	user_id int NOT NULL,
	CONSTRAINT PK_wishToSee PRIMARY KEY (wish_id),
	CONSTRAINT FK_wishToSee_user FOREIGN KEY (user_id) REFERENCES users(user_id),
	CONSTRAINT UC_wishToSee_city_place_name UNIQUE (city, place_name)
);

INSERT INTO wish_to_see (city, place_name, address, for_kids, completed, user_id) VALUES
    ('Seattle', 'Kids Museum', null, 'true', 'false', 2),
    ('Seattle', '5th Avenue', 'Down Town Seattle', 'false', 'false', 1),
    ('California', 'Disney Land', null, 'true', 'false', 1);

COMMIT TRANSACTION;