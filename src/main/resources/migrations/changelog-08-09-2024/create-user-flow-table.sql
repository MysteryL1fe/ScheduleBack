CREATE TABLE IF NOT EXISTS user_flow (
	_user integer NOT NULL,
	flow integer NOT NULL,
	PRIMARY KEY(_user, flow),
	FOREIGN KEY (_user) REFERENCES users(id),
	FOREIGN KEY (flow) REFERENCES flow(id)
);