create table User(
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	username varchar(255),
	password varchar(255)
)  ENGINE=InnoDB;

CREATE UNIQUE INDEX UK_user_username ON User(username);

create table Role(
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name varchar(255) not null
)  ENGINE=InnoDB;

CREATE UNIQUE INDEX UK_role_name ON Role(name);

create table User_Role(
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id BIGINT NOT NULL,
	role_id BIGINT NOT NULL,
	FOREIGN KEY (user_id) REFERENCES User(id),
	FOREIGN KEY (role_id) REFERENCES Role(id)
)  ENGINE=InnoDB;

