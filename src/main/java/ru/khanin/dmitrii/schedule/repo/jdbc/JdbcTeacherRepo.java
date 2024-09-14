package ru.khanin.dmitrii.schedule.repo.jdbc;

import java.util.Map;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Teacher;
import ru.khanin.dmitrii.schedule.repo.TeacherRepo;

@Repository
@RequiredArgsConstructor
public class JdbcTeacherRepo implements TeacherRepo {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Teacher> rowMapper = new DataClassRowMapper<>(Teacher.class);

	@Override
	public Teacher add(Teacher teacher) {
		return jdbcTemplate.queryForObject(
				"INSERT INTO teacher (surname, name, patronymic) VALUES (:surname, :name, :patronymic) RETURNING *",
				new BeanPropertySqlParameterSource(teacher),
				rowMapper
		);
	}

	@Override
	public Optional<Teacher> findById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM teacher WHERE id=:id",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}

	@Override
	public Optional<Teacher> findBySurnameAndNameAndPatronymic(String surname, String name, String patronymic) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM teacher WHERE surname=:surname AND name=:name"
								+ " AND patronymic=:patronymic",
								Map.of(
										"surname", surname,
										"name", name,
										"patronymic", patronymic
								),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<Teacher> findAll() {
		return jdbcTemplate.query(
				"SELECT * FROM teacher",
				rowMapper
		);
	}

	@Override
	public Iterable<Teacher> findAllBySurname(String surname) {
		return jdbcTemplate.query(
				"SELECT * FROM teacher WHERE surname=:surname",
				Map.of("surname", surname),
				rowMapper
		);
	}

	@Override
	public Optional<Teacher> deleteById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"DELETE FROM teacher WHERE id=:id RETURNING *",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<Teacher> deleteAll() {
		return jdbcTemplate.query(
				"DELETE FROM teacher RETURNING *",
				rowMapper
		);
	}

	@Override
	public Iterable<Teacher> findAllWhereSurnameStartsWith(String surname) {
		return jdbcTemplate.query(
				"SELECT * FROM teacher WHERE starts_with(LOWER(surname), LOWER(:surname))",
				Map.of("surname", surname),
				rowMapper
		);
	}

	@Override
	public Iterable<Teacher> findAllWhereFullnameStartsWith(String fullname) {
		return jdbcTemplate.query(
				"SELECT * FROM teacher WHERE starts_with(LOWER(surname || ' ' || name || ' ' || patronymic), LOWER(:fullname))",
				Map.of("fullname", fullname),
				rowMapper
		);
	}

}
