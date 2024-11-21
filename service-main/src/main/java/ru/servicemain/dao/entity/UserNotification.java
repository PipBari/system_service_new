package ru.servicemain.dao.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_notification")
public class UserNotification extends BaseEntity {

    @Column(name = "data_create")
    private LocalDateTime dataCreate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "alert_id")
    private Alerts alert;

    public LocalDateTime getDataCreate() {return dataCreate;}
    public void setDataCreate(LocalDateTime dataCreate) {this.dataCreate = dataCreate;}
    public Users getUser() {
        return user;
    }
    public void setUser(Users user) {
        this.user = user;
    }
    public Alerts getAlert() {
        return alert;
    }
    public void setAlert(Alerts alert) {
        this.alert = alert;
    }
}
