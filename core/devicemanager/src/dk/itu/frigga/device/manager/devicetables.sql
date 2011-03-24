CREATE TABLE IF NOT EXISTS devicevariable
(device_id INTEGER NOT NULL,
variable_id INTEGER NOT NULL,
variable_value BLOB,
PRIMARY KEY (device_id, variable_id));

CREATE TABLE IF NOT EXISTS categoryvariable
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
name VARCHAR(25) UNIQUE NOT NULL,
symbolic VARCHAR(25) UNIQUE NOT NULL,
last_update VARCHAR(25),
online CHAR(1),
PRIMARY KEY (id));