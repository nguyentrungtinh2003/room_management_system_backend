package com.TrungTinhFullStack.room_management_system_backend.Entity;

import com.TrungTinhFullStack.room_management_system_backend.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "room_id")
    private Room room;

    private Date month;

    private Double rentAmount;

    private Double totalUtilityCost;

    private Double totalAmountDue;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
