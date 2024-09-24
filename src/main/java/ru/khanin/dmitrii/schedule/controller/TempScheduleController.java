package ru.khanin.dmitrii.schedule.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.khanin.dmitrii.schedule.dto.cabinet.CabinetResponse;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.dto.subject.SubjectResponse;
import ru.khanin.dmitrii.schedule.dto.teacher.TeacherResponse;
import ru.khanin.dmitrii.schedule.dto.temp.DeleteTempScheduleRequest;
import ru.khanin.dmitrii.schedule.dto.temp.TempScheduleRequest;
import ru.khanin.dmitrii.schedule.dto.temp.TempScheduleResponse;
import ru.khanin.dmitrii.schedule.entity.Cabinet;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Subject;
import ru.khanin.dmitrii.schedule.entity.Teacher;
import ru.khanin.dmitrii.schedule.entity.TempSchedule;
import ru.khanin.dmitrii.schedule.entity.jdbc.TempScheduleJoined;
import ru.khanin.dmitrii.schedule.service.CabinetService;
import ru.khanin.dmitrii.schedule.service.FlowService;
import ru.khanin.dmitrii.schedule.service.SubjectService;
import ru.khanin.dmitrii.schedule.service.TeacherService;
import ru.khanin.dmitrii.schedule.service.TempScheduleService;

@RestController
@RequestMapping("/temp")
@RequiredArgsConstructor
@Slf4j
public class TempScheduleController {
	private final TempScheduleService tempScheduleService;
	private final FlowService flowService;
	private final SubjectService subjectService;
	private final TeacherService teacherService;
	private final CabinetService cabinetService;
	
	@GetMapping("/schedule")
	public ResponseEntity<TempScheduleResponse> getTempSchedule(
			@RequestParam(name = "education_level") int educationLevel, @RequestParam int course,
			@RequestParam int group, @RequestParam int subgroup, @RequestParam(name = "lesson_date") Date lessonDate,
			@RequestParam(name = "lesson_num") int lessonNum
	) {
		log.trace(String.format(
				"Received request to get temp schedule (flow: %s.%s.%s.%s, lesson date: %s, lesson num: %s)",
				educationLevel, course, group, subgroup, lessonDate, lessonNum
		));
		
		TempSchedule found = tempScheduleService.findByFlowAndLessonDateAndLessonNum(
				educationLevel, course, group, subgroup, lessonDate.toLocalDate(), lessonNum
		);
		TempScheduleResponse response = convertTempScheduleToResponse(found);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<TempScheduleResponse>> getAllTempSchedules() {
		log.trace("Received request to get all temp schedules");
		
		Collection<TempSchedule> found = tempScheduleService.findAll();
		List<TempScheduleResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertTempScheduleToResponse(e)));
		
		log.trace(String.format("Found all (%s) temp schedules: %s", found.size(), found));
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/flow")
	public ResponseEntity<List<TempScheduleResponse>> getAllTempSchedulesByFlow(
			@RequestParam(name = "education_level") int educationLevel, @RequestParam int course,
			@RequestParam int group, @RequestParam int subgroup
	) {
		log.trace(String.format(
				"Received request to get all temp schedules by flow %s.%s.%s.%s",
				educationLevel, course, group, subgroup
		));
		
		Collection<TempSchedule> found = tempScheduleService.findAllByFlow(educationLevel, course, group, subgroup);
		List<TempScheduleResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertTempScheduleToResponse(e)));
		
		log.trace(String.format(
				"Found all (%s) temp schedules by flow %s.%s.%s.%s: %s", 
				found.size(), educationLevel, course, group, subgroup, found
		));
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/schedule")
	public ResponseEntity<?> addTempSchedule(@RequestBody TempScheduleRequest tempSchedule) {
		log.trace(String.format("Received request to add temp schedule %s", tempSchedule));
		
		TempSchedule addedSchedule;
		if (tempSchedule.subject() == null) {
			addedSchedule = tempScheduleService.addOrUpdate(
					tempSchedule.flow().education_level(), tempSchedule.flow().course(), tempSchedule.flow().group(),
					tempSchedule.flow().subgroup(), tempSchedule.lesson_date(), tempSchedule.lesson_num(),
					tempSchedule.will_lesson_be()
			);
		} else if (tempSchedule.teacher() == null && tempSchedule.cabinet() == null) {
			addedSchedule = tempScheduleService.addOrUpdate(
					tempSchedule.flow().education_level(), tempSchedule.flow().course(), tempSchedule.flow().group(),
					tempSchedule.flow().subgroup(), tempSchedule.subject().subject(), tempSchedule.lesson_date(),
					tempSchedule.lesson_num(), tempSchedule.will_lesson_be()
			);
		} else if (tempSchedule.teacher() == null) {
			addedSchedule = tempScheduleService.addOrUpdate(
					tempSchedule.flow().education_level(), tempSchedule.flow().course(), tempSchedule.flow().group(),
					tempSchedule.flow().subgroup(), tempSchedule.subject().subject(),
					tempSchedule.cabinet().cabinet(), tempSchedule.cabinet().building(), tempSchedule.lesson_date(),
					tempSchedule.lesson_num(), tempSchedule.will_lesson_be()
			);
		} else if (tempSchedule.cabinet() == null) {
			addedSchedule = tempScheduleService.addOrUpdate(
					tempSchedule.flow().education_level(), tempSchedule.flow().course(), tempSchedule.flow().group(),
					tempSchedule.flow().subgroup(), tempSchedule.subject().subject(), tempSchedule.teacher().surname(),
					tempSchedule.teacher().name(), tempSchedule.teacher().patronymic(), tempSchedule.lesson_date(),
					tempSchedule.lesson_num(), tempSchedule.will_lesson_be()
			);
		} else {
			addedSchedule = tempScheduleService.addOrUpdate(
					tempSchedule.flow().education_level(), tempSchedule.flow().course(), tempSchedule.flow().group(),
					tempSchedule.flow().subgroup(), tempSchedule.subject().subject(), tempSchedule.teacher().surname(),
					tempSchedule.teacher().name(), tempSchedule.teacher().patronymic(),
					tempSchedule.cabinet().cabinet(), tempSchedule.cabinet().building(), tempSchedule.lesson_date(),
					tempSchedule.lesson_num(), tempSchedule.will_lesson_be()
			);
		}
		
		log.trace(String.format("Successfully added temp schedule %s", addedSchedule));
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/schedules")
	@Transactional
	public ResponseEntity<?> addTempSchedules(@RequestBody List<TempScheduleRequest> tempSchedules) {
		log.trace(String.format(
				"Received request to add %s temp schedules %s", tempSchedules.size(), tempSchedules
		));
		
		List<TempSchedule> addedSchedules = new ArrayList<>();
		for (TempScheduleRequest tempSchedule : tempSchedules) {
			TempSchedule addedSchedule = tempScheduleService.addOrUpdate(
					tempSchedule.flow().education_level(), tempSchedule.flow().course(), tempSchedule.flow().group(),
					tempSchedule.flow().subgroup(), tempSchedule.subject().subject(), tempSchedule.teacher().surname(),
					tempSchedule.teacher().name(), tempSchedule.teacher().patronymic(),
					tempSchedule.cabinet().cabinet(), tempSchedule.cabinet().building(), tempSchedule.lesson_date(),
					tempSchedule.lesson_num(), tempSchedule.will_lesson_be()
			);
			addedSchedules.add(addedSchedule);
		}
		
		log.trace(String.format(
				"Successfully added %s temp schedules: %s", addedSchedules.size(), addedSchedules
		));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/schedule")
	public ResponseEntity<?> deleteTempSchedule(@RequestBody DeleteTempScheduleRequest tempSchedule) {
		log.trace(String.format("Received request to delete temp schedule %s", tempSchedule));
		
		TempSchedule deletedSchedule = tempScheduleService.deleteByFlowAndLessonDateAndLessonNum(
				tempSchedule.flow().education_level(), tempSchedule.flow().course(), tempSchedule.flow().group(),
				tempSchedule.flow().subgroup(), tempSchedule.lesson_date(), tempSchedule.lesson_num()
		);
		
		log.trace(String.format("Successfully deleted temp schedule %s", deletedSchedule));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/schedules")
	@Transactional
	public ResponseEntity<?> deleteTempSchedules(@RequestBody List<DeleteTempScheduleRequest> tempSchedules) {
		log.trace(String.format(
				"Received request to delete %s temp schedules: %s", tempSchedules.size(), tempSchedules
		));
		
		List<TempSchedule> deletedSchedules = new ArrayList<>();
		for (DeleteTempScheduleRequest tempSchedule : tempSchedules) {
			TempSchedule deletedSchedule = tempScheduleService.deleteByFlowAndLessonDateAndLessonNum(
					tempSchedule.flow().education_level(), tempSchedule.flow().course(), tempSchedule.flow().group(),
					tempSchedule.flow().subgroup(), tempSchedule.lesson_date(), tempSchedule.lesson_num()
			);
			deletedSchedules.add(deletedSchedule);
		}
		
		log.trace(String.format(
				"Successfully deleted %s temp schedules: %s", deletedSchedules.size(), deletedSchedules
		));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllTempSchedules() {
		log.trace(String.format("Received request to delete all temp schedules"));
		
		Collection<TempSchedule> deletedSchedules = tempScheduleService.deleteAll();

		log.trace(String.format(
				"Successfully deleted all (%s) temp schedules: %s", deletedSchedules.size(), deletedSchedules
		));
		
		return ResponseEntity.ok().build();
	}
	
	private TempScheduleResponse convertTempScheduleToResponse(TempSchedule tempSchedule) {
		if (tempSchedule instanceof TempScheduleJoined) {
			TempScheduleJoined schedule = (TempScheduleJoined) tempSchedule;
			
			FlowResponse flowResponse = new FlowResponse(
					schedule.getFlowJoined().getEducationLevel(), schedule.getFlowJoined().getCourse(),
					schedule.getFlowJoined().getGroup(), schedule.getFlowJoined().getSubgroup(),
					schedule.getFlowJoined().getLastEdit(), schedule.getFlowJoined().getLessonsStartDate(),
					schedule.getFlowJoined().getSessionStartDate(), schedule.getFlowJoined().getSessionEndDate(),
					schedule.getFlowJoined().getActive()
			);
			
			SubjectResponse subjectResponse = new SubjectResponse(schedule.getSubjectJoined().getSubject());
			
			TeacherResponse teacherResponse = new TeacherResponse(
					schedule.getTeacherJoined().getSurname(), schedule.getTeacherJoined().getName(),
					schedule.getTeacherJoined().getPatronymic()
			);
			
			CabinetResponse cabinetResponse = new CabinetResponse(
					schedule.getCabinetJoined().getCabinet(), schedule.getCabinetJoined().getBuilding(),
					schedule.getCabinetJoined().getAddress()
			);
			
			return new TempScheduleResponse(
					flowResponse, schedule.getLessonDate(), schedule.getLessonNum(), schedule.getWillLessonBe(),
					subjectResponse, teacherResponse, cabinetResponse
			);
		} else {
			Flow flow = flowService.findById(tempSchedule.getFlow());
			FlowResponse flowResponse = new FlowResponse(
					flow.getEducationLevel(), flow.getCourse(), flow.getGroup(), flow.getSubgroup(),
					flow.getLastEdit(), flow.getLessonsStartDate(), flow.getSessionStartDate(),
					flow.getSessionEndDate(), flow.getActive()
			);
			
			Subject subject = subjectService.findById(tempSchedule.getSubject());
			SubjectResponse subjectResponse = new SubjectResponse(subject.getSubject());
			
			Teacher teacher = teacherService.findById(tempSchedule.getTeacher());
			TeacherResponse teacherResponse = new TeacherResponse(
					teacher.getSurname(), teacher.getName(),
					teacher.getPatronymic()
			);
			
			Cabinet cabinet = cabinetService.findById(tempSchedule.getCabinet());
			CabinetResponse cabinetResponse = new CabinetResponse(
					cabinet.getCabinet(), cabinet.getBuilding(),
					cabinet.getAddress()
			);
			
			return new TempScheduleResponse(
					flowResponse, tempSchedule.getLessonDate(), tempSchedule.getLessonNum(),
					tempSchedule.getWillLessonBe(), subjectResponse, teacherResponse, cabinetResponse
			);
		}
	}
}
