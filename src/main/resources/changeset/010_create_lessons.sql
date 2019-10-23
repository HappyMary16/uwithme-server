CREATE TABLE lessons (
    study_group_id INT(11) NOT NULL,
    day_index INT(2) NOT NULL,
    lesson_index INT(2) NOT NULL,
    subject_id INT(11) NOT NULL,
    teacher_id INT(11),
    week INT(2),

	FOREIGN KEY (study_group_id) REFERENCES study_groups(id),
	FOREIGN KEY (subject_id) REFERENCES subjects(id),
	FOREIGN KEY (teacher_id) REFERENCES teachers(id)
);