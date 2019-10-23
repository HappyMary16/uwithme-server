CREATE TABLE teachers (
    id INT(11) NOT NULL,
    department_id INT(11),

	PRIMARY KEY (id),
	UNIQUE INDEX id (id),
	FOREIGN KEY (id) REFERENCES users(id),
	FOREIGN KEY (department_id) REFERENCES departments(id)
);