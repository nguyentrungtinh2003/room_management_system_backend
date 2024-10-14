package com.TrungTinhFullStack.room_management_system_backend.Service.Room;

import com.TrungTinhFullStack.room_management_system_backend.Entity.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RoomService {
    List<Room> getAllRoom();
    Room getRoomById(Long id);
    Room addRoom(String roomName, Double rentPrice, Long buildingId,
                 MultipartFile img,List<Long> tenantIds) throws IOException;
    Room updateRoom(Long id,String roomName, Double rentPrice, Long buildingId,
                 MultipartFile img,List<Long> tenantIds) throws IOException;
    Room deleteRoom(Long id);
    List<Room> searchRoomByName(String name);
    List<Room> getAllRoomByBuildingId(Long id);
    List<Room> findRoomByTenantId(Long id);

}
