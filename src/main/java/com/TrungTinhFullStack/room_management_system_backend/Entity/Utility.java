package com.TrungTinhFullStack.room_management_system_backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Utility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    private Date month;

    private Double electricityUsage;
    private Double waterUsage;

    private Double electricityCost;
    private Double waterCost;
}
