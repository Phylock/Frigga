CREATE TABLE IF NOT EXISTS variables
(device_id INTEGER NOT NULL,
name VARCHAR(25) NOT NULL,
type INTEGER NOT NULL,
value BLOB,
PRIMARY KEY (device_id, name));

CREATE TABLE IF NOT EXISTS category
(id INTEGER NOT NULL,
name VARCHAR(25) NOT NULL,
PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS categoryfunction
(category_id INTEGER NOT NULL,
function_id INTEGER NOT NULL,
PRIMARY KEY (category_id, function_id));

CREATE TABLE IF NOT EXISTS functions
(id INTEGER NOT NULL,
name VARCHAR(25) NOT NULL,
PRIMARY KEY (id, name));

CREATE TABLE IF NOT EXISTS devicecategory
(device_id INTEGER NOT NULL,
category_id INTEGER NOT NULL,
PRIMARY KEY (device_id, category_id));

CREATE TABLE IF NOT EXISTS device
(id INTEGER NOT NULL,
name VARCHAR(25),
symbolic_name VARCHAR(25) NOT NULL,
last_update VARCHAR(25),
online CHAR(1),
PRIMARY KEY (id));