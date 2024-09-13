package ru.khanin.dmitrii.schedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Cabinet {
	protected long id;
	protected String cabinet;
	protected String building;
	protected String address;
}
