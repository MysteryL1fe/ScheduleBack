CREATE TABLE IF NOT EXISTS schedule (
	id serial PRIMARY KEY,
	flow integer NOT NULL,
	lesson integer NOT NULL,
	day_of_week integer NOT NULL CHECK (day_of_week >= 1 AND day_of_week <= 7),
	lesson_num integer NOT NULL CHECK (lesson_num >= 1 AND lesson_num <= 8),
	numerator boolean NOT NULL,
	FOREIGN KEY (flow) REFERENCES flow(id),
	FOREIGN KEY (lesson) REFERENCES lesson(id),
	UNIQUE (flow, day_of_week, lesson_num, numerator)
);