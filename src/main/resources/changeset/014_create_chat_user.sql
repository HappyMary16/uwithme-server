CREATE TABLE chat_user (
    chat_id INT(11),
	user_id INT(11),

	FOREIGN KEY (chat_id) REFERENCES chats(id),
	FOREIGN KEY (user_id) REFERENCES users(id)
);