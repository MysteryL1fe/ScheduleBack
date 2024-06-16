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
				"INSERT INTO schedule(flow, day_of_week, lesson, lesson_num, is_numerator)"
				+ " VALUES (:flow, :dayOfWeek, :lesson, :lessonNum, :isNumerator) RETURNING *",
				new BeanPropertySqlParameterSource(schedule),
				rowMapper
		);
	}
	
	@Override
	public Schedule update(Schedule schedule) {
		return jdbcTemplate.queryForObject(
				"UPDATE schedule SET lesson = :lesson"
				+ " WHERE flow=:flow AND day_of_week=:dayOfWeek AND lesson_num=:lessonNum"
				+ " AND is_numerator=:isNumerator RETURNING *",
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
	public Optional<Schedule> findByFlowAndDayOfWeekAndLessonNumAndIsNumerator(
			long flow, int dayOfWeek, int lessonNum, boolean isNumerator
	) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM schedule"
								+ " WHERE flow=:flow AND day_of_week=:dayOfWeek AND lesson_num=:lessonNum"
								+ " AND is_numerator=:isNumerator",
								Map.of(
										"flow", flow,
										"dayOfWeek", dayOfWeek,
										"lessonNum", lessonNum,
										"isNumerator", isNumerator
								),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<? extends Schedule> findAll() {
		return jdbcTemplate.query(
				"SELECT s.id AS schedule_id, s.flow AS flow_id, s.lesson AS lesson_id, s.day_of_week, s.lesson_num,"
				+ " s.is_numerator, f.flow_lvl, f.course, f.flow, f.subgroup, f.last_edit, f.lessons_start_date,"
				+ " f.session_start_date, f.session_end_date, f.active, l.name, l.teacher, l.cabinet"
				+ " FROM schedule s JOIN flow f ON s.flow=f.id JOIN lesson l ON s.lesson=l.id",
				joinedRowMapper
		);
	}
	
	@Override
	public Iterable<? extends Schedule> findAllByFlow(long flow) {
		return jdbcTemplate.query(
				"SELECT s.id AS schedule_id, s.flow AS flow_id, s.lesson AS lesson_id, s.day_of_week, s.lesson_num,"
				+ " s.is_numerator, f.flow_lvl, f.course, f.flow, f.subgroup, f.last_edit, f.lessons_start_date,"
				+ " f.session_start_date, f.session_end_date, f.active, l.name, l.teacher, l.cabinet"
				+ " FROM schedule s JOIN flow f ON s.flow=f.id JOIN lesson l ON s.lesson=l.id"
				+ " WHERE flow=:flow",
				Map.of("flow", flow),
				joinedRowMapper
		);
	}
	
	@Override
	public Iterable<Schedule> findAllByFlowAndDayOfWeekAndIsNumerator(long flow, int dayOfWeek, boolean isNumerator) {
		return jdbcTemplate.query(
				"SELECT * FROM schedule WHERE flow=:flow AND day_of_week=:dayOfWeek AND is_numerator=:isNumerator",
				Map.of(
						"flow", flow,
						"dayOfWeek", dayOfWeek,
						"isNumerator", isNumerator
				),
				rowMapper
		);
	}
	
	@Override
	public Iterable<? extends Schedule> findAllWhereTeacherStartsWith(String teacher) {
		return jdbcTemplate.query(
				"SELECT s.id AS schedule_id, s.flow AS flow_id, s.lesson AS lesson_id, s.day_of_week, s.lesson_num,"
				+ " s.is_numerator, f.flow_lvl, f.course, f.flow, f.subgroup, f.last_edit, f.lessons_start_date,"
				+ " f.session_start_date, f.session_end_date, f.active, l.name, l.teacher, l.cabinet"
				+ " FROM schedule s JOIN flow f ON s.flow=f.id JOIN lesson l ON s.lesson=l.id"
				+ " WHERE istarts_with(l.teacher, :teacher)",
				Map.of("teacher", teacher),
				joinedRowMapper
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
	public Iterable<Schedule> deleteAllByLesson(long lesson) {
		return jdbcTemplate.query(
				"DELETE FROM schedule WHERE lesson=:lesson RETURNING *",
				Map.of("lesson", lesson),
				rowMapper
		);
	}

}
