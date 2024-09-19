package com.TrungTinhFullStack.room_management_system_backend.Service.Impl;

import com.TrungTinhFullStack.room_management_system_backend.Entity.Building;
import com.TrungTinhFullStack.room_management_system_backend.Entity.User;
import com.TrungTinhFullStack.room_management_system_backend.Repository.BuildingRepository;
import com.TrungTinhFullStack.room_management_system_backend.Repository.UserRepository;
import com.TrungTinhFullStack.room_management_system_backend.Service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Building addBuilding(Building building) {
        Building building1 = buildingRepository.save(building);
        return building1;
    }

    @Override
    public List<Building> getAllBuilding() {
        List<Building> buildings = buildingRepository.findAll();
        return buildings;
    }

    @Override
    public Building getBuildingById(Long id) {
        Building building1 = buildingRepository.findById(id).orElse(null);
        return building1;
    }

    @Override
    public Building updateBuilding(Long id, Building building) {
        Building building1 = buildingRepository.findById(id).orElse(null);
        User user = userRepository.findById(building.getLandlord().getId()).orElse(null);

        building1.setName(building.getName());
        building1.setAddress(building.getAddress());
        building1.setLandlord(user);

        buildingRepository.save(building1);
        return building1;
    }

    @Override
    public Building deleteBuilding(Long id) {
        Building building = buildingRepository.findById(id).orElse(null);
        buildingRepository.deleteById(id);
        return building;
    }
}
