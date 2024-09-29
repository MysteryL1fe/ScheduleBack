package ru.khanin.dmitrii.schedule.repo.jdbc.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.khanin.dmitrii.schedule.entity.Cabinet;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Schedule;
import ru.khanin.dmitrii.schedule.entity.Subject;
import ru.khanin.dmitrii.schedule.entity.Teacher;

public class ScheduleRowMapper implements RowMapper<Schedule> {

	@Override
	public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
		Date lessonsStartDate = rs.getDate("lessons_start_date");
		Date sessionStartDate = rs.getDate("session_start_date");
		Date sessionEndDate = rs.getDate("session_end_date");
		
		Flow flow = new Flow();
		flow.setId(rs.getLong("flow_id"));
		flow.setEducationLevel(rs.getInt("education_level"));
		flow.setCourse(rs.getInt("course"));
		flow.setGroup(rs.getInt("_group"));
		flow.setSubgroup(rs.getInt("subgroup"));
		flow.setLastEdit(rs.getTimestamp("last_edit").toLocalDateTime());
		flow.setLessonsStartDate(lessonsStartDate == null ? null : lessonsStartDate.toLocalDate());
		flow.setSessionStartDate(sessionStartDate == null ? null : sessionStartDate.toLocalDate());
		flow.setSessionEndDate(sessionEndDate == null ? null : sessionEndDate.toLocalDate());
		flow.setActive(rs.getBoolean("active"));
		
		Subject subject = new Subject();
		subject.setId(rs.getLong("subject_id"));
		subject.setSubject(rs.getString("subject"));
		
		Teacher teacher = new Teacher();
		teacher.setId(rs.getLong("teacher_id"));
		teacher.setSurname(rs.getString("surname"));
		teacher.setName(rs.getString("name"));
		teacher.setPatronymic(rs.getString("patronymic"));
		
		Cabinet cabinet = new Cabinet();
		cabinet.setId(rs.getLong("cabinet_id"));
		cabinet.setCabinet(rs.getString("cabinet"));
		cabinet.setBuilding(rs.getString("building"));
		cabinet.setAddress(rs.getString("address"));
		
		Schedule result = new Schedule();
		result.setId(rs.getLong("schedule_id"));
		result.setFlow(flow);
		result.setSubject(subject);
		result.setTeacher(teacher);
		result.setCabinet(cabinet);
		result.setDayOfWeek(rs.getInt("day_of_week"));
		result.setLessonNum(rs.getInt("lesson_num"));
		result.setNumerator(rs.getBoolean("numerator"));
		
		return result;
	}
}
