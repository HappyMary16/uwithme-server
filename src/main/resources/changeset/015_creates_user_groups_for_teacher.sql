CREATE TABLE user_groups (
    id INT(11) NOT NULL,
	name VARCHAR(50),
	admin_id INT(11) NOT NULL,

	PRIMARY KEY (id),
	UNIQUE INDEX id (id),
	FOREIGN KEY (admin_id) REFERENCES teachers(id)
);