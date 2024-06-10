package ru.khanin.dmitrii.schedule.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonRequest;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonResponse;
import ru.khanin.dmitrii.schedule.entity.Lesson;
import ru.khanin.dmitrii.schedule.service.LessonService;

@RestController
@RequestMapping("lesson")
@RequiredArgsConstructor
public class LessonController {
	private final LessonService lessonService;
	
	@GetMapping("/all")
	public ResponseEntity<List<LessonResponse>> getAllLessons() {
		Collection<Lesson> found = lessonService.findAll();
		List<LessonResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			result.add(new LessonResponse(e.getName(), e.getTeacher(), e.getCabinet()));
		});
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/lesson")
	public ResponseEntity<?> addLesson(@RequestBody LessonRequest lesson) {
		lessonService.add(lesson.name(), lesson.teacher(), lesson.cabinet());
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllLessons() {
		lessonService.deleteAll();
		return ResponseEntity.ok().build();
	}
}
