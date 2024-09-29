package ru.khanin.dmitrii.schedule.repo.jdbc;

import java.util.Map;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.UserFlow;
import ru.khanin.dmitrii.schedule.repo.UserFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.mapper.UserFlowRowMapper;

@Repository
@RequiredArgsConstructor
public class JdbcUserFlowRepo implements UserFlowRepo {
	private final String SELECT_COLUMNS = "uf._user AS user_id, uf.flow AS flow_id, u.*, f.*";
	private final String JOIN_USER_TABLE = "JOIN (SELECT u.id, u.login, u.admin,"
			+ " array_agg(f.id ORDER BY f.id) AS flow_ids,"
			+ " array_agg(f.education_level ORDER BY f.id) AS flow_education_levels,"
			+ " array_agg(f.course ORDER BY f.id) AS flow_courses,"
			+ " array_agg(f._group ORDER BY f.id) AS flow_groups,"
			+ " array_agg(f.subgroup ORDER BY f.id) AS flow_subgroups,"
			+ " array_agg(f.last_edit ORDER BY f.id) AS flow_last_edits,"
			+ " array_agg(f.lessons_start_date ORDER BY f.id) AS flow_lessons_starts,"
			+ " array_agg(f.session_start_date ORDER BY f.id) AS flow_session_starts,"
			+ " array_agg(f.session_end_date ORDER BY f.id) AS flow_session_ends,"
			+ " array_agg(f.active ORDER BY f.id) AS flow_actives"
			+ " FROM users u JOIN user_flow uf ON uf._user = u.id JOIN flow f ON uf.flow = f.id"
			+ " GROUP BY u.id, u.login, u.admin) AS u ON uf._user = u.id";
	
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<UserFlow> rowMapper = new UserFlowRowMapper();

	@Override
	public UserFlow add(UserFlow userFlow) {
		return jdbcTemplate.queryForObject(
				"WITH uf AS (INSERT INTO user_flow (_user, flow) VALUES (:user, :flow) RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM uf "
						+ JOIN_USER_TABLE + " JOIN flow f ON uf.flow = f.id",
				Map.of(
						"user", userFlow.getUser().getId(),
						"flow", userFlow.getFlow().getId()
				),
				rowMapper
		);
	}

	@Override
	public Optional<UserFlow> findByUserAndFlow(long user, long flow) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT " + SELECT_COLUMNS + " FROM user_flow uf "
										+ JOIN_USER_TABLE + " JOIN flow f ON uf.flow = f.id"
										+ " WHERE _user = :user AND flow = :flow",
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
				"SELECT " + SELECT_COLUMNS + " FROM user_flow uf "
						+ JOIN_USER_TABLE + " JOIN flow f ON uf.flow = f.id",
				rowMapper
		);
	}

	@Override
	public Iterable<UserFlow> findAllByUser(long user) {
		return jdbcTemplate.query(
				"SELECT " + SELECT_COLUMNS + " FROM user_flow uf "
						+ JOIN_USER_TABLE + " JOIN flow f ON uf.flow = f.id"
						+ " WHERE _user = :user",
				Map.of("user", user),
				rowMapper
		);
	}

	@Override
	public Iterable<? extends UserFlow> findAllByFlow(long flow) {
		return jdbcTemplate.query(
				"SELECT " + SELECT_COLUMNS + " FROM user_flow uf "
						+ JOIN_USER_TABLE + " JOIN flow f ON uf.flow = f.id"
						+ " WHERE flow = :flow",
				Map.of("flow", flow),
				rowMapper
		);
	}

	@Override
	public Optional<UserFlow> deleteByUserAndFlow(long user, long flow) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"WITH uf AS (DELETE FROM user_flow WHERE _user=:user AND flow=:flow RETURNING *)"
										+ " SELECT " + SELECT_COLUMNS + " FROM uf "
										+ JOIN_USER_TABLE + " JOIN flow f ON uf.flow = f.id",
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
				"WITH uf AS (DELETE FROM user_flow WHERE _user=:user RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM uf "
						+ JOIN_USER_TABLE + " JOIN flow f ON uf.flow = f.id",
				Map.of("user", user),
				rowMapper
		);
	}

	@Override
	public Iterable<UserFlow> deleteAllByFlow(long flow) {
		return jdbcTemplate.query(
				"WITH uf AS (DELETE FROM user_flow WHERE flow=:flow RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM uf "
						+ JOIN_USER_TABLE + " JOIN flow f ON uf.flow = f.id",
				Map.of("user", flow),
				rowMapper
		);
	}

	@Override
	public Iterable<UserFlow> deleteAll() {
		return jdbcTemplate.query(
				"WITH uf AS (DELETE FROM user_flow RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM uf "
						+ JOIN_USER_TABLE + " JOIN flow f ON uf.flow = f.id",
				rowMapper
		);
	}

}
