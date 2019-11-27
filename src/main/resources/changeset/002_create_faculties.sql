-- CREATE TABLE faculties (
-- 	id INT(11) NOT NULL AUTO_INCREMENT,
-- 	name VARCHAR(50) NOT NULL,
--
-- 	PRIMARY KEY (id),
-- 	UNIQUE INDEX id (id)
-- );
CREATE TABLE faculties
(
    id   SERIAL,
    name VARCHAR(50) NOT NULL,

    PRIMARY KEY (id)
);