package ru.servicemain.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.servicemain.dao.entity.Regions;
import ru.servicemain.dao.rep.RegionsRepository;
import ru.servicemain.service.RegionsService;
import ru.systemapi.dto.RegionsDTO;
import ru.systemapi.exception.ResourceNotFoundException;
import ru.systemapi.exception.BadRequestException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegionsServiceImpl implements RegionsService {

    private final RegionsRepository regionsRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RegionsServiceImpl(RegionsRepository regionsRepository, ModelMapper modelMapper) {
        this.regionsRepository = regionsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<RegionsDTO> getAllRegions() {
        List<Regions> regions = regionsRepository.findAll();

        if (regions.isEmpty()) {
            throw new ResourceNotFoundException("No regions found in the database");
        }

        return regions.stream()
                .map(region -> modelMapper.map(region, RegionsDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RegionsDTO getRegionById(UUID id) {
        if (id == null) {
            throw new BadRequestException("Invalid ID", "Region ID cannot be null");
        }

        Regions region = regionsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + id));

        return modelMapper.map(region, RegionsDTO.class);
    }
}
