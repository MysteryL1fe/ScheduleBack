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
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.dto.homework.DeleteHomeworkRequest;
import ru.khanin.dmitrii.schedule.dto.homework.HomeworkRequest;
import ru.khanin.dmitrii.schedule.dto.homework.HomeworkResponse;
import ru.khanin.dmitrii.schedule.dto.subject.SubjectResponse;
import ru.khanin.dmitrii.schedule.entity.Homework;
import ru.khanin.dmitrii.schedule.service.HomeworkService;

@RestController
@RequestMapping("/homework")
@RequiredArgsConstructor
@Slf4j
public class HomeworkController {
	private final HomeworkService homeworkService;
	
	@GetMapping("/homework")
	public ResponseEntity<HomeworkResponse> getHomework(
			@RequestParam(name = "education_level") int educationLevel, @RequestParam int course,
			@RequestParam int group, @RequestParam int subgroup, @RequestParam(name = "lesson_date") Date lessonDate,
			@RequestParam(name = "lesson_num") int lessonNum
	) {
		log.trace(String.format(
				"Received request to get homework"
				+ " (flow: %s.%s.%s.%s, lesson date: %s, lesson num: %s)",
				educationLevel, course, group, subgroup, lessonDate, lessonNum
		));
		
		Homework found = homeworkService.findByLessonDateAndLessonNumAndFlow(
				lessonDate.toLocalDate(), lessonNum, educationLevel, course, group, subgroup
		);
		HomeworkResponse response = convertHomeworkToResponse(found);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<HomeworkResponse>> getAllHomeworks() {
		log.trace("Received request to get all homeworks");
		
		Collection<Homework> found = homeworkService.findAll();
		List<HomeworkResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertHomeworkToResponse(e)));
		
		log.trace(String.format("Found all (%s) homeworks: %s", found.size(), found));
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/flow")
	public ResponseEntity<List<HomeworkResponse>> getAllHomeworksByFlow(
			@RequestParam(name = "education_level") int educationLevel, @RequestParam int course,
			@RequestParam int group, @RequestParam int subgroup
	) {
		log.trace(String.format(
				"Received request to get all homeworks by flow %s.%s.%s.%s", educationLevel, course, group, subgroup
		));
		
		Collection<Homework> found = homeworkService.findAllByFlow(educationLevel, course, group, subgroup);
		List<HomeworkResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertHomeworkToResponse(e)));
		
		log.trace(String.format("Found all (%s) homeworks by flow %s: %s", found.size(), group, found));
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/homework")
	public ResponseEntity<?> addHomework(@RequestBody HomeworkRequest homework) {
		log.trace(String.format("Received request to add homework %s", homework));
		
		Homework addedHomework = homeworkService.addOrUpdate(
				homework.homework(), homework.lesson_date(), homework.lesson_num(),
				homework.flow().education_level(), homework.flow().course(), homework.flow().group(),
				homework.flow().subgroup(), homework.subject().subject()
		);
		
		log.trace(String.format("Successfully added homework %s", addedHomework));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/homework")
	public ResponseEntity<?> deleteHomework(@RequestBody DeleteHomeworkRequest homework) {
		log.trace(String.format("Received request to delete homework %s", homework));
		
		Homework deletedHomework = homeworkService.deleteByFlowAndLessonDateAndLessonNum(
				homework.flow().education_level(), homework.flow().course(), homework.flow().group(),
				homework.flow().subgroup(), homework.lesson_date(), homework.lesson_num()
		);
		
		log.trace(String.format("Successfully deleted homework %s", deletedHomework));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/homeworks")
	@Transactional
	public ResponseEntity<?> deleteHomeworks(@RequestBody List<DeleteHomeworkRequest> homeworks) {
		log.trace(String.format(
				"Received request to delete %s homeworks: %s", homeworks.size(), homeworks
		));
		
		List<Homework> deletedHomeworks = new ArrayList<>();
		for (DeleteHomeworkRequest homework : homeworks) {
			Homework deletedHomework = homeworkService.deleteByFlowAndLessonDateAndLessonNum(
					homework.flow().education_level(), homework.flow().course(), homework.flow().group(),
					homework.flow().subgroup(), homework.lesson_date(), homework.lesson_num()
			);
			deletedHomeworks.add(deletedHomework);
		}
		
		log.trace(String.format("Successfully deleted %s homeworks: %s", deletedHomeworks.size(), deletedHomeworks));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllHomeworks() {
		log.trace(String.format("Received request to delete all homeworks"));
		
		Collection<Homework> deletedHomeworks = homeworkService.deleteAll();

		log.trace(String.format(
				"Successfully deleted all (%s) homeworks: %s", deletedHomeworks.size(), deletedHomeworks
		));
		
		return ResponseEntity.ok().build();
	}
	
	private HomeworkResponse convertHomeworkToResponse(Homework homework) {
		FlowResponse flowResponse = new FlowResponse(
				homework.getFlow().getEducationLevel(), homework.getFlow().getCourse(), homework.getFlow().getGroup(),
				homework.getFlow().getSubgroup(), homework.getFlow().getLastEdit(),
				homework.getFlow().getLessonsStartDate(), homework.getFlow().getSessionStartDate(),
				homework.getFlow().getSessionEndDate(), homework.getFlow().getActive()
		);
		
		SubjectResponse subjectResponse = new SubjectResponse(homework.getSubject().getSubject());
		
		return new HomeworkResponse(
				homework.getHomework(), homework.getLessonDate(), homework.getLessonNum(),
				flowResponse, subjectResponse
		);
	}
}
