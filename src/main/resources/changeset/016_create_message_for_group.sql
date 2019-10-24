CREATE TABLE messages_for_user_group (
    id INT(11) NOT NULL,
	author_id INT(11) NOT NULL,
	user_group_id INT(11) NOT NULL,
	message BLOB NOT NULL,
	sent_date DATE,

	PRIMARY KEY (id),
	UNIQUE INDEX id (id),
	FOREIGN KEY (author_id) REFERENCES users(id),
	FOREIGN KEY (user_group_id) REFERENCES user_groups(id)
);