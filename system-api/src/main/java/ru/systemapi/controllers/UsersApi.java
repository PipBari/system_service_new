package ru.systemapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.systemapi.dto.UsersDTO;

import jakarta.validation.Valid;
import java.util.UUID;

@Tag(name = "Пользователи", description = "API для управления пользователями")
public interface UsersApi {

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь найден",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsersDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    @GetMapping(value = "/api/users/{id}", produces = "application/json")
    ResponseEntity<EntityModel<UsersDTO>> getUserById(
            @Parameter(description = "UUID пользователя", required = true)
            @PathVariable UUID id);

    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех зарегистрированных пользователей")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список пользователей успешно получен",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping(value = "/api/users", produces = "application/json")
    ResponseEntity<CollectionModel<EntityModel<UsersDTO>>> getAllUsers();

    @Operation(summary = "Создать нового пользователя", description = "Создает нового пользователя на основе переданных данных")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь успешно создан",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsersDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации данных"
            )
    })
    @PostMapping(value = "/api/users", consumes = "application/json", produces = "application/json")
    ResponseEntity<EntityModel<UsersDTO>> createUser(
            @Parameter(description = "Данные для создания пользователя", required = true)
            @Valid @RequestBody UsersDTO usersDTO);

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Пользователь успешно удален"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь с указанным ID не найден"
            )
    })
    @DeleteMapping(value = "/api/users/{id}")
    ResponseEntity<Void> deleteUser(
            @Parameter(description = "UUID пользователя", required = true)
            @PathVariable UUID id);
}
