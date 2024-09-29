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

public class UserRowMapper implements RowMapper<User> {
	FlowRowMapper flowRowMapper = new FlowRowMapper();

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
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
			Flow flow = new Flow();
			flow.setId(flowIds[i].longValue());
			flow.setEducationLevel(flowEducationLevels[i]);
			flow.setCourse(flowCourses[i]);
			flow.setGroup(flowGroups[i]);
			flow.setSubgroup(flowSubgroups[i]);
			flow.setLastEdit(flowLastEdits[i].toLocalDateTime());
			flow.setLessonsStartDate(flowLessonsStarts[i] == null ? null : flowLessonsStarts[i].toLocalDate());
			flow.setSessionStartDate(flowSessionStarts[i] == null ? null : flowSessionStarts[i].toLocalDate());
			flow.setSessionEndDate(flowSessionEnds[i] == null ? null : flowSessionEnds[i].toLocalDate());
			flow.setActive(flowActives[i]);
			
			flows.add(flow);
		}
		
		User user = new User();
		user.setId(rs.getLong("user_id"));
		user.setLogin(rs.getString("login"));
		user.setAdmin(rs.getBoolean("admin"));
		user.setFlows(flows);
		return user;
	}

}
