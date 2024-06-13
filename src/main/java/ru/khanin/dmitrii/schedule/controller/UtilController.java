package ru.khanin.dmitrii.schedule.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.exception.NoAccessException;
import ru.khanin.dmitrii.schedule.service.BrsScheduler;
import ru.khanin.dmitrii.schedule.service.UserService;

@RestController
@RequestMapping("/util")
@RequiredArgsConstructor
public class UtilController {
	private final BrsScheduler brsScheduler;
	private final UserService userService;
	
	@GetMapping("/groups")
	public ResponseEntity<?> checkStudGroups(@RequestHeader("api_key") String apiKey) {
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для обновления групп");
		
		brsScheduler.checkStudGroups();
		return ResponseEntity.ok().build();
	}
}
