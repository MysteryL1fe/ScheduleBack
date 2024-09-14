package ru.khanin.dmitrii.schedule.repo;

import java.util.Optional;

import ru.khanin.dmitrii.schedule.entity.Teacher;

public interface TeacherRepo {
	Teacher add(Teacher teacher);
	Optional<Teacher> findById(long id);
	Optional<Teacher> findBySurnameAndNameAndPatronymic(String surname, String name, String patronymic);
	Iterable<Teacher> findAll();
	Iterable<Teacher> findAllBySurname(String surname);
	Iterable<Teacher> findAllWhereSurnameStartsWith(String surname);
	Iterable<Teacher> findAllWhereFullnameStartsWith(String fullname);
	Optional<Teacher> deleteById(long id);
	Iterable<Teacher> deleteAll();
}
