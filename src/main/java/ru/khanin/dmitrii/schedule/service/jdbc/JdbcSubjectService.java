package ru.khanin.dmitrii.schedule.service.jdbc;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Subject;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcHomeworkRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcScheduleRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcSubjectRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcTempScheduleRepo;
import ru.khanin.dmitrii.schedule.service.SubjectService;

@RequiredArgsConstructor
public class JdbcSubjectService implements SubjectService {
	private final JdbcSubjectRepo subjectRepo;
	private final JdbcScheduleRepo scheduleRepo;
	private final JdbcTempScheduleRepo tempScheduleRepo;
	private final JdbcHomeworkRepo homeworkRepo;

	@Override
	public Subject add(String subject) {
		Subject subjectToAdd = new Subject();
		subjectToAdd.setSubject(subject);
		return subjectRepo.add(subjectToAdd);
	}

	@Override
	public Subject findById(long id) {
		return subjectRepo.findById(id).orElseThrow();
	}

	@Override
	public Subject findBySubject(String subject) {
		return subjectRepo.findBySubject(subject).orElseThrow();
	}

	@Override
	public Collection<Subject> findAll() {
		Iterable<Subject> found = subjectRepo.findAll();
		Collection<Subject> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Subject> findAllWhereSubjectStartsWith(String subject) {
		Iterable<Subject> found = subjectRepo.findAllWhereSubjectStartsWith(subject);
		Collection<Subject> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	@Transactional
	public Subject deleteById(long id) {
		scheduleRepo.deleteAllBySubject(id);
		tempScheduleRepo.deleteAllBySubject(id);
		homeworkRepo.deleteAllBySubject(id);
		return subjectRepo.deleteById(id).orElseThrow();
	}

	@Override
	public Subject deleteBySubject(String subject) {
		Subject found = subjectRepo.findBySubject(subject).orElseThrow();
		return deleteById(found.getId());
	}

	@Override
	@Transactional
	public Collection<Subject> deleteAll() {
		scheduleRepo.deleteAll();
		tempScheduleRepo.deleteAll();
		homeworkRepo.deleteAll();
		Iterable<Subject> deleted = subjectRepo.deleteAll();
		Collection<Subject> result = new ArrayList<>();
		deleted.forEach(result::add);
		return result;
	}

}
