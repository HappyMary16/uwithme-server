CREATE TABLE students (
    id INT(11) NOT NULL,
    study_group_id INT(11),
	student_id VARCHAR(50),
	
	PRIMARY KEY (id),
	UNIQUE INDEX id (id),
	FOREIGN KEY (id) REFERENCES users(id),
	FOREIGN KEY (study_group_id) REFERENCES study_groups(id)
);