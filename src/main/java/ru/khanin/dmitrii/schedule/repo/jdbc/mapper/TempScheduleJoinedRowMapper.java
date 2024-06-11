package ru.khanin.dmitrii.schedule.repo.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Lesson;
import ru.khanin.dmitrii.schedule.entity.jdbc.TempScheduleJoined;

public class TempScheduleJoinedRowMapper implements RowMapper<TempScheduleJoined> {

	@Override
	public TempScheduleJoined mapRow(ResultSet rs, int rowNum) throws SQLException {
		Flow flow = new Flow();
		flow.setId(rs.getLong("flow_id"));
		flow.setFlowLvl(rs.getInt("flow_lvl"));
		flow.setCourse(rs.getInt("course"));
		flow.setFlow(rs.getInt("flow"));
		flow.setSubgroup(rs.getInt("subgroup"));
		
		Lesson lesson = new Lesson();
		lesson.setId(rs.getLong("lesson_id"));
		lesson.setName(rs.getString("name"));
		lesson.setTeacher(rs.getString("teacher"));
		lesson.setCabinet(rs.getString("cabinet"));
		
		TempScheduleJoined result = new TempScheduleJoined();
		result.setId(rs.getLong("temp_schedule_id"));
		result.setFlow(rs.getLong("flow_id"));
		result.setFlowJoined(flow);
		result.setLesson(rs.getLong("leson_id"));
		result.setLessonJoined(lesson);
		result.setLessonDate(rs.getDate("lesson_date").toLocalDate());
		result.setLessonNum(rs.getInt("lesson_num"));
		result.setWillLessonBe(rs.getBoolean("will_lesson_be"));
		
		return result;
	}

}
