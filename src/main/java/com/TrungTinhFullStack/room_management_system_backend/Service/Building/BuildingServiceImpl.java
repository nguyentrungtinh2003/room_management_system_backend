package com.TrungTinhFullStack.room_management_system_backend.Service.Building;

import com.TrungTinhFullStack.room_management_system_backend.Entity.Building;
import com.TrungTinhFullStack.room_management_system_backend.Entity.User;
import com.TrungTinhFullStack.room_management_system_backend.Repository.BuildingRepository;
import com.TrungTinhFullStack.room_management_system_backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String UPLOAD_DIR = "/uploads";

    Path uploadsPath = Paths.get(UPLOAD_DIR);

    @Override
    public Building addBuilding(String name, String address, MultipartFile img, Long landlord_id) throws IOException {
        String fileName = img.getOriginalFilename();
        Path filePath = uploadsPath.resolve(fileName);
        Files.write(filePath,img.getBytes());

        User user = userRepository.findById(landlord_id).orElse(null);

        Building building = new Building();
        building.setLandlord(user);
        building.setImg(fileName);
        building.setAddress(address);
        building.setName(name);

        buildingRepository.save(building);
        return building;
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
