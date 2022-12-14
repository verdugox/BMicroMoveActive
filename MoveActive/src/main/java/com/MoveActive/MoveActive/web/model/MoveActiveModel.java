package com.MoveActive.MoveActive.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoveActiveModel {

    @JsonIgnore
    private String id;
    @NotBlank(message="Contract Number cannot be null or empty")
    private String identityContract;
    @NotNull(message="Amount Number cannot be null or empty")
    private Double amount;
    @NotBlank(message="operationType cannot be null or empty")
    private String operationType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateRegister;

}
