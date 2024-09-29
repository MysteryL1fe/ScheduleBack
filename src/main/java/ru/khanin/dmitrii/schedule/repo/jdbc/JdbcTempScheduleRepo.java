package ru.khanin.dmitrii.schedule.repo.jdbc;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.TempSchedule;
import ru.khanin.dmitrii.schedule.repo.TempScheduleRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.mapper.TempScheduleRowMapper;

@Repository
@RequiredArgsConstructor
public class JdbcTempScheduleRepo implements TempScheduleRepo {
	private final String SELECT_COLUMNS = "temp.id AS temp_schedule_id, temp.lesson_date, temp.lesson_num, temp.will_lesson_be,"
			+ " temp.flow AS flow_id, temp.subject AS subject_id, temp.teacher AS teacher_id, temp.cabinet AS cabinet,"
			+ " f.education_level, f.course, f._group, f.subgroup,"
			+ " f.last_edit, f.lessons_start_date, f.session_start_date, f.session_end_date, f.active,"
			+ " sub.subject, t.surname, t.name, t.patronymic, c.cabinet, c.building, c.address";
	
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<TempSchedule> rowMapper = new TempScheduleRowMapper();
	
	@Override
	public TempSchedule add(TempSchedule tempSchedule) {
		return jdbcTemplate.queryForObject(
				"WITH temp AS (INSERT INTO temp_schedule("
						+ "flow, subject, teacher, cabinet, lesson_date, lesson_num, will_lesson_be) VALUES"
						+ " (:flow, :subject, :teacher, :cabinet, :lessonDate, :lessonNum, :willLessonBe) RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM temp"
						+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
						+ " JOIN teacher t ON temp.teacher = t.id JOIN cabinet c ON temp.cabinet = c.id",
				Map.of(
						"flow", tempSchedule.getFlow().getId(),
						"subject", tempSchedule.getSubject().getId(),
						"teacher", tempSchedule.getTeacher().getId(),
						"cabinet", tempSchedule.getCabinet().getId(),
						"lessonDate", tempSchedule.getLessonDate(),
						"lessonNum", tempSchedule.getLessonNum(),
						"willLessonBe", tempSchedule.getWillLessonBe()
				),
				rowMapper
		);
	}
	
	@Override
	public TempSchedule update(TempSchedule tempSchedule) {
		return jdbcTemplate.queryForObject(
				"WITH temp AS (UPDATE temp_schedule SET subject = :subject, teacher = :teacher,"
						+ " cabinet = :cabinet, will_lesson_be=:willLessonBe"
						+ " WHERE flow = :flow AND lesson_date=:lessonDate AND lesson_num=:lessonNum RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM temp"
						+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
						+ " JOIN teacher t ON temp.teacher = t.id JOIN cabinet c ON temp.cabinet = c.id",
				Map.of(
						"flow", tempSchedule.getFlow().getId(),
						"subject", tempSchedule.getSubject().getId(),
						"teacher", tempSchedule.getTeacher().getId(),
						"cabinet", tempSchedule.getCabinet().getId(),
						"lessonDate", tempSchedule.getLessonDate(),
						"lessonNum", tempSchedule.getLessonNum(),
						"willLessonBe", tempSchedule.getWillLessonBe()
				),
				rowMapper
		);
	}

	@Override
	public Optional<TempSchedule> findById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT " + SELECT_COLUMNS + " FROM temp_schedule temp"
										+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
										+ " JOIN teacher t ON temp.teacher = t.id"
										+ " JOIN cabinet c ON temp.cabinet = c.id"
										+ " WHERE temp.id = :id",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Optional<TempSchedule> findByFlowAndLessonDateAndLessonNum(long flow, LocalDate lessonDate, int lessonNum) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT " + SELECT_COLUMNS + " FROM temp_schedule temp"
										+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
										+ " JOIN teacher t ON temp.teacher = t.id"
										+ " JOIN cabinet c ON temp.cabinet = c.id"
										+ " WHERE flow=:flow AND lesson_date=:lessonDate AND lesson_num=:lessonNum",
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
	public Iterable<? extends TempSchedule> findAll() {
		return jdbcTemplate.query(
				"SELECT " + SELECT_COLUMNS + " FROM temp_schedule temp"
						+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
						+ " JOIN teacher t ON temp.teacher = t.id JOIN cabinet c ON temp.cabinet = c.id",
				rowMapper
		);
	}
	
	@Override
	public Iterable<? extends TempSchedule> findAllByFlow(long flow) {
		return jdbcTemplate.query(
				"SELECT " + SELECT_COLUMNS + " FROM temp_schedule temp"
						+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
						+ " JOIN teacher t ON temp.teacher = t.id JOIN cabinet c ON temp.cabinet = c.id"
						+ " WHERE temp.flow = :flow",
				Map.of("flow", flow),
				rowMapper
		);
	}
	
	@Override
	public Iterable<TempSchedule> findAllByFlowAndLessonDate(long flow, LocalDate lessonDate) {
		return jdbcTemplate.query(
				"SELECT " + SELECT_COLUMNS + " FROM temp_schedule temp"
						+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
						+ " JOIN teacher t ON temp.teacher = t.id JOIN cabinet c ON temp.cabinet = c.id"
						+ " WHERE flow = :flow AND lesson_date = :lessonDate",
				Map.of(
						"flow", flow,
						"lessonDate", lessonDate
				),
				rowMapper
		);
	}

	@Override
	public Optional<TempSchedule> deleteById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"WITH temp AS (DELETE FROM temp_schedule WHERE id = :id RETURNING *)"
										+ " SELECT " + SELECT_COLUMNS + " FROM temp"
										+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
										+ " JOIN teacher t ON temp.teacher = t.id"
										+ " JOIN cabinet c ON temp.cabinet = c.id",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Iterable<TempSchedule> deleteAll() {
		return jdbcTemplate.query(
				"WITH temp AS (DELETE FROM temp_schedule RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM temp"
						+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
						+ " JOIN teacher t ON temp.teacher = t.id JOIN cabinet c ON temp.cabinet = c.id",
				rowMapper
		);
	}
	
	@Override
	public Iterable<TempSchedule> deleteAllBeforeDate(LocalDate date) {
		return jdbcTemplate.query(
				"WITH temp AS (DELETE FROM temp_schedule WHERE lesson_date < :date RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM temp"
						+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
						+ " JOIN teacher t ON temp.teacher = t.id JOIN cabinet c ON temp.cabinet = c.id",
				Map.of("date", date),
				rowMapper
		);
	}
	
	@Override
	public Iterable<TempSchedule> deleteAllByFlow(long flow) {
		return jdbcTemplate.query(
				"WITH temp AS (DELETE FROM temp_schedule WHERE flow = :flow RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM temp"
						+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
						+ " JOIN teacher t ON temp.teacher = t.id JOIN cabinet c ON temp.cabinet = c.id",
				Map.of("flow", flow),
				rowMapper
		);
	}

	@Override
	public Iterable<TempSchedule> deleteAllBySubject(long subject) {
		return jdbcTemplate.query(
				"WITH temp AS (DELETE FROM temp_schedule WHERE subject = :subject RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM temp"
						+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
						+ " JOIN teacher t ON temp.teacher = t.id JOIN cabinet c ON temp.cabinet = c.id",
				Map.of("subject", subject),
				rowMapper
		);
	}

	@Override
	public Iterable<TempSchedule> deleteAllByTeacher(long teacher) {
		return jdbcTemplate.query(
				"WITH temp AS (DELETE FROM temp_schedule WHERE teacher = :teacher RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM temp"
						+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
						+ " JOIN teacher t ON temp.teacher = t.id JOIN cabinet c ON temp.cabinet = c.id",
				Map.of("teacher", teacher),
				rowMapper
		);
	}

	@Override
	public Iterable<TempSchedule> deleteAllByCabinet(long cabinet) {
		return jdbcTemplate.query(
				"WITH temp AS (DELETE FROM temp_schedule WHERE cabinet = :cabinet RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM temp"
						+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
						+ " JOIN teacher t ON temp.teacher = t.id JOIN cabinet c ON temp.cabinet = c.id",
				Map.of("cabinet", cabinet),
				rowMapper
		);
	}

	@Override
	public Iterable<TempSchedule> deleteAllWhereTeacherIsNotNull() {
		return jdbcTemplate.query(
				"WITH temp AS (DELETE FROM temp_schedule WHERE teacher IS NOT NULL RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM temp"
						+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
						+ " JOIN teacher t ON temp.teacher = t.id JOIN cabinet c ON temp.cabinet = c.id",
				rowMapper
		);
	}

	@Override
	public Iterable<TempSchedule> deleteAllWhereCabinetIsNotNull() {
		return jdbcTemplate.query(
				"WITH temp AS (DELETE FROM temp_schedule WHERE cabinet IS NOT NULL RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM temp"
						+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
						+ " JOIN teacher t ON temp.teacher = t.id JOIN cabinet c ON temp.cabinet = c.id",
				rowMapper
		);
	}

	@Override
	public Iterable<TempSchedule> deleteAllWhereSubjectIsNotNull() {
		return jdbcTemplate.query(
				"WITH temp AS (DELETE FROM temp_schedule WHERE subject IS NOT NULL RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM temp"
						+ " JOIN flow f ON temp.flow = f.id JOIN subject sub ON temp.subject = sub.id"
						+ " JOIN teacher t ON temp.teacher = t.id JOIN cabinet c ON temp.cabinet = c.id",
				rowMapper
		);
	}

}
