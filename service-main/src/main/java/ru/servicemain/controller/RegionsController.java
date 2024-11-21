package ru.servicemain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.servicemain.service.RegionsService;
import ru.systemapi.controllers.RegionsApi;
import ru.systemapi.dto.RegionsDTO;
import ru.systemapi.exception.ResourceNotFoundException;
import ru.systemapi.exception.BadRequestException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class RegionsController implements RegionsApi {

    private final RegionsService regionsService;

    @Autowired
    public RegionsController(RegionsService regionsService) {
        this.regionsService = regionsService;
    }

    @Override
    public ResponseEntity<CollectionModel<EntityModel<RegionsDTO>>> getAllRegions() {
        try {
            List<EntityModel<RegionsDTO>> regions = regionsService.getAllRegions().stream()
                    .map(region -> EntityModel.of(region,
                            linkTo(methodOn(RegionsController.class).getRegionById(region.getId())).withSelfRel(),
                            linkTo(methodOn(RegionsController.class).getAllRegions()).withRel("all-regions")))
                    .collect(Collectors.toList());

            CollectionModel<EntityModel<RegionsDTO>> regionsCollection = CollectionModel.of(regions,
                    linkTo(methodOn(RegionsController.class).getAllRegions()).withSelfRel());

            return ResponseEntity.ok(regionsCollection);
        } catch (Exception e) {
            throw new BadRequestException("Failed to fetch all regions", e.getMessage());
        }
    }

    @Override
    public ResponseEntity<EntityModel<RegionsDTO>> getRegionById(UUID id) {
        if (id == null) {
            throw new BadRequestException("Invalid ID", "Region ID cannot be null");
        }

        try {
            RegionsDTO region = regionsService.getRegionById(id);
            EntityModel<RegionsDTO> regionModel = EntityModel.of(region,
                    linkTo(methodOn(RegionsController.class).getRegionById(id)).withSelfRel(),
                    linkTo(methodOn(RegionsController.class).getAllRegions()).withRel("all-regions"));

            return ResponseEntity.ok(regionModel);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Region not found with ID: " + id);
        } catch (Exception e) {
            throw new BadRequestException("Failed to fetch region by ID", e.getMessage());
        }
    }
}
