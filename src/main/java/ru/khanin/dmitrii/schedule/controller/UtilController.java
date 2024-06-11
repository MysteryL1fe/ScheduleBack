package ru.khanin.dmitrii.schedule.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.service.BrsScheduler;

@RestController
@RequestMapping("/util")
@RequiredArgsConstructor
public class UtilController {
	private final BrsScheduler brsScheduler;
	
	@GetMapping("/groups")
	public ResponseEntity<?> checkStudGroups() {
		brsScheduler.checkStudGroups();
		return ResponseEntity.ok().build();
	}
}
