package ru.khanin.dmitrii.schedule.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonRequest;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonResponse;
import ru.khanin.dmitrii.schedule.entity.Lesson;
import ru.khanin.dmitrii.schedule.service.LessonService;
import ru.khanin.dmitrii.schedule.service.UserService;

@RestController
@RequestMapping("lesson")
@RequiredArgsConstructor
public class LessonController {
	private final LessonService lessonService;
	private final UserService userService;
	
	@GetMapping("/all")
	public ResponseEntity<List<LessonResponse>> getAllLessons(@RequestHeader("api_key") String apiKey) {
		if (!userService.checkAdminAccessByApiKey(apiKey))
			return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
		
		Collection<Lesson> found = lessonService.findAll();
		List<LessonResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			result.add(new LessonResponse(e.getName(), e.getTeacher(), e.getCabinet()));
		});
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/lesson")
	public ResponseEntity<?> addLesson(@RequestHeader("api_key") String apiKey, @RequestBody LessonRequest lesson) {
		if (!userService.checkAdminAccessByApiKey(apiKey))
			return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
		
		lessonService.add(lesson.name(), lesson.teacher(), lesson.cabinet());
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllLessons(@RequestHeader("api_key") String apiKey) {
		if (!userService.checkAdminAccessByApiKey(apiKey))
			return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
		
		lessonService.deleteAll();
		return ResponseEntity.ok().build();
	}
}
