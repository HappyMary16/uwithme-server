CREATE TABLE subjects (
    id INT(11) NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    description VARCHAR(250),

	PRIMARY KEY (id),
	UNIQUE INDEX id (id)
);