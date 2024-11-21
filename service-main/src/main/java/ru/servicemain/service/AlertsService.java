package ru.servicemain.service;

import ru.systemapi.dto.AlertsDTO;

import java.util.List;
import java.util.UUID;

public interface AlertsService {
    AlertsDTO getAlertById(UUID id);
    List<AlertsDTO> getAllAlerts();
    AlertsDTO createAlert(AlertsDTO alertsDTO);
    void deleteAlert(UUID id);
}
