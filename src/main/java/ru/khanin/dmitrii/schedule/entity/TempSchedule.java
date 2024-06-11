package ru.khanin.dmitrii.schedule.entity;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class TempSchedule {
	protected long id;
	protected long flow;
	protected long lesson;
	protected LocalDate lessonDate;
	protected int lessonNum;
	protected boolean willLessonBe;
}
