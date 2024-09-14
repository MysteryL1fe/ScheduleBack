package ru.khanin.dmitrii.schedule.repo.jdbc;

import java.util.Map;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.UserFlow;
import ru.khanin.dmitrii.schedule.entity.jdbc.UserFlowJoined;
import ru.khanin.dmitrii.schedule.repo.UserFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.mapper.UserFlowJoinedRowMapper;
import ru.khanin.dmitrii.schedule.repo.mapper.UserFlowRowMapper;

@Repository
@RequiredArgsConstructor
public class JdbcUserFlowRepo implements UserFlowRepo {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<UserFlow> rowMapper = new UserFlowRowMapper();
	private final RowMapper<UserFlowJoined> joinedRowMapper = new UserFlowJoinedRowMapper();

	@Override
	public UserFlow add(UserFlow userFlow) {
		return jdbcTemplate.queryForObject(
				"INSERT INTO user_flow (_user, flow) VALUES (:user, :flow) RETURNING *",
				new BeanPropertySqlParameterSource(userFlow),
				rowMapper
		);
	}

	@Override
	public Optional<UserFlow> findByUserAndFlow(long user, long flow) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM user_flow WHERE _user=:user AND flow=:flow",
								Map.of(
										"user", user,
										"flow", flow
								),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<UserFlow> findAll() {
		return jdbcTemplate.query(
				"SELECT * FROM user_flow",
				rowMapper
		);
	}

	@Override
	public Iterable<UserFlow> findAllByUser(long user) {
		return jdbcTemplate.query(
				"SELECT * FROM user_flow WHERE _user=:user",
				Map.of("user", user),
				rowMapper
		);
	}

	@Override
	public Iterable<? extends UserFlow> findAllByFlow(long flow) {
		return jdbcTemplate.query(
				"SELECT uf._user AS user_id, uf.flow AS flow_id, u.login, u.password, u.admin, f.education_level,"
				+ " f.course, f._group, f.subgroup, f.last_edit, f.lessons_start_date, f.session_start_date,"
				+ " f.session_end_date, f.active"
				+ " FROM user_flow uf JOIN users u ON uf._user=u.id JOIN flow f ON uf.flow=f.id"
				+ " WHERE flow=:flow",
				Map.of("flow", flow),
				joinedRowMapper
		);
	}

	@Override
	public Optional<UserFlow> deleteByUserAndFlow(long user, long flow) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"DELETE FROM user_flow WHERE _user=:user AND flow=:flow RETURNING *",
								Map.of(
										"user", user,
										"flow", flow
								),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<UserFlow> deleteAllByUser(long user) {
		return jdbcTemplate.query(
				"DELETE FROM user_flow WHERE _user=:user RETURNING *",
				Map.of("user", user),
				rowMapper
		);
	}

	@Override
	public Iterable<UserFlow> deleteAllByFlow(long flow) {
		return jdbcTemplate.query(
				"DELETE FROM user_flow WHERE flow=:flow RETURNING *",
				Map.of("user", flow),
				rowMapper
		);
	}

	@Override
	public Iterable<UserFlow> deleteAll() {
		return jdbcTemplate.query(
				"DELETE FROM user_flow RETURNING *",
				rowMapper
		);
	}

}
