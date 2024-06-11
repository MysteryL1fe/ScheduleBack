CREATE TABLE IF NOT EXISTS temp_schedule (
	id serial PRIMARY KEY,
	flow integer NOT NULL,
	lesson integer NOT NULL,
	lesson_date date NOT NULL,
	lesson_num integer NOT NULL CHECK (lesson_num >= 1 AND lesson_num <= 8),
	will_lesson_be boolean NOT NULL,
	FOREIGN KEY (flow) REFERENCES flow(id),
	FOREIGN KEY (lesson) REFERENCES lesson(id),
	UNIQUE (flow, lesson_date, lesson_num)
);