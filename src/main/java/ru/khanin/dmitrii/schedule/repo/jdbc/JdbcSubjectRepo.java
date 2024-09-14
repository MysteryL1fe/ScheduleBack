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
import ru.khanin.dmitrii.schedule.entity.Subject;
import ru.khanin.dmitrii.schedule.repo.SubjectRepo;

@Repository
@RequiredArgsConstructor
public class JdbcSubjectRepo implements SubjectRepo {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Subject> rowMapper = new DataClassRowMapper<>(Subject.class);

	@Override
	public Subject add(Subject subject) {
		return jdbcTemplate.queryForObject(
				"INSERT INTO subject (subject) VALUES (:subject) RETURNING *",
				new BeanPropertySqlParameterSource(subject),
				rowMapper
		);
	}

	@Override
	public Optional<Subject> findById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM subject WHERE id=:id",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}

	@Override
	public Optional<Subject> findBySubject(String subject) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM subject WHERE subject=:subject",
								Map.of("subject", subject),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<Subject> findAll() {
		return jdbcTemplate.query(
				"SELECT * FROM subject",
				rowMapper
		);
	}

	@Override
	public Iterable<Subject> findAllWhereSubjectStartsWith(String subject) {
		return jdbcTemplate.query(
				"SELECT * FROM subject WHERE starts_with(LOWER(subject), LOWER(:subject))",
				Map.of("subject", subject),
				rowMapper
		);
	}

	@Override
	public Optional<Subject> deleteById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"DELETE FROM subject WHERE id=:id RETURNING *",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}

	@Override
	public Optional<Subject> deleteBySubject(String subject) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"DELETE FROM subject WHERE subject=:subject RETURNING *",
								Map.of("subject", subject),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<Subject> deleteAll() {
		return jdbcTemplate.query(
				"DELETE FROM subject RETURNING *",
				rowMapper
		);
	}

}
