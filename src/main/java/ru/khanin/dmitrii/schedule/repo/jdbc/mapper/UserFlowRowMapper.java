package ru.khanin.dmitrii.schedule.repo.jdbc.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.User;
import ru.khanin.dmitrii.schedule.entity.UserFlow;

public class UserFlowRowMapper implements RowMapper<UserFlow> {

	@Override
	public UserFlow mapRow(ResultSet rs, int rowNum) throws SQLException {
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
		flow.setActive(rs.getBoolean("active"));
		flow.setLessonsStartDate(lessonsStartDate == null ? null : lessonsStartDate.toLocalDate());
		flow.setSessionStartDate(sessionStartDate == null ? null : sessionStartDate.toLocalDate());
		flow.setSessionEndDate(sessionEndDate == null ? null : sessionEndDate.toLocalDate());
		
		List<Flow> flows = new ArrayList<>();
		
		Integer[] flowIds = (Integer[]) rs.getArray("flow_ids").getArray();
		Integer[] flowEducationLevels = (Integer[]) rs.getArray("flow_education_levels").getArray();
		Integer[] flowCourses = (Integer[]) rs.getArray("flow_courses").getArray();
		Integer[] flowGroups = (Integer[]) rs.getArray("flow_groups").getArray();
		Integer[] flowSubgroups = (Integer[]) rs.getArray("flow_subgroups").getArray();
		Timestamp[] flowLastEdits = (Timestamp[]) rs.getArray("flow_last_edits").getArray();
		Date[] flowLessonsStarts = (Date[]) rs.getArray("flow_lessons_starts").getArray();
		Date[] flowSessionStarts = (Date[]) rs.getArray("flow_session_starts").getArray();
		Date[] flowSessionEnds = (Date[]) rs.getArray("flow_session_ends").getArray();
		Boolean[] flowActives = (Boolean[]) rs.getArray("flow_actives").getArray();
		
		for (int i = 0; i < flowIds.length; i++) {
			Flow flow1 = new Flow();
			flow1.setId(flowIds[i].longValue());
			flow1.setEducationLevel(flowEducationLevels[i]);
			flow1.setCourse(flowCourses[i]);
			flow1.setGroup(flowGroups[i]);
			flow1.setSubgroup(flowSubgroups[i]);
			flow1.setLastEdit(flowLastEdits[i].toLocalDateTime());
			flow1.setLessonsStartDate(flowLessonsStarts[i] == null ? null : flowLessonsStarts[i].toLocalDate());
			flow1.setSessionStartDate(flowSessionStarts[i] == null ? null : flowSessionStarts[i].toLocalDate());
			flow1.setSessionEndDate(flowSessionEnds[i] == null ? null : flowSessionEnds[i].toLocalDate());
			flow1.setActive(flowActives[i]);
			
			flows.add(flow1);
		}
		
		User user = new User();
		user.setId(rs.getLong("user_id"));
		user.setLogin(rs.getString("login"));
		user.setAdmin(rs.getBoolean("admin"));
		user.setFlows(flows);
		
		UserFlow userFlow = new UserFlow();
		userFlow.setUser(user);
		userFlow.setFlow(flow);
		
		return userFlow;
	}

}
