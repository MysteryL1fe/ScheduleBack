package ru.khanin.dmitrii.schedule.entity;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class TempSchedule {
	protected Long id;
	protected Flow flow;
	protected Subject subject;
	protected Teacher teacher;
	protected Cabinet cabinet;
	protected LocalDate lessonDate;
	protected Integer lessonNum;
	protected Boolean willLessonBe;
}
