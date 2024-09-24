package ru.khanin.dmitrii.schedule.entity;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class TempSchedule {
	protected Long id;
	protected Long flow;
	protected Long subject;
	protected Long teacher;
	protected Long cabinet;
	protected LocalDate lessonDate;
	protected Integer lessonNum;
	protected Boolean willLessonBe;
}
