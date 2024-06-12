CREATE TYPE access_type AS ENUM (
	'admin',
	'flow'
);

CREATE TABLE IF NOT EXISTS users {
	id serial PRIMARY KEY,
	api_key varchar(255) NOT NULL,
	name varchar(255) NOT NULL,
	access access_type NOT NULL,
	flow int CHECK (access='admin' OR flow IS NOT NULL),
	FOREIGN KEY (flow) REFERENCES flow(id)
};