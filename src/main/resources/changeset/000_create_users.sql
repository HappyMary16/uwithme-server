CREATE TABLE users (
	id INT(11) NOT NULL AUTO_INCREMENT,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	nickname VARCHAR(50),
	password VARCHAR(50) NOT NULL,
	phone VARCHAR(50),
	email VARCHAR(50),

	PRIMARY KEY (id),
	UNIQUE INDEX id (id)
);

