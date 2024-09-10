DELETE FROM schedule;

ALTER TABLE schedule
	DROP COLUMN IF EXISTS lesson,
	ADD COLUMN IF NOT EXISTS subject integer NOT NULL,
	ADD COLUMN IF NOT EXISTS teacher integer,
	ADD COLUMN IF NOT EXISTS cabinet integer,
	ADD FOREIGN KEY (subject) REFERENCES subject(id),
	ADD FOREIGN KEY (teacher) REFERENCES teacher(id),
	ADD FOREIGN KEY (cabinet) REFERENCES cabinet(id);