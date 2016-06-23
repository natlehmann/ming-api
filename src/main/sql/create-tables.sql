create table User(
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	username varchar(255) NOT NULL,
	password varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	language varchar(2),
	apiKey varchar(255)
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

create table Report(
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	owner_id BIGINT NOT NULL,
	timePeriod varchar(20) NOT NULL,
	endDate date NOT NULL,
	FOREIGN KEY (owner_id) REFERENCES User(id)
)  ENGINE=InnoDB;

create table ReportItem(
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	trackId varchar(255) NOT NULL,
	trackName varchar(255) NOT NULL,
	artistName varchar(255) NOT NULL,
	labelName varchar(255) NOT NULL,
	totalPlayCount BIGINT,	
	report_id BIGINT NOT NULL,
	FOREIGN KEY (report_id) REFERENCES Report(id)
)  ENGINE=InnoDB;

create table Channel(
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name varchar(255) NOT NULL,
	keyname varchar(255) NOT NULL
)  ENGINE=InnoDB;

create table PlaycountByChannel(
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	channel_id BIGINT NOT NULL,
	playcount BIGINT NOT NULL default 0,
	reportItem_id BIGINT NOT NULL,
	FOREIGN KEY (channel_id) REFERENCES Channel(id),
	FOREIGN KEY (reportItem_id) REFERENCES ReportItem(id)
)  ENGINE=InnoDB;