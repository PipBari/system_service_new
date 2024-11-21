package ru.servicemain.dao.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.servicemain.dao.entity.Alerts;
import ru.servicemain.dao.entity.Regions;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlertsRepository extends JpaRepository<Alerts, UUID> {
    Optional<Alerts> findByDisasterAndRegion(String disaster, Regions region);
}
