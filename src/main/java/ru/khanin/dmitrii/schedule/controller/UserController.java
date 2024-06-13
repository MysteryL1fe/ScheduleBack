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
import ru.khanin.dmitrii.schedule.dto.user.UserRequest;
import ru.khanin.dmitrii.schedule.dto.user.UserResponse;
import ru.khanin.dmitrii.schedule.entity.User;
import ru.khanin.dmitrii.schedule.exception.NoAccessException;
import ru.khanin.dmitrii.schedule.service.UserService;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping("/all")
	public ResponseEntity<List<UserResponse>> getAllUsers(@RequestHeader("api_key") String apiKey) {
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для просмотра пользователей");
		
		Collection<User> found = userService.findAll();
		List<UserResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			result.add(new UserResponse(e.getId(), e.getApiKey(), e.getName(), e.getAccess(), e.getFlow()));
		});
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("api_key")
	public ResponseEntity<List<UserResponse>> getAllUsersByApiKey(
			@RequestHeader("api_key") String apiKey, @RequestBody String api_key
	) {
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для просмотра пользователей");
		
		Collection<User> found = userService.findAllByApiKey(api_key);
		List<UserResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			result.add(new UserResponse(e.getId(), e.getApiKey(), e.getName(), e.getAccess(), e.getFlow()));
		});
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/user")
	public ResponseEntity<?> addUser(@RequestHeader("api_key") String apiKey, @RequestBody UserRequest user) {
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для добавления пользователя");
		
		userService.add(
				user.api_key(), user.name(), user.access(),
				user.flow().flow_lvl(), user.flow().course(), user.flow().flow(), user.flow().subgroup()
		);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllUsers(@RequestHeader("api_key") String apiKey) {
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для удаления пользователей");
		
		userService.deleteAll();
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/api_key")
	public ResponseEntity<?> deleteAllUsersByApiKey(
			@RequestHeader("api_key") String apiKey, @RequestBody String api_key
	) {
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для удаления пользователей");
		
		userService.deleteAllByApiKey(api_key);
		return ResponseEntity.ok().build();
	}
}
