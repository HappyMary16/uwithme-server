CREATE TABLE faculty_department (
	faculty_id INT(11),
    department_id INT(11),

	FOREIGN KEY (faculty_id) REFERENCES faculties(id),
	FOREIGN KEY (department_id) REFERENCES departments(id)
);