package ru.servicemain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.servicemain.service.AlertsService;
import ru.systemapi.controllers.AlertsApi;
import ru.systemapi.dto.AlertsDTO;
import ru.systemapi.exception.BadRequestException;
import ru.systemapi.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class AlertsController implements AlertsApi {

    private final AlertsService alertsService;

    @Autowired
    public AlertsController(AlertsService alertsService) {
        this.alertsService = alertsService;
    }

    @Override
    public ResponseEntity<EntityModel<AlertsDTO>> getAlertById(UUID id) {
        if (id == null) {
            throw new BadRequestException("Invalid ID", "Alert ID cannot be null");
        }

        AlertsDTO alert = alertsService.getAlertById(id);

        EntityModel<AlertsDTO> alertModel = EntityModel.of(alert,
                linkTo(methodOn(AlertsController.class).getAlertById(id)).withSelfRel(),
                linkTo(methodOn(AlertsController.class).getAllAlerts()).withRel("all-alerts"));

        return ResponseEntity.ok(alertModel);
    }

    @Override
    public ResponseEntity<CollectionModel<EntityModel<AlertsDTO>>> getAllAlerts() {
        List<AlertsDTO> alerts = alertsService.getAllAlerts();

        if (alerts.isEmpty()) {
            throw new ResourceNotFoundException("No alerts found in the database");
        }

        List<EntityModel<AlertsDTO>> alertModels = alerts.stream()
                .map(alert -> EntityModel.of(alert,
                        linkTo(methodOn(AlertsController.class).getAlertById(alert.getId())).withSelfRel(),
                        linkTo(methodOn(AlertsController.class).getAllAlerts()).withRel("all-alerts")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<AlertsDTO>> alertsCollection = CollectionModel.of(alertModels,
                linkTo(methodOn(AlertsController.class).getAllAlerts()).withSelfRel());

        return ResponseEntity.ok(alertsCollection);
    }

    @Override
    public ResponseEntity<EntityModel<AlertsDTO>> createAlert(AlertsDTO alertsDTO) {
        if (alertsDTO == null) {
            throw new BadRequestException("Invalid input", "Alert data cannot be null");
        }

        AlertsDTO createdAlert = alertsService.createAlert(alertsDTO);

        EntityModel<AlertsDTO> alertModel = EntityModel.of(createdAlert,
                linkTo(methodOn(AlertsController.class).getAlertById(createdAlert.getId())).withSelfRel(),
                linkTo(methodOn(AlertsController.class).getAllAlerts()).withRel("all-alerts"));

        return ResponseEntity
                .created(linkTo(methodOn(AlertsController.class).getAlertById(createdAlert.getId())).toUri())
                .body(alertModel);
    }

    @Override
    public ResponseEntity<Void> deleteAlert(UUID id) {
        if (id == null) {
            throw new BadRequestException("Invalid ID", "Alert ID cannot be null");
        }

        alertsService.deleteAlert(id);
        return ResponseEntity.noContent().build();
    }
}
