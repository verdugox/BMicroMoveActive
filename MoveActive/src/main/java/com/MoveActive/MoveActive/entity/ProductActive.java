package com.MoveActive.MoveActive.entity;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductActive {


    private String id;
    private String identityContract;
    private Double amount;
    private Double limitAmount;
    private String document;
    private String typeCredit;
    private String holder;
    private String signatory;
    private Double availableAmount;
    private LocalDate dateRegister;

}