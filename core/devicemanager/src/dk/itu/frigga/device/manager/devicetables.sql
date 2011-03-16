CREATE TABLE variable
(device_id INTEGER NOT NULL,
name VARCHAR(25) NOT NULL,
type INTEGER NOT NULL,
value BLOB,
PRIMARY KEY (device_id, name));

CREATE TABLE category
(id INTEGER NOT NULL,
name VARCHAR(25) NOT NULL,
PRIMARY KEY (id));

CREATE TABLE functions
(device_id INTEGER NOT NULL,
name VARCHAR(25) NOT NULL,
PRIMARY KEY (device_id, name));

CREATE TABLE devicecategory
(device_id INTEGER NOT NULL,
category_id INTEGER NOT NULL,
PRIMARY KEY (device_id, category_id));

CREATE TABLE device
(id INTEGER NOT NULL,
name VARCHAR(25),
symbolic_name VARCHAR(25) NOT NULL,
last_update VARCHAR(25),
online CHAR(1),
PRIMARY KEY (id));