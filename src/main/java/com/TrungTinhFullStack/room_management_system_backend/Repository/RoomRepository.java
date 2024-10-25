package com.TrungTinhFullStack.room_management_system_backend.Repository;

import com.TrungTinhFullStack.room_management_system_backend.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
    List<Room> findByRoomNameContainingIgnoreCaseAndIsDeletedFalse(String name);
    List<Room> findAllByIsDeletedFalse();
    List<Room> findAllByBuildingId(Long id);
    @Query("SELECT r FROM Room r JOIN r.tenants u WHERE u.id = :id")
    List<Room> findAllByTenant(Long id);
}
