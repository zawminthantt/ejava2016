drop schema if exists authdb;

create schema authdb;

use authdb;

create table users (
	userid varchar(32) not null,
	password varchar(128) not null,
	primary key (userid)
) engine=InnoDB default charset=utf8;

create table groups (
	groupid varchar(32) not null,
	userid varchar(32) not null,
	primary key (groupid, userid)
) engine=InnoDB default charset=utf8;

CREATE TABLE notes (
  noteid int(11) NOT NULL AUTO_INCREMENT,
  userid varchar(32) DEFAULT NULL,
  title varchar(100) DEFAULT NULL,
  category varchar(10) DEFAULT NULL,
  content varchar(255) DEFAULT NULL,
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (noteid),
  UNIQUE KEY noteid_UNIQUE (noteid),
  KEY userid_idx (userid),
  CONSTRAINT userid FOREIGN KEY (userid) REFERENCES users (userid) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
	Create jdbcRealm
	Add jdbc connection pool and jdbc resource
	Assume jdbc resource is jdbc/authdb

	# realm name referenced in web.xml
	name: authdb-realm 
	# hard coded to jdbcRealm
	JAAS context: jdbcRealm (must be this)
	JNDI: jdbc/authdb
	User Table: users
	User Name Column: userid
	Password Column: password
	Group Table: groups
	Group Table user Name Column: userid
	Group Name Column: groupid
	# Cannot find the use of this in the source code.
	# Need to have a value, enter NONE
	Password Encryption Algorithm: AES
	# digest is use to hash password, user the same digest before updating password
	Digest Algorithm: SHA-256 
	Encoding: Hex
*/
