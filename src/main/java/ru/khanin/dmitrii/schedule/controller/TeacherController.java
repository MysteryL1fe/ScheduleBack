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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.khanin.dmitrii.schedule.dto.teacher.TeacherRequest;
import ru.khanin.dmitrii.schedule.dto.teacher.TeacherResponse;
import ru.khanin.dmitrii.schedule.entity.Teacher;
import ru.khanin.dmitrii.schedule.service.TeacherService;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
@Slf4j
public class TeacherController {
	private TeacherService teacherService;
	
	@GetMapping("/all")
	public ResponseEntity<List<TeacherResponse>> getAllTeachers() {
		log.trace("Received request to get all teachers");
		
		Collection<Teacher> found = teacherService.findAll();
		List<TeacherResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertTeacherToResponse(e)));
		
		log.trace(String.format("Found all (%s) teachers: %s", found.size(), found));
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/surname")
	public ResponseEntity<List<TeacherResponse>> getAllTeachersBySurname(@RequestParam String surname) {
		log.trace(String.format("Received request to get all teachers by surname %s", surname));
		
		Collection<Teacher> found = teacherService.findAll();
		List<TeacherResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertTeacherToResponse(e)));
		
		log.trace(String.format("Found all (%s) teachers by surname %s: %s", found.size(), surname, found));
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/teacher")
	public ResponseEntity<?> addTeacher(@RequestBody TeacherRequest teacher) {
		log.trace(String.format("Received request to add teacher %s", teacher));
		
		Teacher added = teacherService.add(teacher.surname(), teacher.name(), teacher.patronymic());
		
		log.trace(String.format("Successfully added teacher %s", added));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/teacher")
	public ResponseEntity<?> deleteTeacher(@RequestBody TeacherRequest teacher) {
		log.trace(String.format("Received request to delete teacher %s", teacher));
		
		Teacher deleted = teacherService.deleteBySurnameAndNameAndPatronymic(
				teacher.surname(), teacher.name(), teacher.patronymic()
		);
		
		log.trace(String.format("Successfully deleted teacher %s", deleted));
		
		return ResponseEntity.ok().build();
	}
	
	private TeacherResponse convertTeacherToResponse(Teacher teacher) {
		return new TeacherResponse(teacher.getSurname(), teacher.getName(), teacher.getPatronymic());
	}
}
