package ru.khanin.dmitrii.schedule.service;

import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.Cabinet;

public interface CabinetService {
	Cabinet addOrUpdate(String cabinet, String building, String address);
	Cabinet findById(long id);
	Cabinet findByCabinetAndBuilding(String cabinet, String building);
	Collection<Cabinet> findAll();
	Collection<Cabinet> findAllByCabinet(String cabinet);
	Collection<Cabinet> findAllByBuilding(String building);
	Collection<Cabinet> findAllWhereCabinetStartsWith(String cabinet);
	Cabinet deleteById(long id);
	Cabinet deleteByCabinetAndBuilding(String cabinet, String building);
	Collection<Cabinet> deleteAll();
}
