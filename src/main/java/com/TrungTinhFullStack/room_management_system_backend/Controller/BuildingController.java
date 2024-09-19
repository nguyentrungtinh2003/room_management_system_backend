package com.TrungTinhFullStack.room_management_system_backend.Controller;

import com.TrungTinhFullStack.room_management_system_backend.Entity.Building;
import com.TrungTinhFullStack.room_management_system_backend.Service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @GetMapping()
    public ResponseEntity<List<Building>> getAllBuilding() {
        List<Building> buildings = buildingService.getAllBuilding();
        return new ResponseEntity<>(buildings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Building> getBuildingById(@PathVariable Long id) {
        Building building1 = buildingService.getBuildingById(id);
        return new ResponseEntity<>(building1, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Building> addBuilding(@RequestBody Building building) {
        Building building1 = buildingService.addBuilding(building);
        return new ResponseEntity<>(building1, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Building> updateBuilding(@PathVariable Long id,@RequestBody Building building) {
        Building building1 = buildingService.updateBuilding(id,building);
        return new ResponseEntity<>(building1, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Building> deleteBuilding(@PathVariable Long id) {
        Building building1 = buildingService.deleteBuilding(id);
        return new ResponseEntity<>(building1, HttpStatus.OK);
    }
}
