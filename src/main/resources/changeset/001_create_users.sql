CREATE TABLE `Users` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`first_name` VARCHAR(50) NOT NULL,
	`last_name` VARCHAR(50) NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `id` (`id`),
	UNIQUE INDEX `first_name_last_name` (`first_name`, `last_name`)
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;
