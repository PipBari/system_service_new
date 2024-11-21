package ru.servicemain.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.servicemain.dao.entity.Alerts;
import ru.servicemain.dao.entity.Regions;
import ru.servicemain.dao.rep.AlertsRepository;
import ru.servicemain.dao.rep.RegionsRepository;
import ru.servicemain.service.AlertsService;
import ru.systemapi.dto.AlertsDTO;
import ru.systemapi.exception.ResourceNotFoundException;
import ru.systemapi.exception.BadRequestException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AlertsServiceImpl implements AlertsService {

    private final AlertsRepository alertsRepository;
    private final RegionsRepository regionsRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AlertsServiceImpl(AlertsRepository alertsRepository,
                             RegionsRepository regionsRepository,
                             ModelMapper modelMapper) {
        this.alertsRepository = alertsRepository;
        this.regionsRepository = regionsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AlertsDTO getAlertById(UUID id) {
        if (id == null) {
            throw new BadRequestException("Invalid ID", "Alert ID cannot be null");
        }

        Alerts alert = alertsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with ID: " + id));

        AlertsDTO dto = modelMapper.map(alert, AlertsDTO.class);
        mapAdditionalFields(alert, dto);
        return dto;
    }

    @Override
    public List<AlertsDTO> getAllAlerts() {
        List<Alerts> alerts = alertsRepository.findAll();

        if (alerts.isEmpty()) {
            throw new ResourceNotFoundException("No alerts found in the database");
        }

        return alerts.stream()
                .map(alert -> {
                    AlertsDTO dto = modelMapper.map(alert, AlertsDTO.class);
                    mapAdditionalFields(alert, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public AlertsDTO createAlert(AlertsDTO alertsDTO) {
        if (alertsDTO == null) {
            throw new BadRequestException("Invalid input", "Alert data cannot be null");
        }

        Regions region = regionsRepository.findByRegion(alertsDTO.getRegionName())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with name: " + alertsDTO.getRegionName()));

        Alerts alert = modelMapper.map(alertsDTO, Alerts.class);
        alert.setRegion(region);
        Alerts savedAlert = alertsRepository.save(alert);

        AlertsDTO savedDto = modelMapper.map(savedAlert, AlertsDTO.class);
        mapAdditionalFields(savedAlert, savedDto);
        return savedDto;
    }

    @Override
    public void deleteAlert(UUID id) {
        if (id == null) {
            throw new BadRequestException("Invalid ID", "Alert ID cannot be null");
        }

        if (!alertsRepository.existsById(id)) {
            throw new ResourceNotFoundException("Alert not found with ID: " + id);
        }

        alertsRepository.deleteById(id);
    }

    private void mapAdditionalFields(Alerts alert, AlertsDTO dto) {
        if (alert.getRegion() != null) {
            dto.setRegionName(alert.getRegion().getRegion());
        }
    }
}
