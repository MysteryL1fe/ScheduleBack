package ru.khanin.dmitrii.schedule.repo.jdbc;

import java.util.Map;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.User;
import ru.khanin.dmitrii.schedule.repo.UserRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.mapper.UserRowMapper;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepo implements UserRepo {
	private final String SELECT_COLUMNS = "u.id AS user_id, u.login, u.admin,"
			+ " array_agg(f.id ORDER BY f.id) AS flow_ids,"
			+ " array_agg(f.education_level ORDER BY f.id) AS flow_education_levels,"
			+ " array_agg(f.course ORDER BY f.id) AS flow_courses,"
			+ " array_agg(f._group ORDER BY f.id) AS flow_groups,"
			+ " array_agg(f.subgroup ORDER BY f.id) AS flow_subgroups,"
			+ " array_agg(f.last_edit ORDER BY f.id) AS flow_last_edits,"
			+ " array_agg(f.lessons_start_date ORDER BY f.id) AS flow_lessons_starts,"
			+ " array_agg(f.session_start_date ORDER BY f.id) AS flow_session_starts,"
			+ " array_agg(f.session_end_date ORDER BY f.id) AS flow_session_ends,"
			+ " array_agg(f.active ORDER BY f.id) AS flow_actives";
	
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<User> rowMapper = new UserRowMapper();
	
	@Override
	public User add(User user) {
		return jdbcTemplate.queryForObject(
				"WITH u AS (INSERT INTO users(login, password, admin) VALUES (:login, :password, :admin)) RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM u"
						+ " JOIN user_flow uf ON uf._user = u.id JOIN flow f ON uf.flow = f.id"
						+ " GROUP BY u.id, u.login, u.admin",
				new BeanPropertySqlParameterSource(user),
				rowMapper
		);
	}

	@Override
	public User update(User user) {
		return jdbcTemplate.queryForObject(
				"WUTH u AS (UPDATE users SET password = :password, admin = :admin WHERE login = :login RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM u"
						+ " JOIN user_flow uf ON uf._user = u.id JOIN flow f ON uf.flow = f.id"
						+ " GROUP BY u.id, u.login, u.admin",
				new BeanPropertySqlParameterSource(user),
				rowMapper
		);
	}
	
	@Override
	public Iterable<User> findAll() {
		return jdbcTemplate.query(
				"SELECT " + SELECT_COLUMNS + " FROM users u"
						+ " JOIN user_flow uf ON uf._user = u.id JOIN flow f ON uf.flow = f.id"
						+ " GROUP BY u.id, u.login, u.admin",
				rowMapper
		);
	}
	
	@Override
	public Optional<User> deleteById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"WITH u AS (DELETE FROM users WHERE id=:id RETURNING *)"
										+ " SELECT " + SELECT_COLUMNS + " FROM u"
										+ " JOIN user_flow uf ON uf._user = u.id JOIN flow f ON uf.flow = f.id"
										+ " GROUP BY u.id, u.login, u.admin",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Iterable<User> deleteAll() {
		return jdbcTemplate.query(
				"WITH u AS (DELETE FROM users RETURNING *)"
						+ " SELECT " + SELECT_COLUMNS + " FROM u"
						+ " JOIN user_flow uf ON uf._user = u.id JOIN flow f ON uf.flow = f.id"
						+ " GROUP BY u.id, u.login, u.admin",
				rowMapper
		);
	}

	@Override
	public Optional<User> findById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT " + SELECT_COLUMNS + " FROM users u"
										+ " JOIN user_flow uf ON uf._user = u.id JOIN flow f ON uf.flow = f.id"
										+ " WHERE u.id = :id"
										+ " GROUP BY u.id, u.login, u.admin",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}

	@Override
	public Optional<User> findByLogin(String login) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT " + SELECT_COLUMNS + " FROM users u"
										+ " JOIN user_flow uf ON uf._user = u.id JOIN flow f ON uf.flow = f.id"
										+ " WHERE login = :login"
										+ " GROUP BY u.id, u.login, u.admin",
								Map.of("login", login),
								rowMapper
						)
				)
		);
	}
}
