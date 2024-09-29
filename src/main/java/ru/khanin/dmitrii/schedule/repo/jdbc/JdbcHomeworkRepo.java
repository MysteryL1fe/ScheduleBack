package ru.khanin.dmitrii.schedule.repo.jdbc;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Homework;
import ru.khanin.dmitrii.schedule.repo.HomeworkRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.mapper.HomeworkRowMapper;

@Repository
@RequiredArgsConstructor
public class JdbcHomeworkRepo implements HomeworkRepo {
	private final String SELECT_COLUMNS = "h.id AS homework_id, h.homework, h.lesson_date, h.lesson_num,"
			+ " h.flow AS flow_id, h.subject AS subject_id, f.education_level, f.course, f._group, f.subgroup,"
			+ " f.last_edit, f.lessons_start_date, f.session_start_date, f.session_end_date, f.active, s.subject";
	
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Homework> rowMapper = new HomeworkRowMapper();
	
	@Override
	public Homework add(Homework homework) {
		return jdbcTemplate.queryForObject(
				"WITH h AS (INSERT INTO homework(homework, lesson_date, lesson_num, flow, subject)"
						+ " VALUES (:homework, :lessonDate, :lessonNum, :flow, :subject) RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + "FROM h"
						+ " JOIN flow f ON h.flow = f.id JOIN subject s ON h.subject = s.id",
				Map.of(
						"homework", homework.getHomework(),
						"lessonDate", homework.getLessonDate(),
						"lessonNum", homework.getLessonNum(),
						"flow", homework.getFlow().getId(),
						"subject", homework.getSubject().getId()
				),
				rowMapper
		);
	}
	
	@Override
	public Homework update(Homework homework) {
		return jdbcTemplate.queryForObject(
				"WITH h AS (UPDATE homework SET homework = :homework, subject = :subject"
						+ " WHERE lesson_date = :lessonDate AND lesson_num = :lessonNum"
						+ " AND flow = :flow) RETURNING *)"
						+ " SELECT" + SELECT_COLUMNS + "FROM h"
						+ " JOIN flow f ON h.flow = f.id JOIN subject s ON h.subject = s.id",
				Map.of(
						"homework", homework.getHomework(),
						"lessonDate", homework.getLessonDate(),
						"lessonNum", homework.getLessonNum(),
						"flow", homework.getFlow().getId(),
						"subject", homework.getSubject().getId()
				),
				rowMapper
		);
	}

	@Override
	public Optional<Homework> findById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT " + SELECT_COLUMNS + " FROM homework h"
										+ " JOIN flow f ON h.flow = f.id JOIN subject s ON h.subject = s.id"
										+ " WHERE h.id = :id",
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
								"SELECT " + SELECT_COLUMNS + " FROM homework h"
										+ " JOIN flow f ON h.flow = f.id JOIN subject s ON h.subject = s.id"
										+ " WHERE lesson_date=:lessonDate AND lesson_num=:lessonNum"
										+ " AND flow=:flow",
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
				"SELECT " + SELECT_COLUMNS + " FROM homework h"
						+ " JOIN flow f ON h.flow = f.id JOIN subject s ON h.subject = s.id",
				rowMapper
		);
	}
	
	@Override
	public Iterable<Homework> findAllByFlow(long flow) {
		return jdbcTemplate.query(
				"SELECT " + SELECT_COLUMNS + " FROM homework h"
						+ " JOIN flow f ON h.flow = f.id JOIN subject s ON h.subject = s.id"
						+ " WHERE flow=:flow",
				Map.of("flow", flow),
				rowMapper
		);
	}
	
	@Override
	public Iterable<Homework> findAllByLessonDateAndFlow(LocalDate lessonDate, long flow) {
		return jdbcTemplate.query(
				"SELECT " + SELECT_COLUMNS + " FROM homework h"
						+ " JOIN flow f ON h.flow = f.id JOIN subject s ON h.subject = s.id"
						+ " WHERE lesson_date=:lessonDate AND flow=:flow",
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
								"WITH h AS (DELETE FROM homework WHERE id=:id RETURNING *)"
										+ " SELECT " + SELECT_COLUMNS + " FROM h"
										+ " JOIN flow f ON h.flow = f.id JOIN subject s ON h.subject = s.id",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}
		
	@Override
	public Iterable<Homework> deleteAll() {
		return jdbcTemplate.query(
				"WITH h AS (DELETE FROM homework RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM h"
						+ " JOIN flow f ON h.flow = f.id JOIN subject s ON h.subject = s.id",
				rowMapper
		);
	}

	@Override
	public Iterable<Homework> deleteAllBeforeDate(LocalDate date) {
		return jdbcTemplate.query(
				"WITH h AS (DELETE FROM homework WHERE lesson_date < :date RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM h"
						+ " JOIN flow f ON h.flow = f.id JOIN subject s ON h.subject = s.id",
				Map.of("date", date),
				rowMapper
		);
	}
	
	@Override
	public Iterable<Homework> deleteAllByFlow(long flow) {
		return jdbcTemplate.query(
				"WITH h AS (DELETE FROM homework WHERE flow=:flow RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM h"
						+ " JOIN flow f ON h.flow = f.id JOIN subject s ON h.subject = s.id",
				Map.of("flow", flow),
				rowMapper
		);
	}

	@Override
	public Iterable<Homework> deleteAllBySubject(long subject) {
		return jdbcTemplate.query(
				"WITH h AS (DELETE FROM homework WHERE subject=:subject RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + "FROM h"
						+ " JOIN flow f ON h.flow = f.id JOIN subject s ON h.subject = s.id",
				Map.of("subject", subject),
				rowMapper
		);
	}

	@Override
	public Optional<Homework> deleteByFlowAndLessonDateAndLessonNum(long flow, LocalDate lessonDate, int lessonNum) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"WITH h AS (DELETE FROM homework WHERE flow=:flow AND lesson_date=:lessonDate"
										+ " AND lesson_num=:lessonNum RETURNING *)"
										+ " SELECT " + SELECT_COLUMNS + " FROM h"
										+ " JOIN flow f ON h.flow = f.id JOIN subject s ON h.subject = s.id",
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
				"SELECT " + SELECT_COLUMNS + " FROM h"
						+ " JOIN flow f ON h.flow = f.id JOIN subject s ON h.subject = s.id"
						+ " WHERE flow=:flow AND subject=:subject",
				Map.of(
						"flow", flow,
						"subject", subject
				),
				rowMapper
		);
	}
	
}
