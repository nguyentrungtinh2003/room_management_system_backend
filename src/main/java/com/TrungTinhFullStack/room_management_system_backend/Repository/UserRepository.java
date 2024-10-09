package com.TrungTinhFullStack.room_management_system_backend.Repository;

import com.TrungTinhFullStack.room_management_system_backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findByUsernameContainingIgnoreCase(String username);
}
