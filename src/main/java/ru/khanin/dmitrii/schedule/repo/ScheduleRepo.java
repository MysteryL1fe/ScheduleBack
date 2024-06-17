package ru.khanin.dmitrii.schedule.repo;

import java.util.Optional;

import ru.khanin.dmitrii.schedule.entity.Schedule;

public interface ScheduleRepo {
	Schedule add(Schedule schedule);
	Schedule update(Schedule schedule);
	Optional<Schedule> findById(long id);
	Optional<Schedule> findByFlowAndDayOfWeekAndLessonNumAndNumerator(long flow, int dayOfWeek, int lessonNum, boolean numerator);
	Iterable<? extends Schedule> findAll();
	Iterable<? extends Schedule> findAllByFlow(long flow);
	Iterable<Schedule> findAllByFlowAndDayOfWeekAndNumerator(long flow, int dayOfWeek, boolean numerator);
	Iterable<? extends Schedule> findAllWhereTeacherStartsWith(String teacher);
	Optional<Schedule> deleteById(long id);
	Iterable<Schedule> deleteAll();
	Iterable<Schedule> deleteAllByFlow(long flow);
	Iterable<Schedule> deleteAllByLesson(long lesson);
}
