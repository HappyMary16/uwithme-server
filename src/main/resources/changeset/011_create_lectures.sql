CREATE TABLE lectures (
    id INT(11) NOT NULL AUTO_INCREMENT,
    topic VARCHAR(50) NOT NULL,
    lecture BLOB,
    subject_id INT(11) NOT NULL,
    author_id INT(11),

	PRIMARY KEY (id),
	UNIQUE INDEX id (id),
	FOREIGN KEY (subject_id) REFERENCES subjects(id),
	FOREIGN KEY (author_id) REFERENCES users(id)
);