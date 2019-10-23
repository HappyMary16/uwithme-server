CREATE TABLE university_faculty (
    educational_institutions_id INT(11),
	faculty_id INT(11),

	FOREIGN KEY (educational_institutions_id) REFERENCES educational_institutions(id),
	FOREIGN KEY (faculty_id) REFERENCES faculties(id)
);