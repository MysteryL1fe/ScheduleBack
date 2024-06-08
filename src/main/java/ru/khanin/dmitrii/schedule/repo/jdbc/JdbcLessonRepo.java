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
import ru.khanin.dmitrii.schedule.entity.Lesson;
import ru.khanin.dmitrii.schedule.repo.LessonRepo;

@Repository
@RequiredArgsConstructor
public class JdbcLessonRepo implements LessonRepo {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Lesson> rowMapper = new DataClassRowMapper<>(Lesson.class);

	@Override
	public Lesson add(Lesson lesson) {
		return jdbcTemplate.queryForObject(
				"INSERT INTO lesson(name, teacher, cabinet) VALUES (:name, :teacher, :cabinet) RETURNING *",
				new BeanPropertySqlParameterSource(lesson),
				rowMapper
		);
	}

	@Override
	public Optional<Lesson> findById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM lesson WHERE id=:id",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Optional<Lesson> findByNameAndTeacherAndCabinet(String name, String teacher, String cabinet) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM lesson WHERE name like :name AND teacher like :teacher AND cabinet like :cabinet",
								Map.of(
										"name", name,
										"teacher", teacher,
										"cabinet", cabinet
								),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<Lesson> findAll() {
		return jdbcTemplate.query(
				"SELECT * FROM lesson",
				rowMapper
		);
	}
	
	@Override
	public Iterable<Lesson> findAllByTeacher(String teacher) {
		return jdbcTemplate.query(
				"SELECT * FROM lesson WHERE teacher ilike :teacher",
				Map.of("teacher", teacher),
				rowMapper
		);
	}

	@Override
	public Optional<Lesson> deleteById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"DELETE FROM lesson WHERE id=:id RETURNING *",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Iterable<Lesson> deleteAll() {
		return jdbcTemplate.query(
				"DELETE FROM lesson RETURNING *",
				rowMapper
		);
	}

}
