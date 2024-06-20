package ru.khanin.dmitrii.schedule.entity.jdbc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Lesson;
import ru.khanin.dmitrii.schedule.entity.TempSchedule;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = "id", callSuper = false)
public class TempScheduleJoined extends TempSchedule {
	private Flow flowJoined;
	private Lesson lessonJoined;
}
