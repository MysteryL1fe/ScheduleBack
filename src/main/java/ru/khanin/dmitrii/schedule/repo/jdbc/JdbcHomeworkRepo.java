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
				"INSERT INTO homework(homework, lesson_date, lesson_num, flow, subject)"
				+ " VALUES (:homework, :lessonDate, :lessonNum, :flow, :subject) RETURNING *",
				new BeanPropertySqlParameterSource(homework),
				rowMapper
		);
	}
	
	@Override
	public Homework update(Homework homework) {
		return jdbcTemplate.queryForObject(
				"UPDATE homework SET homework=:homework, subject=:subject"
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
								"SELECT * FROM homework"
								+ " WHERE lesson_date=:lessonDate AND lesson_num=:lessonNum AND flow=:flow",
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
				"SELECT h.id AS homework_id, h.homework, h.lesson_date, h.lesson_num, h.flow AS flow_id,"
				+ " h.subject AS subject_id, f.education_level, f.course, f._group, f.subgroup, f.last_edit,"
				+ " f.lessons_start_date, f.session_start_date, f.session_end_date, f.active, s.subject"
				+ " FROM homework h JOIN flow f ON h.flow=f.id JOIN subject s ON h.subject = s.id",
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

	@Override
	public Iterable<Homework> deleteAllBySubject(long subject) {
		return jdbcTemplate.query(
				"DELETE FROM homework WHERE subject=:subject RETURNING *",
				Map.of("subject", subject),
				rowMapper
		);
	}

	@Override
	public Optional<Homework> deleteByFlowAndLessonDateAndLessonNum(long flow, LocalDate lessonDate, int lessonNum) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"DELETE FROM homework"
								+ " WHERE flow=:flow AND lesson_date=:lessonDate AND lesson_num=:lessonNum"
								+ " RETURNING *",
								Map.of(
										"flow", flow,
										"lessonDate", lessonDate,
										"lessonNum", lessonNum
								),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<Homework> findAllByFlowAndSubject(long flow, long subject) {
		return jdbcTemplate.query(
				"SELECT * FROM homework WHERE flow=:flow AND subject=:subject",
				Map.of(
						"flow", flow,
						"subject", subject
				),
				rowMapper
		);
	}
	
}
