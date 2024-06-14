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
import ru.khanin.dmitrii.schedule.entity.jdbc.HomeworkJoined;
import ru.khanin.dmitrii.schedule.repo.HomeworkRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.mapper.HomeworkJoinedRowMapper;

@Repository
@RequiredArgsConstructor
public class JdbcHomeworkRepo implements HomeworkRepo {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Homework> rowMapper = new DataClassRowMapper<>(Homework.class);
	private final RowMapper<HomeworkJoined> joinedRowMapper = new HomeworkJoinedRowMapper();
	
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
	public Homework update(Homework homework) {
		return jdbcTemplate.queryForObject(
				"UPDATE homework SET homework = :homework, lesson_name = :lessonName"
				+ " WHERE lesson_date=:lessonDate AND lesson_num=:lessonNum AND flow=:flow"
				+ " RETURNING *",
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
	public Iterable<? extends Homework> findAll() {
		return jdbcTemplate.query(
				"SELECT h.id AS homework_id, h.homework, h.lesson_date, h.lesson_num, h.flow AS flow_id, h.lesson_name,"
				+ " f.flow_lvl, f.course, f.flow, f.subgroup, f.last_edit"
				+ " FROM homework h JOIN flow f ON h.flow=f.id",
				joinedRowMapper
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

	@Override
	public Iterable<Homework> deleteAllBeforeDate(LocalDate date) {
		return jdbcTemplate.query(
				"DELETE FROM homework WHERE lesson_date<:date RETURNING *",
				Map.of("date", date),
				rowMapper
		);
	}
	
	@Override
	public Iterable<Homework> deleteAllByFlow(long flow) {
		return jdbcTemplate.query(
				"DELETE FROM homework WHERE flow=:flow RETURNING *",
				Map.of("flow", flow),
				rowMapper
		);
	}
	
}
