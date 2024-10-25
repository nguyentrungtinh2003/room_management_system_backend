package com.TrungTinhFullStack.room_management_system_backend.Controller;

import com.TrungTinhFullStack.room_management_system_backend.Dto.ReqRes;
import com.TrungTinhFullStack.room_management_system_backend.Entity.User;
import com.TrungTinhFullStack.room_management_system_backend.Repository.UserRepository;
import com.TrungTinhFullStack.room_management_system_backend.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ReqRes request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("/success")
    public ResponseEntity<?> success(@AuthenticationPrincipal OAuth2User principal) {

        if (principal == null) {
            return ResponseEntity.ok("redirect:/login"); // Nếu chưa đăng nhập, chuyển hướng về trang login
        }

        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");

        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setUsername(name);
            userRepository.save(user);
        }

        return ResponseEntity.ok(user);
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

    @GetMapping("/search")
    public ResponseEntity<List<User>> getSearchUserByUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.searchUserByUsername(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<User>> getUserByPage(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "2") int size,
                                                    @RequestParam(defaultValue = "username") String sortBy,
                                                    @RequestParam(defaultValue = "desc") String sortDir) {
        return ResponseEntity.ok(userService.getUsersByPage(page,size,sortBy,sortDir));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestPart String username,
                                        @RequestPart(required = false) String password,
                                        @RequestPart String email,
                                        @RequestPart(required = false) MultipartFile img,
                                        @RequestPart String phoneNumber,
                                        @RequestPart String citizenIdentification,
                                        @RequestPart String address,
                                        @RequestPart(required = false) String role) throws IOException {
        return ResponseEntity.ok(userService.updateUser(id,username,password,email,img,phoneNumber,citizenIdentification,address,role));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PutMapping("/unlock/{id}")
    public ResponseEntity<User> unlockUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.unlockUser(id));
    }

    // API gửi OTP đến email
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        return ResponseEntity.ok(userService.sendOtpToEmail(email));
    }

    // API xác nhận OTP và thay đổi mật khẩu
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) throws IllegalAccessException {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("newPassword");

        return ResponseEntity.ok(userService.verifyOtpAndChangePassword(email, otp, newPassword));
    }

    @PostMapping("/sendMail")
    public ResponseEntity<String> sendMail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String subject = request.get("subject");
        String body = request.get("body");
        return ResponseEntity.ok(userService.sendMail(email,subject,body));
    }
}
