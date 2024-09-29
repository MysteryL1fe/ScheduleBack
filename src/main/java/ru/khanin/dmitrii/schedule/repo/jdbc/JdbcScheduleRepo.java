package ru.khanin.dmitrii.schedule.repo.jdbc;

import java.util.Map;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Schedule;
import ru.khanin.dmitrii.schedule.repo.ScheduleRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.mapper.ScheduleRowMapper;

@Repository
@RequiredArgsConstructor
public class JdbcScheduleRepo implements ScheduleRepo {
	private final String SELECT_COLUMNS = "sch.id AS schedule_id, sch.day_of_week, sch.lesson_num, sch.numerator,"
			+ " sch.flow AS flow_id, sch.subject AS subject_id, sch.teacher AS teacher_id, sch.cabinet AS cabinet"
			+ " f.education_level, f.course, f._group, f.subgroup, f.last_edit, f.lessons_start_date,"
			+ " f.session_start_date, f.session_end_date, f.active, sub.subject,"
			+ " t.surname, t.name, t.patronymic, c.cabinet, c.building, c.address";
			
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Schedule> rowMapper = new ScheduleRowMapper();
	
	@Override
	public Schedule add(Schedule schedule) {
		return jdbcTemplate.queryForObject(
				"WITH sch AS (INSERT INTO schedule("
						+ "flow, day_of_week, lesson_num, numerator, subject, teacher, cabinet) VALUES"
						+ " (:flow, :dayOfWeek, :lessonNum, :numerator, :subject, :teacher, :cabinet) RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM sch"
						+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
						+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id",
				Map.of(
						"flow", schedule.getFlow().getId(),
						"dayOfWeek", schedule.getDayOfWeek(),
						"lessonNum", schedule.getLessonNum(),
						"numerator", schedule.getNumerator(),
						"subject", schedule.getSubject().getId(),
						"teacher", schedule.getTeacher().getId(),
						"cabinet", schedule.getCabinet().getId()
				),
				rowMapper
		);
	}
	
	@Override
	public Schedule update(Schedule schedule) {
		return jdbcTemplate.queryForObject(
				"WITH sch AS (UPDATE schedule SET subject = :subject, teacher = :teacher, cabinet = :cabinet"
						+ " WHERE flow = :flow AND day_of_week = :dayOfWeek AND lesson_num = :lessonNum"
						+ " AND numerator = :numerator RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM sch"
						+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
						+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id",
				Map.of(
						"flow", schedule.getFlow().getId(),
						"dayOfWeek", schedule.getDayOfWeek(),
						"lessonNum", schedule.getLessonNum(),
						"numerator", schedule.getNumerator(),
						"subject", schedule.getSubject().getId(),
						"teacher", schedule.getTeacher().getId(),
						"cabinet", schedule.getCabinet().getId()
				),
				rowMapper
		);
	}

	@Override
	public Optional<Schedule> findById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT " + SELECT_COLUMNS + " FROM schedule sch"
										+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
										+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id"
										+ " WHERE sch.id = :id",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Optional<Schedule> findByFlowAndDayOfWeekAndLessonNumAndNumerator(
			long flow, int dayOfWeek, int lessonNum, boolean numerator
	) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT " + SELECT_COLUMNS + " FROM schedule sch"
										+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
										+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id"
										+ " WHERE flow = :flow AND day_of_week = :dayOfWeek AND lesson_num = :lessonNum"
										+ " AND numerator = :numerator",
								Map.of(
										"flow", flow,
										"dayOfWeek", dayOfWeek,
										"lessonNum", lessonNum,
										"numerator", numerator
								),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<? extends Schedule> findAll() {
		return jdbcTemplate.query(
				"SELECT " + SELECT_COLUMNS + " FROM sch"
						+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
						+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id",
				rowMapper
		);
	}
	
	@Override
	public Iterable<? extends Schedule> findAllByFlow(long flow) {
		return jdbcTemplate.query(
				"SELECT " + SELECT_COLUMNS + " FROM sch"
						+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
						+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id"
						+ " WHERE s.flow = :flow",
				Map.of("flow", flow),
				rowMapper
		);
	}
	
	@Override
	public Iterable<Schedule> findAllByFlowAndDayOfWeekAndNumerator(long flow, int dayOfWeek, boolean numerator) {
		return jdbcTemplate.query(
				"SELECT " + SELECT_COLUMNS + " FROM sch"
						+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
						+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id"
						+ " WHERE flow = :flow AND day_of_week = :dayOfWeek AND numerator = :numerator",
				Map.of(
						"flow", flow,
						"dayOfWeek", dayOfWeek,
						"numerator", numerator
				),
				rowMapper
		);
	}

	@Override
	public Optional<Schedule> deleteById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"WITH sch AS (DELETE FROM schedule WHERE id=:id RETURNING *)"
										+ " SELECT " + SELECT_COLUMNS + " FROM sch"
										+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
										+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Iterable<Schedule> deleteAll() {
		return jdbcTemplate.query(
				"WITH sch AS (DELETE FROM schedule RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM sch"
						+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
						+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id",
				rowMapper
		);
	}
	
	@Override
	public Iterable<Schedule> deleteAllByFlow(long flow) {
		return jdbcTemplate.query(
				"WITH sch AS (DELETE FROM schedule WHERE flow = :flow RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM sch"
						+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
						+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id",
				Map.of("flow", flow),
				rowMapper
		);
	}

	@Override
	public Iterable<? extends Schedule> findAllByTeacher(long teacher) {
		return jdbcTemplate.query(
				"SELECT " + SELECT_COLUMNS + " FROM sch"
						+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
						+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id"
						+ " WHERE s.teacher = :teacher",
				Map.of("teacher", teacher),
				rowMapper
		);
	}

	@Override
	public Iterable<Schedule> deleteAllBySubject(long subject) {
		return jdbcTemplate.query(
				"WITH sch AS (DELETE FROM schedule WHERE subject = :subject RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM sch"
						+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
						+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id",
				Map.of("subject", subject),
				rowMapper
		);
	}

	@Override
	public Iterable<Schedule> deleteAllByTeacher(long teacher) {
		return jdbcTemplate.query(
				"WITH sch AS (DELETE FROM schedule WHERE teacher = :teacher RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM sch"
						+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
						+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id",
				Map.of("teacher", teacher),
				rowMapper
		);
	}

	@Override
	public Iterable<Schedule> deleteAllByCabinet(long cabinet) {
		return jdbcTemplate.query(
				"WITH sch AS (DELETE FROM schedule WHERE cabinet = :cabinet RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM sch"
						+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
						+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id",
				Map.of("cabinet", cabinet),
				rowMapper
		);
	}

	@Override
	public Optional<Schedule> deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
			long flow, int dayOfWeek, int lessonNum, boolean numerator
	) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"WITH sch AS (DELETE FROM schedule WHERE flow = :flow AND day_of_week = :dayOfWeek"
										+ " AND lesson_num = :lessonNum AND numerator = :numerator RETURNING *)"
										+ " SELECT " + SELECT_COLUMNS + " FROM sch"
										+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
										+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id",
								Map.of(
										"flow", flow,
										"dayOfWeek", dayOfWeek,
										"lessonNum", lessonNum,
										"numerator", numerator
								),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<Schedule> deleteAllWhereTeacherIsNotNull() {
		return jdbcTemplate.query(
				"WITH sch AS (DELETE FROM schedule WHERE teacher IS NOT NULL RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM sch"
						+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
						+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id",
				rowMapper
		);
	}

	@Override
	public Iterable<Schedule> deleteAllWhereCabinetIsNotNull() {
		return jdbcTemplate.query(
				"WITH sch AS (DELETE FROM schedule WHERE cabinet IS NOT NULL RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM sch"
						+ " JOIN flow f ON sch.flow = f.id JOIN subject sub ON sch.subject = sub.id"
						+ " JOIN teacher t ON sch.teacher = t.id JOIN cabinet c ON sch.cabinet = c.id",
				rowMapper
		);
	}

}
