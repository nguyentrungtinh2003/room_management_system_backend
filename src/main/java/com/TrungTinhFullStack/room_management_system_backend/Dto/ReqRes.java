package com.TrungTinhFullStack.room_management_system_backend.Dto;

import com.TrungTinhFullStack.room_management_system_backend.Enum.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

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

    private Long statusCode;

    private String message;

    private String token;

    private String refreshToken;

    private String expirationTime;

    private boolean enable;
}
