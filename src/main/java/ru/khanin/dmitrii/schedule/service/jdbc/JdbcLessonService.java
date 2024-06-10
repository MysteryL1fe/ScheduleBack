package ru.khanin.dmitrii.schedule.service.jdbc;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Lesson;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcLessonRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcScheduleRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcTempScheduleRepo;
import ru.khanin.dmitrii.schedule.service.LessonService;

@RequiredArgsConstructor
public class JdbcLessonService implements LessonService {
	private final JdbcLessonRepo lessonRepo;
	private final JdbcScheduleRepo scheduleRepo;
	private final JdbcTempScheduleRepo tempScheduleRepo;

	@Override
	public Lesson add(String name, String teacher, String cabinet) {
		Lesson lesson = new Lesson();
		lesson.setName(name);
		lesson.setTeacher(teacher);
		lesson.setCabinet(cabinet);
		return lessonRepo.add(lesson);
	}

	@Override
	public Collection<Lesson> findAll() {
		Iterable<Lesson> found = lessonRepo.findAll();
		Collection<Lesson> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Lesson> findAllByTeacher(String teacher) {
		Iterable<Lesson> found = lessonRepo.findAllByTeacher(teacher);
		Collection<Lesson> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	@Transactional
	public Lesson deleteById(long id) {
		scheduleRepo.deleteAllByLesson(id);
		tempScheduleRepo.deleteAllByLesson(id);
		return lessonRepo.deleteById(id).orElse(null);
	}

	@Override
	@Transactional
	public Collection<Lesson> deleteAll() {
		scheduleRepo.deleteAll();
		tempScheduleRepo.deleteAll();
		Iterable<Lesson> deleted = lessonRepo.deleteAll();
		Collection<Lesson> result = new ArrayList<>();
		deleted.forEach(result::add);
		return result;
	}

}
