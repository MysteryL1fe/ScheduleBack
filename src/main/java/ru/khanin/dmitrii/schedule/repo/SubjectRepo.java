package ru.khanin.dmitrii.schedule.repo;

import java.util.Optional;

import ru.khanin.dmitrii.schedule.entity.Subject;

public interface SubjectRepo {
	Subject add(Subject subject);
	Optional<Subject> findById(long id);
	Optional<Subject> findBySubject(String subject);
	Iterable<Subject> findAll();
	Iterable<Subject> findAllWhereSubjectStartsWith(String subject);
	Optional<Subject> deleteById(long id);
	Optional<Subject> deleteBySubject(String subject);
	Iterable<Subject> deleteAll();
}
