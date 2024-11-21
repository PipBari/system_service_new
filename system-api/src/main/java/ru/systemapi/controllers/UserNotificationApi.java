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
import ru.systemapi.dto.UserNotificationDTO;

import jakarta.validation.Valid;
import java.util.UUID;

@Tag(name = "Уведомления пользователей", description = "API для управления уведомлениями пользователей")
public interface UserNotificationApi {

    @Operation(summary = "Получить уведомление по ID", description = "Возвращает уведомление по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Уведомление найдено",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserNotificationDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Уведомление не найдено"
            )
    })
    @GetMapping(value = "/api/user-notifications/{id}", produces = "application/json")
    ResponseEntity<EntityModel<UserNotificationDTO>> getNotificationById(
            @Parameter(description = "UUID уведомления", required = true)
            @PathVariable UUID id);

    @Operation(summary = "Получить все уведомления", description = "Возвращает список всех уведомлений")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список уведомлений успешно получен",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping(value = "/api/user-notifications", produces = "application/json")
    ResponseEntity<CollectionModel<EntityModel<UserNotificationDTO>>> getAllNotifications();

    @Operation(summary = "Создать уведомление", description = "Создает новое уведомление на основе предоставленных данных")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Уведомление успешно создано",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserNotificationDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации данных"
            )
    })
    @PostMapping(value = "/api/user-notifications", consumes = "application/json", produces = "application/json")
    ResponseEntity<EntityModel<UserNotificationDTO>> createNotification(
            @Parameter(description = "Данные для создания уведомления", required = true)
            @Valid @RequestBody UserNotificationDTO userNotificationDTO);

    @Operation(summary = "Удалить уведомление", description = "Удаляет уведомление по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Уведомление успешно удалено"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Уведомление с указанным ID не найдено"
            )
    })
    @DeleteMapping(value = "/api/user-notifications/{id}")
    ResponseEntity<Void> deleteNotification(
            @Parameter(description = "UUID уведомления", required = true)
            @PathVariable UUID id);
}
