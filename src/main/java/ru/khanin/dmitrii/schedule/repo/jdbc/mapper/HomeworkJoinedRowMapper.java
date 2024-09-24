package ru.khanin.dmitrii.schedule.repo.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Subject;
import ru.khanin.dmitrii.schedule.entity.jdbc.HomeworkJoined;

public class HomeworkJoinedRowMapper implements RowMapper<HomeworkJoined> {

	@Override
	public HomeworkJoined mapRow(ResultSet rs, int rowNum) throws SQLException {
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
		
		HomeworkJoined result = new HomeworkJoined();
		result.setId(rs.getLong("homework_id"));
		result.setHomework(rs.getString("homework"));
		result.setLessonDate(rs.getDate("lesson_date").toLocalDate());
		result.setLessonNum(rs.getInt("lesson_num"));
		result.setFlow(rs.getLong("flow_id"));
		result.setFlowJoined(flow);
		result.setSubject(rs.getLong("subject_id"));
		result.setSubjectJoined(subject);
		
		return result;
	}

}
