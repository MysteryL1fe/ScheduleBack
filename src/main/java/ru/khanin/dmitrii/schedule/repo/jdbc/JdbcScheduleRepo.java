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
import ru.khanin.dmitrii.schedule.entity.Schedule;
import ru.khanin.dmitrii.schedule.entity.jdbc.ScheduleJoined;
import ru.khanin.dmitrii.schedule.repo.ScheduleRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.mapper.ScheduleJoinedRowMapper;

@Repository
@RequiredArgsConstructor
public class JdbcScheduleRepo implements ScheduleRepo {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Schedule> rowMapper = new DataClassRowMapper<>(Schedule.class);
	private final RowMapper<ScheduleJoined> joinedRowMapper = new ScheduleJoinedRowMapper();
	
	@Override
	public Schedule add(Schedule schedule) {
		return jdbcTemplate.queryForObject(
				"INSERT INTO schedule(flow, day_of_week, lesson_num, numerator, subject, teacher, cabinet)"
				+ " VALUES (:flow, :dayOfWeek, :lessonNum, :numerator, :subject, :teacher, :cabinet) RETURNING *",
				new BeanPropertySqlParameterSource(schedule),
				rowMapper
		);
	}
	
	@Override
	public Schedule update(Schedule schedule) {
		return jdbcTemplate.queryForObject(
				"UPDATE schedule SET subject=:subject, teacher=:teacher, cabinet=:cabinet"
				+ " WHERE flow=:flow AND day_of_week=:dayOfWeek AND lesson_num=:lessonNum"
				+ " AND numerator=:numerator RETURNING *",
				new BeanPropertySqlParameterSource(schedule),
				rowMapper
		);
	}

	@Override
	public Optional<Schedule> findById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM schedule WHERE id=:id",
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
								"SELECT * FROM schedule"
								+ " WHERE flow=:flow AND day_of_week=:dayOfWeek AND lesson_num=:lessonNum"
								+ " AND numerator=:numerator",
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
				"SELECT sch.id AS schedule_id, sch.flow AS flow_id, sch.subject AS subject_id,"
				+ " sch.teacher AS teacher_id, sch.cabinet AS cabinet_id, sch.day_of_week, sch.lesson_num,"
				+ " sch.numerator, f.education_level, f.course, f._group, f.subgroup, f.last_edit, f.lessons_start_date,"
				+ " f.session_start_date, f.session_end_date, f.active, sub.subject, t.surname, t.name, t.patronymic,"
				+ " c.cabinet, c.building, c.address"
				+ " FROM schedule sch JOIN flow f ON sch.flow=f.id JOIN subject sub ON sch.subject=sub.id"
				+ " JOIN teacher t ON sch.teacher=t.id JOIN cabinet c ON sch.cabinet=c.id",
				joinedRowMapper
		);
	}
	
	@Override
	public Iterable<? extends Schedule> findAllByFlow(long flow) {
		return jdbcTemplate.query(
				"SELECT sch.id AS schedule_id, sch.flow AS flow_id, sch.subject AS subject_id,"
				+ " sch.teacher AS teacher_id, sch.cabinet AS cabinet_id, sch.day_of_week, sch.lesson_num,"
				+ " sch.numerator, f.education_level, f.course, f._group, f.subgroup, f.last_edit, f.lessons_start_date,"
				+ " f.session_start_date, f.session_end_date, f.active, sub.subject, t.surname, t.name, t.patronymic,"
				+ " c.cabinet, c.building, c.address"
				+ " FROM schedule sch JOIN flow f ON sch.flow=f.id JOIN subject sub ON sch.subject=sub.id"
				+ " JOIN teacher t ON sch.teacher=t.id JOIN cabinet c ON sch.cabinet=c.id"
				+ " WHERE s.flow=:flow",
				Map.of("flow", flow),
				joinedRowMapper
		);
	}
	
	@Override
	public Iterable<Schedule> findAllByFlowAndDayOfWeekAndNumerator(long flow, int dayOfWeek, boolean numerator) {
		return jdbcTemplate.query(
				"SELECT * FROM schedule WHERE flow=:flow AND day_of_week=:dayOfWeek AND numerator=:numerator",
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
								"DELETE FROM schedule WHERE id=:id RETURNING *",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Iterable<Schedule> deleteAll() {
		return jdbcTemplate.query(
				"DELETE FROM schedule RETURNING *",
				rowMapper
		);
	}
	
	@Override
	public Iterable<Schedule> deleteAllByFlow(long flow) {
		return jdbcTemplate.query(
				"DELETE FROM schedule WHERE flow=:flow RETURNING *",
				Map.of("flow", flow),
				rowMapper
		);
	}

	@Override
	public Iterable<? extends Schedule> findAllByTeacher(long teacher) {
		return jdbcTemplate.query(
				"SELECT sch.id AS schedule_id, sch.flow AS flow_id, sch.subject AS subject_id,"
				+ " sch.teacher AS teacher_id, sch.cabinet AS cabinet_id, sch.day_of_week, sch.lesson_num,"
				+ " sch.numerator, f.education_level, f.course, f._group, f.subgroup, f.last_edit, f.lessons_start_date,"
				+ " f.session_start_date, f.session_end_date, f.active, sub.subject, t.surname, t.name, t.patronymic,"
				+ " c.cabinet, c.building, c.address"
				+ " FROM schedule sch JOIN flow f ON sch.flow=f.id JOIN subject sub ON sch.subject=sub.id"
				+ " JOIN teacher t ON sch.teacher=t.id JOIN cabinet c ON sch.cabinet=c.id"
				+ " WHERE s.teacher=:teacher",
				Map.of("teacher", teacher),
				joinedRowMapper
		);
	}

	@Override
	public Iterable<Schedule> deleteAllBySubject(long subject) {
		return jdbcTemplate.query(
				"DELETE FROM schedule WHERE subject=:subject RETURNING *",
				Map.of("subject", subject),
				rowMapper
		);
	}

	@Override
	public Iterable<Schedule> deleteAllByTeacher(long teacher) {
		return jdbcTemplate.query(
				"DELETE FROM schedule WHERE teacher=:teacher RETURNING *",
				Map.of("teacher", teacher),
				rowMapper
		);
	}

	@Override
	public Iterable<Schedule> deleteAllByCabinet(long cabinet) {
		return jdbcTemplate.query(
				"DELETE FROM schedule WHERE cabinet=:cabinet RETURNING *",
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
								"DELETE FROM schedule WHERE flow=:flow AND day_of_week=:dayOfWeek"
								+ " AND lesson_num=:lessonNum AND numerator=:numerator RETURNING *",
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
				"DELETE FROM schedule WHERE teacher IS NOT NULL RETURNING *",
				rowMapper
		);
	}

	@Override
	public Iterable<Schedule> deleteAllWhereCabinetIsNotNull() {
		return jdbcTemplate.query(
				"DELETE FROM schedule WHERE cabinet IS NOT NULL RETURNING *",
				rowMapper
		);
	}

}
