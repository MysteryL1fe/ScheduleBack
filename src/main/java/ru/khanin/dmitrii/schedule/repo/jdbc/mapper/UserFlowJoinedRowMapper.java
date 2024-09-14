package ru.khanin.dmitrii.schedule.repo.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.User;
import ru.khanin.dmitrii.schedule.entity.jdbc.UserFlowJoined;

public class UserFlowJoinedRowMapper implements RowMapper<UserFlowJoined> {

	@Override
	public UserFlowJoined mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId(rs.getLong("user_id"));
		user.setLogin(rs.getString("login"));
		user.setPassword(rs.getString("password"));
		user.setAdmin(rs.getBoolean("admin"));
		
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
		
		UserFlowJoined result = new UserFlowJoined();
		result.setUser(rs.getLong("user_id"));
		result.setUserJoined(user);
		result.setFlow(rs.getLong("flow_id"));
		result.setFlowJoined(flow);
		
		return result;
	}

}
