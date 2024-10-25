package com.TrungTinhFullStack.room_management_system_backend.Service.Building;

import com.TrungTinhFullStack.room_management_system_backend.Entity.Building;
import com.TrungTinhFullStack.room_management_system_backend.Entity.User;
import com.TrungTinhFullStack.room_management_system_backend.Repository.BuildingRepository;
import com.TrungTinhFullStack.room_management_system_backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public Building addBuilding(String name, String address, MultipartFile img, Long landlord_id) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = null;
        fileName = img.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
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
        List<Building> buildings = buildingRepository.findAllByIsDeletedFalse();
        return buildings;
    }

    @Override
    public Building getBuildingById(Long id) {
        Building building1 = buildingRepository.findById(id).orElse(null);
        return building1;
    }

    @Override
    public Building updateBuilding(Long id, String name, String address,MultipartFile img,Long landlord_id) throws IOException {
        Building building1 = buildingRepository.findById(id).orElse(null);
        User user = userRepository.findById(landlord_id).orElse(null);

        if(img != null && !img.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(img.getOriginalFilename()));
            assert building1 != null;
            building1.setImg(fileName);
            Path path = Paths.get("uploads/" + fileName);
            Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }
        if(name != null && !name.isEmpty()) {
            assert building1 != null;
            building1.setName(name);
        }
        if(address != null && !address.isEmpty()) {
            assert building1 != null;
            building1.setAddress(address);
        }
        assert building1 != null;
        building1.setLandlord(user);

        buildingRepository.save(building1);
        return building1;
    }

    @Override
    public Building deleteBuilding(Long id) {
        Building building = buildingRepository.findById(id).orElse(null);
        building.setDeleted(true);
        buildingRepository.save(building);
        return building;
    }

    @Override
    public List<Building> searchBuildingByName(String name) {
        return buildingRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(name);
    }

    @Override
    public List<Building> getAllBuildingByLandlord(Long id) {
        return buildingRepository.findAllByLandlordId(id);
    }

    @Override
    public Page<Building> getBuildingsByPage(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page,size,sort);
        return buildingRepository.findAll(pageable);
    }
}
