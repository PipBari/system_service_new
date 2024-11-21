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
import ru.systemapi.dto.RegionsDTO;

import java.util.UUID;

@Tag(name = "Регионы", description = "API для управления регионами")
public interface RegionsApi {

    @Operation(summary = "Получить список всех регионов", description = "Возвращает список всех регионов")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список регионов успешно получен",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping(value = "/api/regions", produces = "application/json")
    ResponseEntity<CollectionModel<EntityModel<RegionsDTO>>> getAllRegions();

    @Operation(summary = "Получить регион по ID", description = "Возвращает регион по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Регион найден",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegionsDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Регион не найден"
            )
    })
    @GetMapping(value = "/api/regions/{id}", produces = "application/json")
    ResponseEntity<EntityModel<RegionsDTO>> getRegionById(
            @Parameter(description = "UUID региона", required = true)
            @PathVariable UUID id);
}
