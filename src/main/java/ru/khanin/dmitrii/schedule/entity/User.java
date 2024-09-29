package ru.khanin.dmitrii.schedule.entity;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class User {
	protected Long id;
	protected String login;
	protected String password;
	protected Boolean admin;
	protected List<Flow> flows;
}
