package com.TrungTinhFullStack.room_management_system_backend.Entity;

import com.TrungTinhFullStack.room_management_system_backend.Enum.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String img;

    private String phoneNumber;

    private String citizenIdentification;

    private String address;

    private Date startDate;

    private Role role;
}
