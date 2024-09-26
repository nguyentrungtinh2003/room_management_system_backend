package com.TrungTinhFullStack.room_management_system_backend.Controller;

import com.TrungTinhFullStack.room_management_system_backend.Dto.ReqRes;
import com.TrungTinhFullStack.room_management_system_backend.Entity.User;
import com.TrungTinhFullStack.room_management_system_backend.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ReqRes request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestPart String username,
                                      @RequestPart String password,
                                      @RequestPart String email,
                                      @RequestPart MultipartFile img,
                                      @RequestPart String phoneNumber,
                                      @RequestPart String citizenIdentification,
                                      @RequestPart String address,
                                      @RequestPart String role) throws IOException {
        return ResponseEntity.ok(userService.register(username,password,email,img,phoneNumber,citizenIdentification,address,role));
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestPart String username,
                                        @RequestPart String password,
                                        @RequestPart String email,
                                        @RequestPart MultipartFile img,
                                        @RequestPart String phoneNumber,
                                        @RequestPart String citizenIdentification,
                                        @RequestPart String address,
                                        @RequestPart String role) {
        return ResponseEntity.ok(userService.updateUser(id,username,password,email,img,phoneNumber,citizenIdentification,address,role));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}
