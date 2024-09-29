package ru.khanin.dmitrii.schedule.repo.jdbc;

import java.util.Map;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.repo.FlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.mapper.FlowRowMapper;

@Repository
@RequiredArgsConstructor
public class JdbcFlowRepo implements FlowRepo {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Flow> rowMapper = new FlowRowMapper();
	
	@Override
	public Flow add(Flow flow) {
		return jdbcTemplate.queryForObject(
				"INSERT INTO flow(education_level, course, _group, subgroup, last_edit"
				+ (flow.getLessonsStartDate() != null ? ", lessons_start_date" : "")
				+ (flow.getSessionStartDate() != null ? ", session_start_date" : "")
				+ (flow.getSessionEndDate() != null ? ", session_end_date" : "")
				+ ", active)"
				+ " VALUES (:educationLevel, :course, :group, :subgroup, :lastEdit"
				+ (flow.getLessonsStartDate() != null ? ", :lessonsStartDate" : "")
				+ (flow.getSessionStartDate() != null ? ", :sessionStartDate" : "")
				+ (flow.getSessionEndDate() != null ? ", :sessionEndDate" : "")
				+ ", :active) RETURNING *",
				new BeanPropertySqlParameterSource(flow),
				rowMapper
		);
	}
	
	@Override
	public Flow update(Flow flow) {
		return jdbcTemplate.queryForObject(
				"UPDATE flow SET last_edit=:lastEdit, lessons_start_date=:lessonsStartDate,"
				+ " session_start_date=:sessionStartDate, session_end_date=:sessionEndDate, active=:active"
				+ " WHERE education_level=:educationLevel AND course=:course AND _group=:group"
				+ " AND subgroup=:subgroup RETURNING *",
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
	public Optional<Flow> findByEducationLevelAndCourseAndGroupAndSubgroup(
			int educationLevel, int course, int group, int subgroup
	) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM flow"
								+ " WHERE education_level=:educationLevel AND course=:course AND _group=:group AND subgroup=:subgroup",
								Map.of(
										"educationLevel", educationLevel,
										"course", course,
										"group", group,
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
	public Iterable<Flow> findAllActive() {
		return jdbcTemplate.query(
				"SELECT * FROM flow WHERE active=true",
				rowMapper
		);
	}
	
	@Override
	public Iterable<Flow> findAllByEducationLevel(int educationLevel) {
		return jdbcTemplate.query(
				"SELECT * FROM flow WHERE education_level=:educationLevel",
				Map.of("educationLevel", educationLevel),
				rowMapper
		);
	}
	
	@Override
	public Iterable<Flow> findAllByEducationLevelAndCourse(int educationLevel, int course) {
		return jdbcTemplate.query(
				"SELECT * FROM flow WHERE education_level=:educationLevel AND course=:course",
				Map.of(
						"educationLevel", educationLevel,
						"course", course
				),
				rowMapper
		);
	}
	
	@Override
	public Iterable<Flow> findAllByEducationLevelAndCourseAndGroup(int educationLevel, int course, int group) {
		return jdbcTemplate.query(
				"SELECT * FROM flow WHERE education_level=:educationLevel AND course=:course AND _group=:group",
				Map.of(
						"educationLevel", educationLevel,
						"course", course,
						"group", group
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
