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
				"INSERT INTO users(login, password, admin) VALUES (:login, :password, :admin) RETURNING *",
				new BeanPropertySqlParameterSource(user),
				rowMapper
		);
	}

	@Override
	public User update(User user) {
		return jdbcTemplate.queryForObject(
				"UPDATE users SET password=:password, admin=:admin WHERE login=:login RETURNING *",
				new BeanPropertySqlParameterSource(user),
				rowMapper
		);
	}
	
	@Override
	public Iterable<User> findAll() {
		return jdbcTemplate.query(
				"SELECT * FROM users",
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
	public Iterable<User> deleteAll() {
		return jdbcTemplate.query(
				"DELETE FROM users RETURNING *",
				rowMapper
		);
	}

	@Override
	public Optional<User> findById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM users WHERE id=:id",
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
								"SELECT * FROM users WHERE login=:login",
								Map.of("login", login),
								rowMapper
						)
				)
		);
	}
}
