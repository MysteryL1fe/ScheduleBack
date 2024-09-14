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
import ru.khanin.dmitrii.schedule.entity.TempSchedule;
import ru.khanin.dmitrii.schedule.entity.jdbc.TempScheduleJoined;
import ru.khanin.dmitrii.schedule.repo.TempScheduleRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.mapper.TempScheduleJoinedRowMapper;

@Repository
@RequiredArgsConstructor
public class JdbcTempScheduleRepo implements TempScheduleRepo {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<TempSchedule> rowMapper = new DataClassRowMapper<>(TempSchedule.class);
	private final RowMapper<TempScheduleJoined> joinedRowMapper = new TempScheduleJoinedRowMapper();
	
	@Override
	public TempSchedule add(TempSchedule tempSchedule) {
		return jdbcTemplate.queryForObject(
				"INSERT INTO temp_schedule(flow, subject, teacher, cabinet, lesson_date, lesson_num, will_lesson_be)"
				+ " VALUES (:flow, :subject, :teacher, :cabinet, :lessonDate, :lessonNum, :willLessonBe) RETURNING *",
				new BeanPropertySqlParameterSource(tempSchedule),
				rowMapper
		);
	}
	
	@Override
	public TempSchedule update(TempSchedule tempSchedule) {
		return jdbcTemplate.queryForObject(
				"UPDATE temp_schedule SET subject=:subject, teacher=:teacher, cabinet=:cabinet,"
				+ " will_lesson_be=:willLessonBe"
				+ " WHERE flow=:flow AND lesson_date=:lessonDate AND lesson_num=:lessonNum"
				+ " RETURNING *",
				new BeanPropertySqlParameterSource(tempSchedule),
				rowMapper
		);
	}

	@Override
	public Optional<TempSchedule> findById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM temp_schedule WHERE id=:id",
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
								"SELECT * FROM temp_schedule"
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
				"SELECT ts.id AS temp_schedule_id, ts.flow AS flow_id, ts.subject AS subject_id,"
				+ " ts.teacher AS teacher_id, ts.cabinet AS cabinet_id, ts.lesson_date, ts.lesson_num,"
				+ " ts.will_lesson_be, f.education_level, f.course, f._group, f.subgroup, f.last_edit, f.lessons_start_date,"
				+ " f.session_start_date, f.session_end_date, f.active, s.subject, t.surname, t.name, t.patronymic,"
				+ " c.cabinet, c.building, c.address"
				+ " FROM temp_schedule ts JOIN flow f ON ts.flow=f.id JOIN subject s ON ts.subject=s.id"
				+ " JOIN teacher t ON ts.teacher=t.id JOIN cabinet c ON ts.cabinet=c.id",
				joinedRowMapper
		);
	}
	
	@Override
	public Iterable<? extends TempSchedule> findAllByFlow(long flow) {
		return jdbcTemplate.query(
				"SELECT ts.id AS temp_schedule_id, ts.flow AS flow_id, ts.subject AS subject_id,"
				+ " ts.teacher AS teacher_id, ts.cabinet AS cabinet_id, ts.lesson_date, ts.lesson_num,"
				+ " ts.will_lesson_be, f.education_level, f.course, f._group, f.subgroup, f.last_edit, f.lessons_start_date,"
				+ " f.session_start_date, f.session_end_date, f.active, s.subject, t.surname, t.name, t.patronymic,"
				+ " c.cabinet, c.building, c.address"
				+ " FROM temp_schedule ts JOIN flow f ON ts.flow=f.id JOIN subject s ON ts.subject=s.id"
				+ " JOIN teacher t ON ts.teacher=t.id JOIN cabinet c ON ts.cabinet=c.id"
				+ " WHERE ts.flow=:flow",
				Map.of("flow", flow),
				joinedRowMapper
		);
	}
	
	@Override
	public Iterable<TempSchedule> findAllByFlowAndLessonDate(long flow, LocalDate lessonDate) {
		return jdbcTemplate.query(
				"SELECT * FROM temp_schedule WHERE flow=:flow AND lesson_date=:lessonDate",
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
								"DELETE FROM temp_schedule WHERE id=:id RETURNING *",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Iterable<TempSchedule> deleteAll() {
		return jdbcTemplate.query(
				"DELETE FROM temp_schedule RETURNING *",
				rowMapper
		);
	}
	
	@Override
	public Iterable<TempSchedule> deleteAllBeforeDate(LocalDate date) {
		return jdbcTemplate.query(
				"DELETE FROM temp_schedule WHERE lesson_date<:date RETURNING *",
				Map.of("date", date),
				rowMapper
		);
	}
	
	@Override
	public Iterable<TempSchedule> deleteAllByFlow(long flow) {
		return jdbcTemplate.query(
				"DELETE FROM temp_schedule WHERE flow=:flow RETURNING *",
				Map.of("flow", flow),
				rowMapper
		);
	}

	@Override
	public Iterable<TempSchedule> deleteAllBySubject(long subject) {
		return jdbcTemplate.query(
				"DELETE FROM temp_schedule WHERE subject=:subject RETURNING *",
				Map.of("subject", subject),
				rowMapper
		);
	}

	@Override
	public Iterable<TempSchedule> deleteAllByTeacher(long teacher) {
		return jdbcTemplate.query(
				"DELETE FROM temp_schedule WHERE teacher=:teacher RETURNING *",
				Map.of("teacher", teacher),
				rowMapper
		);
	}

	@Override
	public Iterable<TempSchedule> deleteAllByCabinet(long cabinet) {
		return jdbcTemplate.query(
				"DELETE FROM temp_schedule WHERE cabinet=:cabinet RETURNING *",
				Map.of("cabinet", cabinet),
				rowMapper
		);
	}

	@Override
	public Iterable<TempSchedule> deleteAllWhereTeacherIsNotNull() {
		return jdbcTemplate.query(
				"DELETE FROM temp_schedule WHERE teacher IS NOT NULL RETURNING *",
				rowMapper
		);
	}

	@Override
	public Iterable<TempSchedule> deleteAllWhereCabinetIsNotNull() {
		return jdbcTemplate.query(
				"DELETE FROM temp_schedule WHERE cabinet IS NOT NULL RETURNING *",
				rowMapper
		);
	}

	@Override
	public Iterable<TempSchedule> deleteAllWhereSubjectIsNotNull() {
		return jdbcTemplate.query(
				"DELETE FROM temp_schedule WHERE subject IS NOT NULL RETURNING *",
				rowMapper
		);
	}

}
