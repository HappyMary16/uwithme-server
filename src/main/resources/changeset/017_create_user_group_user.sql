CREATE TABLE user_group_user (
    user_group_id INT(11),
	user_id INT(11),

	FOREIGN KEY (user_group_id) REFERENCES user_groups(id),
	FOREIGN KEY (user_id) REFERENCES users(id)
);