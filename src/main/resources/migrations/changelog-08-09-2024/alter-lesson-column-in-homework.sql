DELETE FROM homework;

ALTER TABLE homework
	DROP COLUMN IF EXISTS lesson_name,
	ADD COLUMN IF NOT EXISTS subject integer NOT NULL,
	ADD FOREIGN KEY (subject) REFERENCES subject(id);