package ru.servicemain.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import ru.servicemain.dao.entity.Alerts;
import ru.servicemain.dao.entity.Regions;
import ru.servicemain.dao.rep.AlertsRepository;
import ru.servicemain.dao.rep.RegionsRepository;

import java.util.*;

@Component
@DependsOn("regionsInitializer")
public class AlertsInitializer {

    private final AlertsRepository alertsRepository;
    private final RegionsRepository regionsRepository;

    private static final Map<String, Alerts> ALERTS_MAP = new HashMap<>();

    @Autowired
    public AlertsInitializer(AlertsRepository alertsRepository, RegionsRepository regionsRepository) {
        this.alertsRepository = alertsRepository;
        this.regionsRepository = regionsRepository;
    }

    @PostConstruct
    public void initAlerts() {
        if (alertsRepository.count() > 0) {
            System.out.println("Alerts already initialized. Loading existing alerts.");
            alertsRepository.findAll().forEach(alert ->
                    ALERTS_MAP.put(alert.getDisaster().trim().toLowerCase(), alert));
            System.out.println("Loaded existing alerts into ALERTS_MAP: " + ALERTS_MAP.keySet());
            return;
        }

        List<Regions> regions = regionsRepository.findAll();
        if (regions.isEmpty()) {
            System.err.println("No regions found! Please ensure regions are initialized.");
            return;
        }

        List<String> disasters = Arrays.asList("flood", "storm", "fire", "earthquake", "strong wind");
        List<String> messages = Arrays.asList(
                "Severe flooding reported in the area. Evacuation advised.",
                "A strong storm is approaching with winds up to 100 km/h.",
                "Forest fire detected near the region. Firefighters are on-site.",
                "Minor earthquake felt in the region. No damages reported yet.",
                "Strong winds expected. Secure loose objects outdoors."
        );

        for (Regions region : regions) {
            for (int i = 0; i < disasters.size(); i++) {
                Alerts alert = new Alerts();
                alert.setDisaster(disasters.get(i));
                alert.setMessage(messages.get(i));
                alert.setCreatedDate(new Date());
                alert.setRegion(region);
                Alerts savedAlert = alertsRepository.save(alert);
                ALERTS_MAP.put(disasters.get(i).trim().toLowerCase(), savedAlert);
                System.out.println("Alert created: " + disasters.get(i) + " in region " + region.getRegion());
            }
        }

        System.out.println("ALERTS_MAP initialized with keys: " + ALERTS_MAP.keySet());
    }

    public static Alerts getAlertByDisasterType(String disasterType) {
        System.out.println("Fetching alert for disaster type: " + disasterType);
        Alerts alert = ALERTS_MAP.get(disasterType.trim().toLowerCase());
        if (alert == null) {
            System.err.println("Alert not found for disaster type: " + disasterType);
        }
        return alert;
    }
}
