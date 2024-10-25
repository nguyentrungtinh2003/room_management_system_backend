package com.TrungTinhFullStack.room_management_system_backend.Service.Room;

import com.TrungTinhFullStack.room_management_system_backend.Entity.Building;
import com.TrungTinhFullStack.room_management_system_backend.Entity.Room;
import com.TrungTinhFullStack.room_management_system_backend.Entity.User;
import com.TrungTinhFullStack.room_management_system_backend.Enum.Role;
import com.TrungTinhFullStack.room_management_system_backend.Enum.RoomStatus;
import com.TrungTinhFullStack.room_management_system_backend.Repository.RoomRepository;
import com.TrungTinhFullStack.room_management_system_backend.Repository.UserRepository;
import com.TrungTinhFullStack.room_management_system_backend.Service.Building.BuildingService;
import com.TrungTinhFullStack.room_management_system_backend.Service.User.UserService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService{

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public List<Room> getAllRoom() {
        return roomRepository.findAllByIsDeletedFalse();
    }

    @Override
    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    @Override
    public Room addRoom(String roomName, Double rentPrice, Long buildingId, MultipartFile img, List<Long> tenantIds) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = null;
        fileName = UUID.randomUUID() +"_" +img.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath,img.getBytes());

        Building building = buildingService.getBuildingById(buildingId);
        Room room = new Room();
        room.setRoomName(roomName);
        room.setBuilding(building);
        room.setRentPrice(rentPrice);
        room.setImg(fileName);

        if(tenantIds != null && !tenantIds.isEmpty()) {
            List<User> tenants = userRepository.findAllById(tenantIds)
                    .stream()
                    .filter(user -> user.getRole() == Role.TENANT)
                    .collect(Collectors.toList());
            room.setTenants(tenants);
            room.setStatus(RoomStatus.OCCUPIED);
        }else {
            room.setTenants(new ArrayList<>());
            room.setStatus(RoomStatus.VACANT);
        }
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Long id,String roomName, Double rentPrice, Long buildingId,
                           MultipartFile img, List<Long> tenantIds) throws IOException {
        Room room = getRoomById(id);
        if(img != null && !img.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(img.getOriginalFilename()));
            assert room != null;
            room.setImg(fileName);
            Path path = Paths.get("uploads/" + fileName);
            Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }
        if(roomName != null && !roomName.isEmpty()) {
            room.setRoomName(roomName);
        }
        if(rentPrice != null && !rentPrice.isInfinite()) {
            room.setRentPrice(rentPrice);
        }
        if(buildingId != null) {
            Building building = buildingService.getBuildingById(buildingId);
            room.setBuilding(building);
        }
        if(tenantIds != null && !tenantIds.isEmpty()) {
            List<User> tenants = userRepository.findAllById(tenantIds)
                    .stream()
                    .filter(user -> user.getRole() == Role.TENANT)
                    .collect(Collectors.toList());
            room.setTenants(tenants);
            room.setStatus(RoomStatus.OCCUPIED);
        }
        roomRepository.save(room);

        return room;
    }

    @Override
    public Room deleteRoom(Long id) {
        Room room = getRoomById(id);
        room.setDeleted(true);
        roomRepository.save(room);
        return room;
    }

    @Override
    public List<Room> searchRoomByName(String name) {
        return roomRepository.findByRoomNameContainingIgnoreCaseAndIsDeletedFalse(name);
    }

    @Override
    public List<Room> getAllRoomByBuildingId(Long id) {
        return roomRepository.findAllByBuildingId(id);
    }

    @Override
    public List<Room> findRoomByTenantId(Long id) {
        return roomRepository.findAllByTenant(id);
    }

    @Override
    public Page<Room> getRoomByPage(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page,size,sort);
        return roomRepository.findAll(pageable);
    }

}
