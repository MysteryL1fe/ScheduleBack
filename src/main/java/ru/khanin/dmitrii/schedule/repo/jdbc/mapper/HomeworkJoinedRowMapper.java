package ru.khanin.dmitrii.schedule.repo.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.jdbc.HomeworkJoined;

public class HomeworkJoinedRowMapper implements RowMapper<HomeworkJoined> {

	@Override
	public HomeworkJoined mapRow(ResultSet rs, int rowNum) throws SQLException {
		Flow flow = new Flow();
		flow.setId(rs.getLong("flow_id"));
		flow.setFlowLvl(rs.getInt("flow_lvl"));
		flow.setCourse(rs.getInt("course"));
		flow.setFlow(rs.getInt("flow"));
		flow.setSubgroup(rs.getInt("subgroup"));
		flow.setLastEdit(rs.getTimestamp("last_edit").toLocalDateTime());
		flow.setLessonsStartDate(rs.getDate("lessons_start_date").toLocalDate());
		flow.setSessionStartDate(rs.getDate("session_start_date").toLocalDate());
		flow.setSessionEndDate(rs.getDate("session_end_date").toLocalDate());
		flow.setActive(rs.getBoolean("active"));
		
		HomeworkJoined result = new HomeworkJoined();
		result.setId(rs.getLong("homework_id"));
		result.setHomework(rs.getString("homework"));
		result.setLessonDate(rs.getDate("lesson_date").toLocalDate());
		result.setLessonNum(rs.getInt("lesson_num"));
		result.setFlow(rs.getInt("flow_id"));
		result.setJoinedFlow(flow);
		
		return result;
	}

}
