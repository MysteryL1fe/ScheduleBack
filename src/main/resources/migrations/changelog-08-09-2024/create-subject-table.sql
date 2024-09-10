CREATE TABLE IF NOT EXISTS subject (
	id serial PRIMARY KEY,
	subject varchar(255) NOT NULL UNIQUE
);