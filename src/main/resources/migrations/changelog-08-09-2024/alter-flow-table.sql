ALTER TABLE flow RENAME COLUMN flow_lvl TO education_level;
ALTER TABLE flow RENAME COLUMN flow TO _group;
ALTER TABLE flow
	ALTER COLUMN lessons_start_date DROP NOT NULL,
	ALTER COLUMN lessons_start_date DROP DEFAULT,
	ALTER COLUMN session_start_date DROP NOT NULL,
	ALTER COLUMN session_start_date DROP DEFAULT,
	ALTER COLUMN session_end_date DROP NOT NULL,
	ALTER COLUMN session_end_date DROP DEFAULT;
ALTER TABLE flow DROP CONSTRAINT flow_course_check;
ALTER TABLE flow ADD CHECK(course > 0);