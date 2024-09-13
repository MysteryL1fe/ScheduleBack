package ru.khanin.dmitrii.schedule.entity.jdbc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.User;
import ru.khanin.dmitrii.schedule.entity.UserFlow;

@Data
@ToString(callSuper = true)
public class UserFlowJoined extends UserFlow {
	private User userJoined;
	private Flow flowJoined;
}
