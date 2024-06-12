package ru.khanin.dmitrii.schedule.repo.jdbc;

import java.util.Map;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.User;
import ru.khanin.dmitrii.schedule.repo.UserRepo;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepo implements UserRepo {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<User> rowMapper = new DataClassRowMapper<>(User.class);
	
	@Override
	public User add(User user) {
		return jdbcTemplate.queryForObject(
				"INSERT INTO users(api_key, name, access, flow) VALUES (:apiKey, :name, :access, :flow) RETURNING *",
				new BeanPropertySqlParameterSource(user),
				rowMapper
		);
	}
	
	@Override
	public Iterable<? extends User> findAll() {
		return jdbcTemplate.query(
				"SELECT * FROM users",
				rowMapper
		);
	}
	
	@Override
	public Iterable<? extends User> findAllByApiKey(String apiKey) {
		return jdbcTemplate.query(
				"SELECT * FROM users WHERE api_key=:apiKey",
				Map.of("apiKey", apiKey),
				rowMapper
		);
	}
	
	@Override
	public Optional<User> deleteById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"DELETE FROM users WHERE id=:id RETURNING *",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Iterable<? extends User> deleteAll() {
		return jdbcTemplate.query(
				"DELETE FROM users RETURNING *",
				rowMapper
		);
	}
	
	@Override
	public Iterable<? extends User> deleteAllWhereApiKeyIsNotNull() {
		return jdbcTemplate.query(
				"DELETE FROM flow WHERE users IS NOT NULL RETURNING *",
				rowMapper
		);
	}
	
	@Override
	public Iterable<? extends User> deleteAllByApiKey(String apiKey) {
		return jdbcTemplate.query(
				"DELETE FROM users WHERE api_key=:apiKey RETURNING *",
				Map.of("apiKey", apiKey),
				rowMapper
		);
	}
	
	@Override
	public Iterable<? extends User> deleteAllByFlow(long flow) {
		return jdbcTemplate.query(
				"DELETE FROM users WHERE flow=:flow RETURNING *",
				Map.of("flow", flow),
				rowMapper
		);
	}
}
