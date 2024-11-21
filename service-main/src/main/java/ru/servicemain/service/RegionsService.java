package ru.servicemain.service;

import ru.systemapi.dto.RegionsDTO;

import java.util.List;
import java.util.UUID;

public interface RegionsService {
    List<RegionsDTO> getAllRegions();
    RegionsDTO getRegionById(UUID id);
}
