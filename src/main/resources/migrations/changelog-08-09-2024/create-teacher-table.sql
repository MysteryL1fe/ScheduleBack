CREATE TABLE IF NOT EXISTS teacher (
	id serial PRIMARY KEY,
	surname varchar(255) NOT NULL,
	name varchar(255),
	patronymic varchar(255),
	UNIQUE (surname, name, patronymic)
);