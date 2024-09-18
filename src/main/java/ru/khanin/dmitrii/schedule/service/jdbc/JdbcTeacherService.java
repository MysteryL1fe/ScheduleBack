package ru.khanin.dmitrii.schedule.service.jdbc;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Teacher;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcScheduleRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcTeacherRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcTempScheduleRepo;
import ru.khanin.dmitrii.schedule.service.TeacherService;

@RequiredArgsConstructor
public class JdbcTeacherService implements TeacherService {
	private final JdbcTeacherRepo teacherRepo;
	private final JdbcScheduleRepo scheduleRepo;
	private final JdbcTempScheduleRepo tempScheduleRepo;

	@Override
	public Teacher add(String surname) {
		Teacher teacherToAdd = new Teacher();
		teacherToAdd.setSurname(surname);
		return teacherRepo.add(teacherToAdd);
	}

	@Override
	public Teacher add(String surname, String name, String patronymic) {
		Teacher teacherToAdd = new Teacher();
		teacherToAdd.setSurname(surname);
		teacherToAdd.setName(name);
		teacherToAdd.setPatronymic(patronymic);
		return teacherRepo.add(teacherToAdd);
	}

	@Override
	public Teacher findById(long id) {
		return teacherRepo.findById(id).orElseThrow();
	}

	@Override
	public Teacher findBySurnameAndNameAndPatronymic(String surname, String name, String patronymic) {
		return teacherRepo.findBySurnameAndNameAndPatronymic(surname, name, patronymic).orElseThrow();
	}

	@Override
	public Collection<Teacher> findAll() {
		Iterable<Teacher> found = teacherRepo.findAll();
		Collection<Teacher> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Teacher> findAllBySurname(String surname) {
		Iterable<Teacher> found = teacherRepo.findAllBySurname(surname);
		Collection<Teacher> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Teacher> findAllWhereSurnameStartsWith(String surname) {
		Iterable<Teacher> found = teacherRepo.findAllWhereSurnameStartsWith(surname);
		Collection<Teacher> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Teacher> findAllWhereFullnameStartsWith(String fullname) {
		Iterable<Teacher> found = teacherRepo.findAllWhereFullnameStartsWith(fullname);
		Collection<Teacher> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	@Transactional
	public Teacher deleteById(long id) {
		scheduleRepo.deleteAllByTeacher(id);
		tempScheduleRepo.deleteAllByTeacher(id);
		return teacherRepo.deleteById(id).orElseThrow();
	}

	@Override
	public Teacher deleteBySurnameAndNameAndPatronymic(String surname, String name, String patronymic) {
		Teacher found = teacherRepo.findBySurnameAndNameAndPatronymic(surname, name, patronymic).orElseThrow();
		return deleteById(found.getId());
	}

	@Override
	@Transactional
	public Collection<Teacher> deleteAll() {
		scheduleRepo.deleteAllWhereTeacherIsNotNull();
		tempScheduleRepo.deleteAllWhereTeacherIsNotNull();
		Iterable<Teacher> deleted = teacherRepo.deleteAll();
		Collection<Teacher> result = new ArrayList<>();
		deleted.forEach(result::add);
		return result;
	}

}
