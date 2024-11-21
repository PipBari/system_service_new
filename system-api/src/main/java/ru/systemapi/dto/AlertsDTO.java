package ru.systemapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlertsDTO {

    private UUID id;

    @NotBlank(message = "Disaster type cannot be empty")
    private String disaster;

    @NotBlank(message = "Message cannot be empty")
    private String message;

    @NotNull(message = "Created date cannot be null")
    private Date createdDate;

    @NotBlank(message = "Region name cannot be empty")
    private String regionName;
}
