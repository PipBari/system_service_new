package ru.servicemain.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import ru.servicemain.dao.entity.Regions;
import ru.servicemain.dao.rep.RegionsRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class RegionsInitializer {

    private RegionsRepository regionsRepository;

    @Autowired
    public RegionsInitializer(RegionsRepository regionsRepository) {
        this.regionsRepository = regionsRepository;
    }

    @PostConstruct
    public void initRegions() {
        List<String> regionNames = Arrays.asList("Moscow", "Kaluga", "Kazan", "Tver");

        for (String regionName : regionNames) {
            if (!regionsRepository.existsByRegion(regionName)) {
                Regions region = new Regions();
                region.setRegion(regionName);
                regionsRepository.save(region);
            }
        }
    }
}
