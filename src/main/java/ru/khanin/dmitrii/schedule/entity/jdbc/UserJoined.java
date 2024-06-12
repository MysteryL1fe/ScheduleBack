package ru.khanin.dmitrii.schedule.entity.jdbc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.User;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class UserJoined extends User {
	private Flow flowJoined;
}
