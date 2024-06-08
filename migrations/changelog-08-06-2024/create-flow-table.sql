CREATE TABLE IF NOT EXISTS flow (
	id serial PRIMARY KEY,
	flow_lvl integer NOT NULL CHECK (flow_lvl >= 1 AND flow_lvl <= 3),
	course integer NOT NULL CHECK (course > 0 AND course <= 5),
	flow integer NOT NULL CHECK (flow > 0),
	subgroup integer NOT NULL CHECK (subgroup > 0),
	UNIQUE (flow_lvl, course, flow, subgroup)
);