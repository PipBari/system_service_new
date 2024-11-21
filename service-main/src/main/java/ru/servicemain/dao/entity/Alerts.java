package ru.servicemain.dao.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "alerts")
public class Alerts extends BaseEntity {

    @Column(name = "disaster")
    private String disaster;

    @Column(name = "message")
    private String message;

    @Column(name = "created_date")
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Regions region;

    public String getDisaster() {
        return disaster;
    }

    public void setDisaster(String disaster) {
        this.disaster = disaster;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Regions getRegion() {
        return region;
    }

    public void setRegion(Regions region) {
        this.region = region;
    }
}
