package ru.khanin.dmitrii.schedule.controller;

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
import ru.khanin.dmitrii.schedule.dto.schedule.DeleteScheduleRequest;
import ru.khanin.dmitrii.schedule.dto.schedule.ScheduleRequest;
import ru.khanin.dmitrii.schedule.dto.schedule.ScheduleResponse;
import ru.khanin.dmitrii.schedule.dto.subject.SubjectResponse;
import ru.khanin.dmitrii.schedule.dto.teacher.TeacherResponse;
import ru.khanin.dmitrii.schedule.entity.Schedule;
import ru.khanin.dmitrii.schedule.service.ScheduleService;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
	private final ScheduleService scheduleService;
	
	@GetMapping("/schedule")
	public ResponseEntity<ScheduleResponse> getSchedule(
			@RequestParam(name = "education_level") int educationLevel, @RequestParam int course,
			@RequestParam int group, @RequestParam int subgroup, @RequestParam(name = "day_of_week") int dayOfWeek,
			@RequestParam(name = "lesson_num") int lessonNum, @RequestParam boolean numerator
	) {
		log.trace(String.format(
				"Received request to get temp schedule"
				+ " (flow: %s.%s.%s.%s, day of week: %s, lesson num: %s, numerator: %s)",
				educationLevel, course, group, subgroup, dayOfWeek, lessonNum, numerator
		));
		
		Schedule found = scheduleService.findByFlowAndDayOfWeekAndLessonNumAndNumerator(
				educationLevel, course, group, subgroup, dayOfWeek, lessonNum, numerator
		);
		ScheduleResponse response = convertScheduleToResponse(found);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<ScheduleResponse>> getAllSchedules() {
		log.trace("Received request to get all schedules");
		
		Collection<Schedule> found = scheduleService.findAll();
		List<ScheduleResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertScheduleToResponse(e)));
		
		log.trace(String.format("Found all (%s) schedules: %s", found.size(), found));
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/flow")
	public ResponseEntity<List<ScheduleResponse>> getAllSchedulesByFlow(
			@RequestParam(name = "education_level") int educationLevel, @RequestParam int course,
			@RequestParam int group, @RequestParam int subgroup
	) {
		log.trace(String.format(
				"Received request to get all schedules by flow %s.%s.%s.%s", educationLevel, course, group, subgroup
		));
		
		Collection<Schedule> found = scheduleService.findAllByFlow(educationLevel, course, group, subgroup);
		List<ScheduleResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertScheduleToResponse(e)));
		
		log.trace(String.format(
				"Found all (%s) schedules by flow %s.%s.%s.%s: %s", found.size(), educationLevel, course, group, subgroup,
				found
		));
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/teacher")
	public ResponseEntity<List<ScheduleResponse>> getAllScheduleByTeacher(
			@RequestParam String surname, @RequestParam String name, @RequestParam String patronymic
	) {
		log.trace(String.format(
				"Received request to get all schedules by teacher %s %s %s",
				surname, name, patronymic
		));
		
		Collection<Schedule> found = scheduleService.findAllByTeacher(surname, name, patronymic);
		List<ScheduleResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertScheduleToResponse(e)));
		
		log.trace(String.format(
				"Found all (%s) schedules by teacher %s %s %s: %s",
				found.size(), surname, name, patronymic, found
		));
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/schedule")
	public ResponseEntity<?> addSchedule(@RequestBody ScheduleRequest schedule) {
		log.trace(String.format("Received request to add schedule %s", schedule));
		
		Schedule addedSchedule;
		if (schedule.teacher() == null && schedule.cabinet() == null) {
			addedSchedule = scheduleService.addOrUpdate(
					schedule.flow().education_level(), schedule.flow().course(), schedule.flow().group(),
					schedule.flow().subgroup(), schedule.subject().subject(), schedule.day_of_week(),
					schedule.lesson_num(), schedule.numerator()
			);
		} else if (schedule.teacher() == null) {
			addedSchedule = scheduleService.addOrUpdate(
					schedule.flow().education_level(), schedule.flow().course(), schedule.flow().group(),
					schedule.flow().subgroup(), schedule.subject().subject(), schedule.cabinet().cabinet(),
					schedule.cabinet().building(), schedule.day_of_week(), schedule.lesson_num(), schedule.numerator()
			);
		} else if (schedule.cabinet() == null) {
			addedSchedule = scheduleService.addOrUpdate(
					schedule.flow().education_level(), schedule.flow().course(), schedule.flow().group(),
					schedule.flow().subgroup(), schedule.subject().subject(), schedule.teacher().surname(),
					schedule.teacher().name(), schedule.teacher().patronymic(), schedule.day_of_week(),
					schedule.lesson_num(), schedule.numerator()
			);
		} else {
			addedSchedule = scheduleService.addOrUpdate(
					schedule.flow().education_level(), schedule.flow().course(), schedule.flow().group(),
					schedule.flow().subgroup(), schedule.subject().subject(), schedule.teacher().surname(),
					schedule.teacher().name(), schedule.teacher().patronymic(), schedule.cabinet().cabinet(),
					schedule.cabinet().building(), schedule.day_of_week(), schedule.lesson_num(), schedule.numerator()
			);
		}
		
		log.trace(String.format("Successfully added schedule %s", addedSchedule));
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/schedules")
	@Transactional
	public ResponseEntity<?> addSchedules(@RequestBody List<ScheduleRequest> schedules) {
		log.trace(String.format(
				"Received request to add %s schedules %s", schedules.size(), schedules
		));
		
		List<Schedule> addedSchedules = new ArrayList<>();
		for (ScheduleRequest schedule : schedules) {
			Schedule addedSchedule;
			if (schedule.teacher() == null && schedule.cabinet() == null) {
				addedSchedule = scheduleService.addOrUpdate(
						schedule.flow().education_level(), schedule.flow().course(), schedule.flow().group(),
						schedule.flow().subgroup(), schedule.subject().subject(), schedule.day_of_week(),
						schedule.lesson_num(), schedule.numerator()
				);
			} else if (schedule.teacher() == null) {
				addedSchedule = scheduleService.addOrUpdate(
						schedule.flow().education_level(), schedule.flow().course(), schedule.flow().group(),
						schedule.flow().subgroup(), schedule.subject().subject(), schedule.cabinet().cabinet(),
						schedule.cabinet().building(), schedule.day_of_week(), schedule.lesson_num(), schedule.numerator()
				);
			} else if (schedule.cabinet() == null) {
				addedSchedule = scheduleService.addOrUpdate(
						schedule.flow().education_level(), schedule.flow().course(), schedule.flow().group(),
						schedule.flow().subgroup(), schedule.subject().subject(), schedule.teacher().surname(),
						schedule.teacher().name(), schedule.teacher().patronymic(), schedule.day_of_week(),
						schedule.lesson_num(), schedule.numerator()
				);
			} else {
				addedSchedule = scheduleService.addOrUpdate(
						schedule.flow().education_level(), schedule.flow().course(), schedule.flow().group(),
						schedule.flow().subgroup(), schedule.subject().subject(), schedule.teacher().surname(),
						schedule.teacher().name(), schedule.teacher().patronymic(), schedule.cabinet().cabinet(),
						schedule.cabinet().building(), schedule.day_of_week(), schedule.lesson_num(), schedule.numerator()
				);
			}
			addedSchedules.add(addedSchedule);
		}
		
		log.trace(String.format("Successfully added %s schedules: %s", addedSchedules.size(), addedSchedules));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/schedule")
	public ResponseEntity<?> deleteSchedule(@RequestBody DeleteScheduleRequest schedule) {
		log.trace(String.format("Received request to delete schedule %s", schedule));
		
		Schedule deletedSchedule = scheduleService.deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
				schedule.flow().education_level(), schedule.flow().course(), schedule.flow().group(),
				schedule.flow().subgroup(), schedule.day_of_week(), schedule.lesson_num(), schedule.numerator()
		);
		
		log.trace(String.format("Successfully deleted schedule %s", deletedSchedule));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/schedules")
	@Transactional
	public ResponseEntity<?> deleteSchedules(@RequestBody List<DeleteScheduleRequest> schedules
	) {
		log.trace(String.format(
				"Received request to delete %s schedules: %s", schedules.size(), schedules
		));
		
		List<Schedule> deletedSchedules = new ArrayList<>();
		for (DeleteScheduleRequest schedule : schedules) {
			Schedule deletedSchedule = scheduleService.deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
					schedule.flow().education_level(), schedule.flow().course(), schedule.flow().group(),
					schedule.flow().subgroup(), schedule.day_of_week(), schedule.lesson_num(), schedule.numerator()
			);
			deletedSchedules.add(deletedSchedule);
		}
		
		log.trace(String.format("Successfully deleted %s schedules: %s", deletedSchedules.size(), deletedSchedules));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllSchedules() {
		log.trace(String.format("Received request to delete all schedules"));
		
		Collection<Schedule> deletedSchedules = scheduleService.deleteAll();

		log.trace(String.format(
				"Successfully deleted all (%s) schedules: %s", deletedSchedules.size(), deletedSchedules
		));
		
		return ResponseEntity.ok().build();
	}
	
	private ScheduleResponse convertScheduleToResponse(Schedule schedule) {
		FlowResponse flowResponse = new FlowResponse(
				schedule.getFlow().getEducationLevel(), schedule.getFlow().getCourse(), schedule.getFlow().getGroup(),
				schedule.getFlow().getSubgroup(), schedule.getFlow().getLastEdit(),
				schedule.getFlow().getLessonsStartDate(), schedule.getFlow().getSessionStartDate(),
				schedule.getFlow().getSessionEndDate(), schedule.getFlow().getActive()
		);
		
		SubjectResponse subjectResponse = new SubjectResponse(schedule.getSubject().getSubject());
		
		TeacherResponse teacherResponse = new TeacherResponse(
				schedule.getTeacher().getSurname(), schedule.getTeacher().getName(),
				schedule.getTeacher().getPatronymic()
		);
		
		CabinetResponse cabinetResponse = new CabinetResponse(
				schedule.getCabinet().getCabinet(), schedule.getCabinet().getBuilding(),
				schedule.getCabinet().getAddress()
		);
		
		return new ScheduleResponse(
				flowResponse, schedule.getDayOfWeek(), schedule.getLessonNum(), schedule.getNumerator(),
				subjectResponse, teacherResponse, cabinetResponse
		);
	}
}
