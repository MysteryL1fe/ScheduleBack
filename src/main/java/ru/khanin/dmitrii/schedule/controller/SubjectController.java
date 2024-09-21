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
import lombok.extern.slf4j.Slf4j;
import ru.khanin.dmitrii.schedule.dto.subject.SubjectRequest;
import ru.khanin.dmitrii.schedule.dto.subject.SubjectResponse;
import ru.khanin.dmitrii.schedule.entity.Subject;
import ru.khanin.dmitrii.schedule.service.SubjectService;

@RestController
@RequestMapping("/subject")
@RequiredArgsConstructor
@Slf4j
public class SubjectController {
	private final SubjectService subjectService;
	
	@GetMapping("/all")
	public ResponseEntity<List<SubjectResponse>> getAllSubjects() {
		log.trace("Received request to find all subjects");
		
		Collection<Subject> found = subjectService.findAll();
		List<SubjectResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertSubjectToResponse(e)));
		
		log.trace(String.format("Found all (%s) subjects: %s", found.size(), found));
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/subject")
	public ResponseEntity<?> addSubject(@RequestBody SubjectRequest subject) {
		log.trace(String.format("Received request to add subject %s", subject));
		
		Subject addedSubject = subjectService.add(subject.subject());
		
		log.trace(String.format("Successfully added subject %s", addedSubject));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/subject")
	public ResponseEntity<?> deleteSubject(@RequestBody SubjectRequest subject) {
		log.trace(String.format("Received request to delete subject %s", subject));
		
		Subject deleted = subjectService.deleteBySubject(subject.subject());
		
		log.trace(String.format("Successfully deleted subject %s", deleted));
		
		return ResponseEntity.ok().build();
	}
	
	private SubjectResponse convertSubjectToResponse(Subject subject) {
		return new SubjectResponse(subject.getSubject());
	}
}
