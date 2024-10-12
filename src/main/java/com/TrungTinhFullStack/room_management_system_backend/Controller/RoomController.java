package com.TrungTinhFullStack.room_management_system_backend.Controller;

import com.TrungTinhFullStack.room_management_system_backend.Entity.Room;
import com.TrungTinhFullStack.room_management_system_backend.Service.Room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/all")
    public ResponseEntity<List<Room>> getAllRoom() {
        List<Room> rooms = roomService.getAllRoom();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity <Room> getRoomById(@PathVariable Long id) {
        Room rooms = roomService.getRoomById(id);
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity <List<Room>> searchRoomByName(@RequestParam String name) {
        List<Room> rooms = roomService.searchRoomByName(name);
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/buildingId/{id}")
    public ResponseEntity <List<Room>> getAllRoomByBuildingId(@PathVariable Long id) {
        List<Room> rooms = roomService.getAllRoomByBuildingId(id);
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity <Room> addRoom(@RequestParam String roomName,
                                         @RequestParam Double rentPrice,
                                         @RequestParam Long buildingId,
                                         @RequestPart MultipartFile img,
                                         @RequestParam List<Long> tenants) throws IOException {
        Room rooms = roomService.addRoom(roomName,rentPrice,buildingId,img,tenants);
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity <Room> updateRoom(@PathVariable Long id,
                                            @RequestParam(required = false) String roomName,
                                         @RequestParam(required = false) Double rentPrice,
                                         @RequestParam(required = false) Long buildingId,
                                         @RequestPart(required = false) MultipartFile img,
                                         @RequestParam(required = false) List<Long> tenants) throws IOException {
        Room rooms = roomService.updateRoom(id,roomName,rentPrice,buildingId,img,tenants);
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity <Room> deleteRoom(@PathVariable Long id) {
        Room rooms = roomService.deleteRoom(id);
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

}
