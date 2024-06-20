package ru.khanin.dmitrii.schedule.entity.jdbc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Homework;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = "id", callSuper = false)
public class HomeworkJoined extends Homework {
	private Flow joinedFlow;
}
