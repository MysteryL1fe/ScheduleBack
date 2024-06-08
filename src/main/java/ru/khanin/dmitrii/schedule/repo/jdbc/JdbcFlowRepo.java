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
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.repo.FlowRepo;

@Repository
@RequiredArgsConstructor
public class JdbcFlowRepo implements FlowRepo {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Flow> rowMapper = new DataClassRowMapper<>(Flow.class);
	
	@Override
	public Flow add(Flow flow) {
		return jdbcTemplate.queryForObject(
				"INSERT INTO flow(flow_lvl, course, flow, subgroup) VALUES (:flowLvl, :course, :flow, :subgroup) RETURNING *",
				new BeanPropertySqlParameterSource(flow),
				rowMapper
		);
	}

	@Override
	public Optional<Flow> findById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM flow WHERE id=:id",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Optional<Flow> findByFlowLvlAndCourseAndFlowAndSubgroup(int flowLvl, int course, int flow, int subgroup) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM flow WHERE flow_lvl=:flowLvl AND course=:course AND flow=:flow AND subgroup=:subgroup",
								Map.of(
										"flowLvl", flowLvl,
										"course", course,
										"flow", flow,
										"subgroup", subgroup
								),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<Flow> findAll() {
		return jdbcTemplate.query(
				"SELECT * FROM flow",
				rowMapper
		);
	}
	
	@Override
	public Iterable<Flow> findAllByFlowLvl(int flowLvl) {
		return jdbcTemplate.query(
				"SELECT * FROM flow WHERE flow_lvl=:flowLvl",
				Map.of("flow_lvl", flowLvl),
				rowMapper
		);
	}
	
	@Override
	public Iterable<Flow> findAllByFlowLvlAndCourse(int flowLvl, int course) {
		return jdbcTemplate.query(
				"SELECT * FROM flow WHERE flow_lvl=:flowLvl AND course=:course",
				Map.of(
						"flow_lvl", flowLvl,
						"course", course
				),
				rowMapper
		);
	}
	
	@Override
	public Iterable<Flow> findAllByFlowLvlAndCourseAndFlow(int flowLvl, int course, int flow) {
		return jdbcTemplate.query(
				"SELECT * FROM flow WHERE flow_lvl=:flowLvl AND course=:course AND flow=:flow",
				Map.of(
						"flow_lvl", flowLvl,
						"course", course,
						"flow", flow
				),
				rowMapper
		);
	}

	@Override
	public Optional<Flow> deleteById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"DELETE FROM flow WHERE id=:id RETURNING *",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}

	@Override
	public Iterable<Flow> deleteAll() {
		return jdbcTemplate.query(
				"DELETE FROM flow RETURNING *",
				rowMapper
		);
	}

}
