package com.TrungTinhFullStack.room_management_system_backend.Service.Building;

import com.TrungTinhFullStack.room_management_system_backend.Entity.Building;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BuildingService {

    Building addBuilding(String name, String address, MultipartFile img,Long landlord_id) throws IOException;
    List<Building> getAllBuilding();
    Building getBuildingById(Long id);
    Building updateBuilding(Long id,String name,String address,MultipartFile img,Long landlord_id) throws IOException;
    Building deleteBuilding(Long id);
    List<Building> searchBuildingByName(String name);
    List<Building> getAllBuildingByLandlord(Long id);
}
