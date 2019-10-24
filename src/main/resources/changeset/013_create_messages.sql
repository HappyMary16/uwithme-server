CREATE TABLE messages (
    id INT(11) NOT NULL,
	author_id INT(11) NOT NULL,
	chat_id INT(11) NOT NULL,
	message BLOB NOT NULL,
	sent_date DATE,
	
	PRIMARY KEY (id),
	UNIQUE INDEX id (id),
	FOREIGN KEY (author_id) REFERENCES users(id),
	FOREIGN KEY (chat_id) REFERENCES chats(id)
);