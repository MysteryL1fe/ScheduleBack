package ru.khanin.dmitrii.schedule.repo;

import java.util.Optional;

import ru.khanin.dmitrii.schedule.entity.Schedule;

public interface ScheduleRepo {
	Schedule add(Schedule schedule);
	Schedule update(Schedule schedule);
	Optional<Schedule> findById(long id);
	Optional<Schedule> findByFlowAndDayOfWeekAndLessonNumAndIsNumerator(long flow, int dayOfWeek, int lessonNum, boolean isNumerator);
	Iterable<? extends Schedule> findAll();
	Iterable<? extends Schedule> findAllByFlow(long flow);
	Iterable<Schedule> findAllByFlowAndDayOfWeekAndIsNumerator(long flow, int dayOfWeek, boolean isNumerator);
	Iterable<? extends Schedule> findAllWhereTeacherStartsWith(String teacher);
	Optional<Schedule> deleteById(long id);
	Iterable<Schedule> deleteAll();
	Iterable<Schedule> deleteAllByFlow(long flow);
	Iterable<Schedule> deleteAllByLesson(long lesson);
}
