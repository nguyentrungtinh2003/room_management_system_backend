package com.TrungTinhFullStack.room_management_system_backend.Service.User;

import com.TrungTinhFullStack.room_management_system_backend.Dto.ReqRes;
import com.TrungTinhFullStack.room_management_system_backend.Entity.User;
import com.TrungTinhFullStack.room_management_system_backend.Enum.Role;
import com.TrungTinhFullStack.room_management_system_backend.Repository.UserRepository;
import com.TrungTinhFullStack.room_management_system_backend.Service.Email.EmailService;
import com.TrungTinhFullStack.room_management_system_backend.Service.Jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public ReqRes login(ReqRes request) {
        ReqRes res = new ReqRes();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
            var user = userRepository.findByUsername(request.getUsername());
            if(user == null) {
                throw new BadCredentialsException("User not found !");
            }
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(),user);

            res.setId(user.getId());
            res.setUsername(user.getUsername());
            res.setEnable(true);
            res.setAddress(user.getAddress());
            res.setEmail(user.getEmail());
            res.setImg(user.getImg());
            res.setMessage("Login success !");
            res.setRole(user.getRole());
            res.setPassword(user.getPassword());
            res.setPhoneNumber(user.getPhoneNumber());
            res.setToken(jwt);
            res.setExpirationTime("24Hrs");
            res.setRefreshToken(refreshToken);
            res.setStartDate(user.getStartDate());
            res.setStatusCode(200L);
            res.setCitizenIdentification(user.getCitizenIdentification());

            return res;
        }catch(Exception e) {
            res.setMessage(e.getMessage());
            res.setStatusCode(500L);
            return res;
        }
    }

    @Override
    public User register(
            String username, String password, String email,
            MultipartFile img, String phoneNumber, String citizenIdentification,
            String address,String role) throws IOException {

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = null;
            fileName = img.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath,img.getBytes());


        User user1 = userRepository.findByUsername(username);
        if(user1 != null && user1.getUsername().equals(username)) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        Role role1 = Role.valueOf(role.toUpperCase());
        user.setImg(fileName);
        user.setEmail(email);
        user.setRole(role1);
        user.setAddress(address);
        user.setPassword(passwordEncoder.encode(password));
        user.setCitizenIdentification(citizenIdentification);
        user.setUsername(username);
        user.setEnabled(true);
        user.setPhoneNumber(phoneNumber);
        user.setStartDate(new Date());

        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(null);
        return user;
    }

    @Override
    public User updateUser(Long id, String username, String password, String email, MultipartFile img,
                           String phoneNumber, String citizenIdentification, String address, String role) throws IOException {
        User user = getUserById(id);
        if(password != null && password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        if(img != null && img.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(img.getOriginalFilename()));
            user.setImg(fileName);
            Path path = Paths.get("uploads/" + fileName);
            Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }

        if (role != null && !role.isEmpty()) {
            user.setRole(Role.valueOf(role.toUpperCase()));
        }

        user.setEmail(email);
        user.setAddress(address);
        user.setCitizenIdentification(citizenIdentification);
        user.setUsername(username);
        user.setPhoneNumber(phoneNumber);

        userRepository.save(user);

        return user;
    }


    @Override
    public User deleteUser(Long id) {
        User user = getUserById(id);
        user.setEnabled(false);
        userRepository.save(user);
        return user;
    }

    @Override
    public User unlockUser(Long id) {
        User user = getUserById(id);
        user.setEnabled(true);
        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> searchUserByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    @Override
    public String sendOtpToEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            return ("Email không tồn tại !");
        }

        String otp = String.format("%06d",new Random().nextInt(999999));

        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

        userRepository.save(user);

        emailService.sendMail(email,"Mã OTP của bạn",
                "Mã OTP : " + otp + " có hiệu lực trong 10 phút !");
        return "Mã OTP đã gửi !";
    }

    @Override
    public String verifyOtpAndChangePassword(String email, String otp, String newPassword) throws IllegalAccessException {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            return "Email không tồn tại !";
        }
        if(!user.getOtp().equals(otp)) {
            throw new IllegalAccessException("Mã OTP không hợp lệ !");
        }
        if(user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("OTP đã hết hạn");
        }
        user.setOtp(null);
        user.setOtpExpiry(null);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "Mật khẩu đã thay đổi thành công !";
    }

    @Override
    public String sendMail(String to, String subject, String body) {
        emailService.sendMail(to,subject,body);
        return "Gửi email thành công !";
    }
}