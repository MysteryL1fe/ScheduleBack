package ru.khanin.dmitrii.schedule.repo.jdbc.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.khanin.dmitrii.schedule.entity.Flow;

public class FlowRowMapper implements RowMapper<Flow> {

	@Override
	public Flow mapRow(ResultSet rs, int rowNum) throws SQLException {
		Date lessonsStartDate = rs.getDate("lessons_start_date");
		Date sessionStartDate = rs.getDate("session_start_date");
		Date sessionEndDate = rs.getDate("session_end_date");
		
		Flow result = new Flow();
		result.setId(rs.getLong("id"));
		result.setEducationLevel(rs.getInt("education_level"));
		result.setCourse(rs.getInt("course"));
		result.setGroup(rs.getInt("_group"));
		result.setSubgroup(rs.getInt("subgroup"));
		result.setLastEdit(rs.getTimestamp("last_edit").toLocalDateTime());
		result.setActive(rs.getBoolean("active"));
		result.setLessonsStartDate(lessonsStartDate == null ? null : lessonsStartDate.toLocalDate());
		result.setSessionStartDate(sessionStartDate == null ? null : sessionStartDate.toLocalDate());
		result.setSessionEndDate(sessionEndDate == null ? null : sessionEndDate.toLocalDate());
		
		return result;
	}

}
