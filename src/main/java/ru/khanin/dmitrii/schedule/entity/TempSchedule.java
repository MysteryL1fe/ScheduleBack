package ru.khanin.dmitrii.schedule.entity;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class TempSchedule {
	private long id;
	private long flow;
	private long lesson;
	private LocalDate lessonDate;
	private int lessonNum;
	private boolean willLessonBe;
}
