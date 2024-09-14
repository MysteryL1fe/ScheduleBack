package ru.khanin.dmitrii.schedule.repo;

import java.util.Optional;

import ru.khanin.dmitrii.schedule.entity.Cabinet;

public interface CabinetRepo {
	Cabinet add(Cabinet cabinet);
	Cabinet update(Cabinet cabinet);
	Optional<Cabinet> findById(long id);
	Optional<Cabinet> findByCabinetAndBuilding(String cabinet, String building);
	Iterable<Cabinet> findAll();
	Iterable<Cabinet> findAllByCabinet(String cabinet);
	Iterable<Cabinet> findAllByBuilding(String building);
	Iterable<Cabinet> findAllWhereCabinetStartsWith(String cabinet);
	Optional<Cabinet> deleteById(long id);
	Iterable<Cabinet> deleteAll();
}
