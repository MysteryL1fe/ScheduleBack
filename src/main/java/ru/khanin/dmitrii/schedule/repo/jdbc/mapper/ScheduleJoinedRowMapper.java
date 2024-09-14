package ru.khanin.dmitrii.schedule.repo.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.khanin.dmitrii.schedule.entity.Cabinet;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Subject;
import ru.khanin.dmitrii.schedule.entity.Teacher;
import ru.khanin.dmitrii.schedule.entity.jdbc.ScheduleJoined;

public class ScheduleJoinedRowMapper implements RowMapper<ScheduleJoined> {

	@Override
	public ScheduleJoined mapRow(ResultSet rs, int rowNum) throws SQLException {
		Flow flow = new Flow();
		flow.setId(rs.getLong("flow_id"));
		flow.setEducationLevel(rs.getInt("education_level"));
		flow.setCourse(rs.getInt("course"));
		flow.setGroup(rs.getInt("_group"));
		flow.setSubgroup(rs.getInt("subgroup"));
		flow.setLastEdit(rs.getTimestamp("last_edit").toLocalDateTime());
		flow.setLessonsStartDate(rs.getDate("lessons_start_date").toLocalDate());
		flow.setSessionStartDate(rs.getDate("session_start_date").toLocalDate());
		flow.setSessionEndDate(rs.getDate("session_end_date").toLocalDate());
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
		
		ScheduleJoined result = new ScheduleJoined();
		result.setId(rs.getLong("schedule_id"));
		result.setFlow(rs.getLong("flow_id"));
		result.setFlowJoined(flow);
		result.setSubject(rs.getLong("subject_id"));
		result.setSubjectJoined(subject);
		result.setTeacher(rs.getLong("teacher_id"));
		result.setTeacherJoined(teacher);
		result.setCabinet(rs.getLong("cabinet_id"));
		result.setCabinetJoined(cabinet);
		result.setDayOfWeek(rs.getInt("day_of_week"));
		result.setLessonNum(rs.getInt("lesson_num"));
		result.setNumerator(rs.getBoolean("numerator"));
		
		return result;
	}

}
