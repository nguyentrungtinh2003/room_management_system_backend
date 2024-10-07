package com.TrungTinhFullStack.room_management_system_backend.Repository;

import com.TrungTinhFullStack.room_management_system_backend.Entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Building,Long> {
    List<Building> findByNameContainingIgnoreCase(String name);
}
