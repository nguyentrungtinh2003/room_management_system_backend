package com.TrungTinhFullStack.room_management_system_backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    @ManyToOne()
    @JoinColumn(name = "landlord_id")
    private User landlord;
}
