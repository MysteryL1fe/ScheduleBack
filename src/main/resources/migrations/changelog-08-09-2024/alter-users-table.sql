DELETE FROM users;

ALTER TABLE users
	DROP COLUMN api_key,
	DROP COLUMN name,
	DROP COLUMN access,
	DROP COLUMN flow,
	ADD COLUMN login varchar(255) NOT NULL UNIQUE,
	ADD COLUMN password varchar(255) NOT NULL,
	ADD COLUMN admin boolean NOT NULL;