package ru.khanin.dmitrii.schedule.repo.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.khanin.dmitrii.schedule.entity.UserFlow;

public class UserFlowRowMapper implements RowMapper<UserFlow> {

	@Override
	public UserFlow mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserFlow result = new UserFlow();
		result.setUser(rs.getLong("_user"));
		result.setFlow(rs.getLong("flow"));
		
		return result;
	}

}
