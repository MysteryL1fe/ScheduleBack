package ru.khanin.dmitrii.schedule.service.jdbc;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Cabinet;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcCabinetRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcScheduleRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcTempScheduleRepo;
import ru.khanin.dmitrii.schedule.service.CabinetService;

@RequiredArgsConstructor
public class JdbcCabinetService implements CabinetService {
	private final JdbcCabinetRepo cabinetRepo;
	private final JdbcScheduleRepo scheduleRepo;
	private final JdbcTempScheduleRepo tempScheduleRepo;

	@Override
	public Cabinet addOrUpdate(String cabinet, String building, String address) {
		Cabinet cabinetToAdd = new Cabinet();
		cabinetToAdd.setCabinet(cabinet);
		cabinetToAdd.setBuilding(building);
		cabinetToAdd.setAddress(address);
		
		if (cabinetRepo.findByCabinetAndBuilding(cabinet, building).isPresent()) {
			return cabinetRepo.update(cabinetToAdd);
		}
		
		return cabinetRepo.add(cabinetToAdd);
	}

	@Override
	public Cabinet findById(long id) {
		return cabinetRepo.findById(id).orElseThrow();
	}

	@Override
	public Cabinet findByCabinetAndBuilding(String cabinet, String building) {
		return cabinetRepo.findByCabinetAndBuilding(cabinet, building).orElseThrow();
	}

	@Override
	public Collection<Cabinet> findAll() {
		Iterable<Cabinet> found = cabinetRepo.findAll();
		Collection<Cabinet> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Cabinet> findAllByCabinet(String cabinet) {
		Iterable<Cabinet> found = cabinetRepo.findAllByCabinet(cabinet);
		Collection<Cabinet> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Cabinet> findAllByBuilding(String building) {
		Iterable<Cabinet> found = cabinetRepo.findAllByBuilding(building);
		Collection<Cabinet> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Cabinet> findAllWhereCabinetStartsWith(String cabinet) {
		Iterable<Cabinet> found = cabinetRepo.findAllWhereCabinetStartsWith(cabinet);
		Collection<Cabinet> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	@Transactional
	public Cabinet deleteById(long id) {
		scheduleRepo.deleteAllByCabinet(id);
		tempScheduleRepo.deleteAllByCabinet(id);
		return cabinetRepo.deleteById(id).orElseThrow();
	}

	@Override
	public Cabinet deleteByCabinetAndBuilding(String cabinet, String building) {
		Cabinet found = cabinetRepo.findByCabinetAndBuilding(cabinet, building).orElseThrow();
		return deleteById(found.getId());
	}

	@Override
	@Transactional
	public Collection<Cabinet> deleteAll() {
		scheduleRepo.deleteAllWhereCabinetIsNotNull();
		tempScheduleRepo.deleteAllWhereCabinetIsNotNull();
		Iterable<Cabinet> deleted = cabinetRepo.deleteAll();
		Collection<Cabinet> result = new ArrayList<>();
		deleted.forEach(result::add);
		return result;
	}

}
