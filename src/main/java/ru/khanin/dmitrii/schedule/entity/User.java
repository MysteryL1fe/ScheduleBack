package ru.khanin.dmitrii.schedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class User {
	protected long id;
	protected String login;
	protected String password;
	protected boolean admin;
}
