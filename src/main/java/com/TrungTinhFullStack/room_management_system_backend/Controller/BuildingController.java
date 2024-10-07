package com.TrungTinhFullStack.room_management_system_backend.Controller;

import com.TrungTinhFullStack.room_management_system_backend.Entity.Building;
import com.TrungTinhFullStack.room_management_system_backend.Service.Building.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/buildings")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @GetMapping("/all")
    public ResponseEntity<List<Building>> getAllBuilding() {
        List<Building> buildings = buildingService.getAllBuilding();
        return new ResponseEntity<>(buildings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Building> getBuildingById(@PathVariable Long id) {
        Building building1 = buildingService.getBuildingById(id);
        return new ResponseEntity<>(building1, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Building>> searchBuildingByName(@RequestParam String name) {
        List<Building> building1 = buildingService.searchBuildingByName(name);
        return new ResponseEntity<>(building1, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Building> addBuilding(@RequestParam("name") String name,
                                                @RequestParam("address") String address,
                                                @RequestParam("landlord_id") Long landlord_id,
                                                @RequestPart MultipartFile img) throws IOException {
        Building building1 = buildingService.addBuilding(name,address,img,landlord_id);
        return new ResponseEntity<>(building1, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Building> updateBuilding(@PathVariable Long id,
                                                   @RequestParam(value = "name",required = false) String name,
                                                   @RequestParam(value = "address",required = false) String address,
                                                   @RequestParam(value = "landlord_id",required = false) Long landlord_id,
                                                   @RequestPart(required = false) MultipartFile img) throws IOException {
        Building building1 = buildingService.updateBuilding(id,name,address,img,landlord_id);
        return new ResponseEntity<>(building1, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Building> deleteBuilding(@PathVariable Long id) {
        Building building1 = buildingService.deleteBuilding(id);
        return new ResponseEntity<>(building1, HttpStatus.OK);
    }
}
