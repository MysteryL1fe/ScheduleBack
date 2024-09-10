CREATE TABLE IF NOT EXISTS cabinet (
	id serial PRIMARY KEY,
	cabinet varchar(255) NOT NULL,
	building varchar(255) NOT NULL,
	address varchar(255),
	UNIQUE (cabinet, building)
);