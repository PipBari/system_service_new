package ru.servicemain.dao.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "regions")
public class Regions extends BaseEntity {
    @Column(name = "region")
    private String region;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Alerts> alerts;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<Alerts> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alerts> alerts) {
        this.alerts = alerts;
    }
}
