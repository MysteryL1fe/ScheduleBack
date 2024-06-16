package ru.khanin.dmitrii.schedule.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonRequest;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonResponse;
import ru.khanin.dmitrii.schedule.entity.Lesson;
import ru.khanin.dmitrii.schedule.exception.NoAccessException;
import ru.khanin.dmitrii.schedule.service.LessonService;
import ru.khanin.dmitrii.schedule.service.UserService;

@RestController
@RequestMapping("lesson")
@RequiredArgsConstructor
@Slf4j
public class LessonController {
	private final LessonService lessonService;
	private final UserService userService;
	
	@GetMapping("/all")
	public ResponseEntity<List<LessonResponse>> getAllLessons() {
		log.info("Received request to get all lessons");
		
		Collection<Lesson> found = lessonService.findAll();
		List<LessonResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			result.add(new LessonResponse(e.getName(), e.getTeacher(), e.getCabinet()));
		});
		
		log.info(String.format("Found all (%s) lessons: %s", found.size(), found));
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/lesson")
	public ResponseEntity<?> addLesson(@RequestHeader("api_key") String apiKey, @RequestBody LessonRequest lesson) {
		log.info(String.format("Received request from \"%s\" to add lesson %s", apiKey, lesson));
		
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для добавления занятия");

		log.info(String.format("User \"%s\" is trying to add lesson %s", apiKey, lesson));
		
		Lesson addedLesson = lessonService.add(lesson.name(), lesson.teacher(), lesson.cabinet());
		
		log.info(String.format("User \"%s\" has successfully added lesson %s", apiKey, addedLesson));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllLessons(@RequestHeader("api_key") String apiKey) {
		log.info(String.format("Received request from \"%s\" to delete all lessons", apiKey));
		
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для удаления занятий");
		
		log.info(String.format("User \"%s\" is trying to delete all lessons", apiKey));
		
		Collection<Lesson> deletedLessons = lessonService.deleteAll();

		log.info(String.format(
				"User \"%s\" has successfully deleted all (%s) lessons: %s",
				apiKey, deletedLessons.size(), deletedLessons
		));
		
		return ResponseEntity.ok().build();
	}
}
