package ru.khanin.dmitrii.schedule.entity.jdbc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Lesson;
import ru.khanin.dmitrii.schedule.entity.TempSchedule;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class TempScheduleJoined extends TempSchedule {
	private Flow flowJoined;
	private Lesson lessonJoined;
}