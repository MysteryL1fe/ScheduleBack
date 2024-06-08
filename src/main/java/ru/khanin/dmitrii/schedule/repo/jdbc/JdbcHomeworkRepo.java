package ru.khanin.dmitrii.schedule.repo.jdbc;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Homework;
import ru.khanin.dmitrii.schedule.repo.HomeworkRepo;

@Repository
@RequiredArgsConstructor
public class JdbcHomeworkRepo implements HomeworkRepo {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Homework> rowMapper = new DataClassRowMapper<>(Homework.class);
	
	@Override
	public Homework add(Homework homework) {
		return jdbcTemplate.queryForObject(
				"INSERT INTO homework(homework, lesson_date, lesson_num, flow, lesson_name)"
				+ " VALUES (:homework, :lessonDate, :lessonNum, :flow, :lessonName) RETURNING *",
				new BeanPropertySqlParameterSource(homework),
				rowMapper
		);
	}

	@Override
	public Optional<Homework> findById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM homework WHERE id=:id",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Optional<Homework> findByLessonDateAndLessonNumAndFlow(LocalDate lessonDate, int lessonNum, long flow) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM homework WHERE lesson_date=:lessonDate AND lesson_num=:lessonNum AND flow=:flow",
								Map.of(
										"lessonDate", lessonDate,
										"lessonNum", lessonNum,
										"flow", flow
								),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<Homework> findAll() {
		return jdbcTemplate.query(
				"SELECT * FROM homework",
				rowMapper
		);
	}
	
	@Override
	public Iterable<Homework> findAllByFlow(long flow) {
		return jdbcTemplate.query(
				"SELECT * FROM homework WHERE flow=:flow",
				Map.of("flow", flow),
				rowMapper
		);
	}
	
	@Override
	public Iterable<Homework> findAllByLessonDateAndFlow(LocalDate lessonDate, long flow) {
		return jdbcTemplate.query(
				"SELECT * FROM homework WHERE lesson_date=:lessonDate AND flow=:flow",
				Map.of(
						"lessonDate", lessonDate,
						"flow", flow
				),
				rowMapper
		);
	}

	@Override
	public Optional<Homework> deleteById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"DELETE FROM homework WHERE id=:id RETURNING *",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Iterable<Homework> deleteAll() {
		return jdbcTemplate.query(
				"DELETE FROM homework RETURNING *",
				rowMapper
		);
	}

}
