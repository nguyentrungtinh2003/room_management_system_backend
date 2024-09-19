package com.TrungTinhFullStack.room_management_system_backend.Entity;

import com.TrungTinhFullStack.room_management_system_backend.Enum.RoomStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    @ManyToOne()
    @JoinColumn(name = "building_id")
    private Building building;

    private Double rentPrice;

    @ManyToMany()
    @JoinTable(
            name = "room_tenant",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "tenant_id")
    )
    private List<User> tenants = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private RoomStatus status;
}
