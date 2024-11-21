package ru.servicemain.dao.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.servicemain.dao.entity.Regions;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegionsRepository extends JpaRepository<Regions, UUID> {
    boolean existsByRegion(String region);
    Optional<Regions> findByRegion(String regionName);
}
