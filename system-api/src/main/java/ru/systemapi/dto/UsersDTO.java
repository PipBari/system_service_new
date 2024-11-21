package ru.systemapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {

    private UUID id;

    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @NotBlank(message = "Second name cannot be empty")
    private String secondName;

    @NotBlank(message = "Telephone number cannot be empty")
    @Pattern(regexp = "\\+?[0-9\\-\\s]+", message = "Invalid telephone number format")
    private String telephoneNumber;
}
