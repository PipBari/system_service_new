package ru.servicemain.dao.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.servicemain.dao.entity.UserNotification;
import ru.servicemain.dao.entity.Users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, UUID> {
    boolean existsByUserAndDataCreate(Users user, LocalDateTime dataCreate);
}

