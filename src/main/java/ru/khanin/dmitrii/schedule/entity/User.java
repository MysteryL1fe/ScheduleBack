package ru.khanin.dmitrii.schedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class User {
	protected long id;
	protected String apiKey;
	protected String name;
	protected AccessType access;
	protected long flow;
	
	public enum AccessType {
		admin,
		flow
	}
}
