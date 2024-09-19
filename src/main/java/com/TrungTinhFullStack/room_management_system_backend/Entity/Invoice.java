package com.TrungTinhFullStack.room_management_system_backend.Entity;

import com.TrungTinhFullStack.room_management_system_backend.Enum.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Room room;

    private Date month;

    private Double rentAmount;

    private Double totalUtilityCost;

    private Double totalAmountDue;

    private PaymentStatus status;
}
