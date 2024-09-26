package com.TrungTinhFullStack.room_management_system_backend.Service.User;

import com.TrungTinhFullStack.room_management_system_backend.Dto.ReqRes;
import com.TrungTinhFullStack.room_management_system_backend.Entity.User;
import com.TrungTinhFullStack.room_management_system_backend.Enum.Role;
import com.TrungTinhFullStack.room_management_system_backend.Repository.UserRepository;
import com.TrungTinhFullStack.room_management_system_backend.Service.Jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String UPLOAD_DIR = "/uploads";

    Path uploadsPath = Paths.get(UPLOAD_DIR);

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

        String fileName = img.getOriginalFilename();
        Path filePath = uploadsPath.resolve(fileName);
        Files.write(filePath,img.getBytes());

        com.TrungTinhFullStack.room_management_system_backend.Entity.User user =
                userRepository.findByUsername(username);
        if(user != null) {
            throw new RuntimeException("User already exists");
        }

        Role role1 = Role.valueOf(role.toUpperCase());
        user.setImg(fileName);
        user.setEmail(email);
        user.setRole(role1);
        user.setAddress(address);
        user.setPassword(passwordEncoder.encode(password));
        user.setCitizenIdentification(citizenIdentification);
        user.setUsername(username);
        user.setPhoneNumber(phoneNumber);

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
                           String phoneNumber, String citizenIdentification, String address, String role) {
        User user = getUserById(id);
        if(password != null || password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        if(img != null || img.isEmpty()) {
            user.setImg(img.getOriginalFilename());
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
        userRepository.delete(user);
        return user;
    }
}
