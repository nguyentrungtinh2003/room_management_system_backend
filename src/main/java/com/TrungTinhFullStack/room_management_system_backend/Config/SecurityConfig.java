package com.TrungTinhFullStack.room_management_system_backend.Config;

import com.TrungTinhFullStack.room_management_system_backend.Entity.User;
import com.TrungTinhFullStack.room_management_system_backend.Enum.Role;
import com.TrungTinhFullStack.room_management_system_backend.Repository.UserRepository;
import com.TrungTinhFullStack.room_management_system_backend.Service.Jwt.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.net.URLEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt CSRF nếu không cần
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // Cho phép tất cả các yêu cầu
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // Trang đăng nhập tùy chỉnh cho OAuth2
                        .defaultSuccessUrl("http://localhost:3000/", true) // Chuyển hướng khi thành công
                        .failureUrl("/login?error=true") // Chuyển hướng khi thất bại
                        .successHandler((request, response, authentication) -> { // Xử lý khi đăng nhập thành công
                            var oauthUser = (org.springframework.security.oauth2.core.user.OAuth2User) authentication.getPrincipal();
                            String email = oauthUser.getAttribute("email");
                            String username = oauthUser.getAttribute("name");

                            // Tìm user theo username
                            User user = userRepository.findByEmail(email);

                            // Nếu không tồn tại, tạo mới và lưu vào DB
                            if (user == null) {
                                user = new User(); // Tạo đối tượng User mới
                                user.setEmail(email);
                                user.setUsername(username);
                                user.setRole(Role.TENANT);
                                userRepository.save(user); // Lưu vào cơ sở dữ liệu
                                response.sendRedirect("http://localhost:3000/" + user.getRole());
                            }

                            response.sendRedirect("http://localhost:3000/" + user.getRole());

                        })
                )
                .formLogin(form -> form
                        .loginPage("/login") // Trang đăng nhập tùy chỉnh cho form login
                        .defaultSuccessUrl("/success", true) // Chuyển hướng khi đăng nhập thành công
                        .failureUrl("/login?error=true") // Chuyển hướng khi thất bại
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")) // Xử lý khi chưa đăng nhập
                );

        return http.build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
