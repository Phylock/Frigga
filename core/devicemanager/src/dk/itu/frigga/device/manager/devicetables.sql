CREATE TABLE IF NOT EXISTS device_variable
(
  device_id INTEGER NOT NULL,
  variable_id INTEGER NOT NULL,
  variable_value TEXT,
  PRIMARY KEY (device_id, variable_id)
);

CREATE TABLE IF NOT EXISTS position_local
(
  device_id INTEGER NOT NULL,
  room VARCHAR(25) NOT NULL,
  sender VARCHAR(64) NOT NULL,
  pos_x DOUBLE DEFAULT 0,
  pos_y DOUBLE DEFAULT 0,
  pos_z DOUBLE DEFAULT 0,
  vel_x DOUBLE DEFAULT 0,
  vel_y DOUBLE DEFAULT 0,
  vel_z DOUBLE DEFAULT 0,
  updated DATETIME,
  PRIMARY KEY (device_id, room)
);

CREATE TABLE IF NOT EXISTS position_global
(
  device_id INTEGER NOT NULL,
  pos_x DOUBLE DEFAULT 0,
  pos_y DOUBLE DEFAULT 0,
  pos_z DOUBLE DEFAULT 0,
  vel_x DOUBLE DEFAULT 0,
  vel_y DOUBLE DEFAULT 0,
  vel_z DOUBLE DEFAULT 0,
  updated DATETIME,
  PRIMARY KEY (device_id)
);

CREATE TABLE IF NOT EXISTS category_variable
(
  category_id INTEGER NOT NULL,
  variable_id INTEGER NOT NULL,
  PRIMARY KEY (category_id, variable_id)
);

CREATE TABLE IF NOT EXISTS variabletype
(
  id INTEGER NOT NULL,
  varname VARCHAR(25) NOT NULL,
  vartype VARCHAR(25) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS category
(
  id INTEGER NOT NULL,
  catname VARCHAR(25) UNIQUE NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS category_function
(
  category_id INTEGER NOT NULL,
  function_id INTEGER NOT NULL,
  PRIMARY KEY (category_id, function_id)
);

CREATE TABLE IF NOT EXISTS functions
(
  id INTEGER NOT NULL,
  name VARCHAR(25) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS function_param
(
  function_id INTEGER NOT NULL,
  name VARCHAR(25) NOT NULL,
  type VARCHAR(25) NOT NULL,
  PRIMARY KEY (function_id, name)
);

CREATE TABLE IF NOT EXISTS device_category
(
  device_id INTEGER NOT NULL,
  category_id INTEGER NOT NULL,
  PRIMARY KEY (device_id, category_id)
);

CREATE TABLE IF NOT EXISTS device
(
  id INTEGER NOT NULL,
  devname VARCHAR(128) NOT NULL,
  symbolic VARCHAR(128) UNIQUE NOT NULL,
  last_update DATETIME,
  online CHAR(1),
  driver VARCHAR(64) NOT NULL,
  PRIMARY KEY (id)
);
