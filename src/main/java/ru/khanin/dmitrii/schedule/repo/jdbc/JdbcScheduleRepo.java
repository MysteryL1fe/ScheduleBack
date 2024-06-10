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
import ru.khanin.dmitrii.schedule.repo.ScheduleRepo;

@Repository
@RequiredArgsConstructor
public class JdbcScheduleRepo implements ScheduleRepo {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Schedule> rowMapper = new DataClassRowMapper<>(Schedule.class);
	
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
	public Optional<Schedule> findByFlowAndDayOfWeekAndLessonNumAndIsNumerator(long flow, int dayOfWeek, int lessonNum,
			boolean isNumerator) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM schedule WHERE flow=:flow AND day_of_week=:dayOfWeek AND lesson_num=:lessonNum",
								Map.of(
										"flow", flow,
										"dayOfWeek", dayOfWeek,
										"lessonNum", lessonNum
								),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<Schedule> findAll() {
		return jdbcTemplate.query(
				"SELECT * FROM schedule",
				rowMapper
		);
	}
	
	@Override
	public Iterable<Schedule> findAllByFlow(long flow) {
		return jdbcTemplate.query(
				"SELECT * FROM schedule WHERE flow=:flow",
				Map.of("flow", flow),
				rowMapper
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
	public Iterable<Schedule> findAllWhereTeacherStartsWith(String teacher) {
		return jdbcTemplate.query(
				"SELECT s.id, s.flow, s.lesson, s.day_of_week, s.lesson_num, s.is_numerator"
				+ " FROM schedule s JOIN lesson l ON s.lesson=l.id"
				+ " WHERE istarts_with(l.teacher, :teacher)",
				Map.of("teacher", teacher),
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
	public Iterable<Schedule> deleteAllByLesson(long lesson) {
		return jdbcTemplate.query(
				"DELETE FROM schedule WHERE lesson=:lesson RETURNING *",
				Map.of("lesson", lesson),
				rowMapper
		);
	}

}
