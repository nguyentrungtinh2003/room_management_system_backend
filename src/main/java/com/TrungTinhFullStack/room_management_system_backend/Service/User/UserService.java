package com.TrungTinhFullStack.room_management_system_backend.Service.User;

import com.TrungTinhFullStack.room_management_system_backend.Dto.ReqRes;
import com.TrungTinhFullStack.room_management_system_backend.Entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
ReqRes login(ReqRes request);
User register( String username, String password, String email, MultipartFile img,
               String phoneNumber, String citizenIdentification,
               String address,String role) throws IOException;
List<User> getAllUser();
User getUserById(Long id);
User updateUser(Long id,String username, String password, String email, MultipartFile img,
                String phoneNumber, String citizenIdentification,
                String address,String role) throws IOException;
User deleteUser(Long id);
User unlockUser(Long id);
List<User> searchUserByUsername(String username);
String sendOtpToEmail(String email);
String verifyOtpAndChangePassword(String email, String otp, String newPassword) throws IllegalAccessException;
String sendMail(String to,String subject,String body);
}
