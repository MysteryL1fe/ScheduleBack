package ru.khanin.dmitrii.schedule.repo.jdbc.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Homework;
import ru.khanin.dmitrii.schedule.entity.Subject;

public class HomeworkRowMapper implements RowMapper<Homework> {
	
	@Override
	public Homework mapRow(ResultSet rs, int rowNum) throws SQLException {
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
		
		Homework result = new Homework();
		result.setId(rs.getLong("homework_id"));
		result.setHomework(rs.getString("homework"));
		result.setLessonDate(rs.getDate("lesson_date").toLocalDate());
		result.setLessonNum(rs.getInt("lesson_num"));
		result.setFlow(flow);
		result.setSubject(subject);
		
		return result;
	}
}
