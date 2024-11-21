package ru.systemapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationDTO {

    private UUID id;

    @NotNull(message = "Creation date cannot be null")
    private LocalDateTime dataCreate;

    @NotBlank(message = "Alert message cannot be empty")
    private String alertMessage;

    @NotBlank(message = "Disaster type cannot be empty")
    private String alertDisaster;

    @NotBlank(message = "Region cannot be empty")
    private String region;

    @NotBlank(message = "User first name cannot be empty")
    private String userFirstName;

    @NotBlank(message = "User last name cannot be empty")
    private String userLastName;

    @NotBlank(message = "Telephone number cannot be empty")
    @Pattern(regexp = "\\+?[0-9\\-\\s]+", message = "Invalid telephone number format")
    private String userTelephoneNumber;
}
