CREATE TABLE users (
	id INT(11) NOT NULL AUTO_INCREMENT,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	nick VARCHAR(50),
	password VARCHAR(50) NOT NULL,
	study_group INT(11),
	student_id VARCHAR(50),
	educational_institution_id INT(11),
	phone VARCHAR(50),
	email VARCHAR(50),

	PRIMARY KEY (id),
	UNIQUE INDEX id (id),
	FOREIGN KEY (educational_institution_id) REFERENCES educational_institution(id)
)
;

