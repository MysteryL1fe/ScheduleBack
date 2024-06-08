CREATE TABLE IF NOT EXISTS homework (
	id serial PRIMARY KEY,
	homework text NOT NULL,
	lesson_date date NOT NULL,
	lesson_num integer NOT NULL CHECK (lesson_num >= 1 AND lesson_num <= 8),
	flow integer NOT NULL,
	lesson_name varchar(255) NOT NULL,
	FOREIGN KEY (flow) REFERENCES flow(id),
	UNIQUE (lesson_date, lesson_num, flow)
);