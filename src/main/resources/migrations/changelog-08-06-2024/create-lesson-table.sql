CREATE TABLE IF NOT EXISTS lesson (
	id serial PRIMARY KEY,
	name varchar(255) NOT NULL,
	teacher varchar(255),
	cabinet varchar(255),
	UNIQUE (name, teacher, cabinet)
);