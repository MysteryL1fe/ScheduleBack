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
import ru.khanin.dmitrii.schedule.entity.Cabinet;
import ru.khanin.dmitrii.schedule.repo.CabinetRepo;

@Repository
@RequiredArgsConstructor
public class JdbcCabinetRepo implements CabinetRepo {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Cabinet> rowMapper = new DataClassRowMapper<>(Cabinet.class);

	@Override
	public Cabinet add(Cabinet cabinet) {
		return jdbcTemplate.queryForObject(
				"INSERT INTO cabinet (cabinet, building, address) VALUES (:cabinet, :building, :address) RETURNING *",
				new BeanPropertySqlParameterSource(cabinet), 
				rowMapper
		);
	}

	@Override
	public Cabinet update(Cabinet cabinet) {
		return jdbcTemplate.queryForObject(
				"UPDATE cabinet SET address=:address WHERE cabinet=:cabinet AND building=:building RETURNING *",
				new BeanPropertySqlParameterSource(cabinet),
				rowMapper
		);
	}

	@Override
	public Optional<Cabinet> findById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM cabinet WHERE id=:id",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}

	@Override
	public Optional<Cabinet> findByCabinetAndBuilding(String cabinet, String building) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM cabinet WHERE cabinet=:cabinet AND building=:building",
								Map.of(
										"cabinet", cabinet,
										"building", building
								),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<Cabinet> findAll() {
		return jdbcTemplate.query(
				"SELECT * FROM cabinet",
				rowMapper
		);
	}

	@Override
	public Iterable<Cabinet> findAllByCabinet(String cabinet) {
		return jdbcTemplate.query(
				"SELECT * FROM cabinet WHERE cabinet=:cabinet",
				Map.of("cabinet", cabinet),
				rowMapper
		);
	}

	@Override
	public Iterable<Cabinet> findAllByBuilding(String building) {
		return jdbcTemplate.query(
				"SELECT * FROM cabinet WHERE building=:building",
				Map.of("building", building),
				rowMapper
		);
	}

	@Override
	public Iterable<Cabinet> findAllWhereCabinetStartsWith(String cabinet) {
		return jdbcTemplate.query(
				"SELECT * FROM cabinet WHERE starts_with(LOWER(cabinet), LOWER(:cabinet))",
				Map.of("cabinet", cabinet),
				rowMapper
		);
	}

	@Override
	public Optional<Cabinet> deleteById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"DELETE FROM cabinet WHERE id=:id RETURNING *",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<Cabinet> deleteAll() {
		return jdbcTemplate.query(
				"DELETE FROM cabinet RETURNING *",
				rowMapper
		);
	}

}
