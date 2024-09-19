package com.TrungTinhFullStack.room_management_system_backend.Service;

import com.TrungTinhFullStack.room_management_system_backend.Entity.Building;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BuildingService {

    Building addBuilding(Building building);
    List<Building> getAllBuilding();
    Building getBuildingById(Long id);
    Building updateBuilding(Long id,Building building);
    Building deleteBuilding(Long id);
}
