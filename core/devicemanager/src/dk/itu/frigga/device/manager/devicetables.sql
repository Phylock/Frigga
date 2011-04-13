CREATE TABLE IF NOT EXISTS device_variable
(device_id INTEGER NOT NULL,
variable_id INTEGER NOT NULL,
variable_value BLOB,
PRIMARY KEY (device_id, variable_id));

CREATE TABLE IF NOT EXISTS category_variable
(category_id INTEGER NOT NULL,
variable_id INTEGER NOT NULL,
PRIMARY KEY (category_id, variable_id));

CREATE TABLE IF NOT EXISTS variabletype
(id INTEGER NOT NULL,
varname VARCHAR(25) NOT NULL,
vartype INTEGER NOT NULL,
PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS category
(id INTEGER NOT NULL,
catname VARCHAR(25) UNIQUE NOT NULL,
PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS category_function
(category_id INTEGER NOT NULL,
function_id INTEGER NOT NULL,
PRIMARY KEY (category_id, function_id));

CREATE TABLE IF NOT EXISTS functions
(id INTEGER NOT NULL,
name VARCHAR(25) NOT NULL,
PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS device_category
(device_id INTEGER NOT NULL,
category_id INTEGER NOT NULL,
PRIMARY KEY (device_id, category_id));

CREATE TABLE IF NOT EXISTS device
(id INTEGER NOT NULL,
devname VARCHAR(25) UNIQUE NOT NULL,
symbolic VARCHAR(25) UNIQUE NOT NULL,
last_update TEXT,
online CHAR(1),
PRIMARY KEY (id));