package ru.khanin.dmitrii.schedule.service.jdbc;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.User;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcUserFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcUserRepo;
import ru.khanin.dmitrii.schedule.service.UserService;

@RequiredArgsConstructor
public class JdbcUserService implements UserService {
	private final JdbcUserRepo userRepo;
	private final JdbcUserFlowRepo userFlowRepo;

	@Override
	public User addOrUpdate(String login, String password, boolean admin) {
		User user = new User();
		user.setLogin(login);
		user.setPassword(password);
		user.setAdmin(admin);
		
		if (userRepo.findByLogin(login).isPresent()) {
			return userRepo.update(user);
		}
		
		return userRepo.add(user);
	}
	
	@Override
	public User findById(long id) {
		return userRepo.findById(id).orElseThrow();
	}

	@Override
	public User findByLogin(String login) {
		return userRepo.findByLogin(login).orElseThrow();
	}

	@Override
	public Collection<User> findAll() {
		Iterable<? extends User> found = userRepo.findAll();
		Collection<User> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public User deleteByLogin(String login) {
		User foundUser = userRepo.findByLogin(login).orElseThrow();
		return deleteById(foundUser.getId());
	}

	@Override
	@Transactional
	public User deleteById(long id) {
		userFlowRepo.deleteAllByUser(id);
		return userRepo.deleteById(id).orElseThrow();
	}

	@Override
	public boolean isAdmin(String login) {
		return userRepo.findByLogin(login).orElseThrow().getAdmin();
	}
	
}
