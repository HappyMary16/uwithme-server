create TABLE study_gruop (
	id INT(11) NOT NULL AUTO_INCREMENT,
	group_name VARCHAR(50) NOT NULL,
	educational_institution_id INT(11),
	faculty INT(11),
	cafedra INT(11),
	course INT(2),

	PRIMARY KEY (id),
	UNIQUE INDEX id (id),
	FOREIGN KEY (educational_institution_id) REFERENCES educational_institution(id)
)
;