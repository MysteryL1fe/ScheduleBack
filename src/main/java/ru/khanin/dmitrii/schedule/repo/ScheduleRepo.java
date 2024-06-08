package ru.khanin.dmitrii.schedule.repo;

import java.util.Optional;

import ru.khanin.dmitrii.schedule.entity.Schedule;

public interface ScheduleRepo {
	Schedule add(Schedule schedule);
	Optional<Schedule> findById(long id);
	Optional<Schedule> findByFlowAndDayOfWeekAndLessonNumAndIsNumerator(long flow, int dayOfWeek, int lessonNum, boolean isNumerator);
	Iterable<Schedule> findAll();
	Iterable<Schedule> findAllByFlow(int flow);
	Iterable<Schedule> findAllByFlowAndDayOfWeekAndIsNumerator(long flow, int dayOfWeek, boolean isNumerator);
	Optional<Schedule> deleteById(long id);
	Iterable<Schedule> deleteAll();
}
