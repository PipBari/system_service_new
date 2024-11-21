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
import ru.systemapi.dto.AlertsDTO;

import jakarta.validation.Valid;
import java.util.UUID;

@Tag(name = "Оповещения", description = "API для управления оповещениями")
public interface AlertsApi {

    @Operation(summary = "Получить оповещение по ID", description = "Возвращает оповещение по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Оповещение найдено",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlertsDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Оповещение не найдено"
            )
    })
    @GetMapping(value = "/api/alerts/{id}", produces = "application/json")
    ResponseEntity<EntityModel<AlertsDTO>> getAlertById(
            @Parameter(description = "UUID оповещения", required = true)
            @PathVariable UUID id);

    @Operation(summary = "Получить все оповещения", description = "Возвращает список всех оповещений")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список оповещений успешно получен",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping(value = "/api/alerts", produces = "application/json")
    ResponseEntity<CollectionModel<EntityModel<AlertsDTO>>> getAllAlerts();

    @Operation(summary = "Создать новое оповещение", description = "Создает новое оповещение на основе переданных данных")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Оповещение успешно создано",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlertsDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации данных"
            )
    })
    @PostMapping(value = "/api/alerts", consumes = "application/json", produces = "application/json")
    ResponseEntity<EntityModel<AlertsDTO>> createAlert(
            @Parameter(description = "Данные для создания оповещения", required = true)
            @Valid @RequestBody AlertsDTO alertsDTO);

    @Operation(summary = "Удалить оповещение", description = "Удаляет оповещение по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Оповещение успешно удалено"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Оповещение с указанным ID не найдено"
            )
    })
    @DeleteMapping(value = "/api/alerts/{id}")
    ResponseEntity<Void> deleteAlert(
            @Parameter(description = "UUID оповещения", required = true)
            @PathVariable UUID id);
}
