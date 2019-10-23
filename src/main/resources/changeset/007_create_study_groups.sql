CREATE TABLE study_groups (
	id INT(11) NOT NULL AUTO_INCREMENT,
	group_name VARCHAR(50) NOT NULL,
	department_id INT(11),
	course INT(2),

	PRIMARY KEY (id),
	UNIQUE INDEX id (id),
	FOREIGN KEY (department_id) REFERENCES departments(id)
);