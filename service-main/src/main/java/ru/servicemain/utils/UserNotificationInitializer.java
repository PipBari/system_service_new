package ru.servicemain.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.servicemain.dao.entity.Alerts;
import ru.servicemain.dao.entity.UserNotification;
import ru.servicemain.dao.entity.Users;
import ru.servicemain.dao.rep.AlertsRepository;
import ru.servicemain.dao.rep.UserNotificationRepository;
import ru.servicemain.dao.rep.UsersRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@Component
@DependsOn({"regionsInitializer", "alertsInitializer", "usersInitializer"})
public class UserNotificationInitializer {

    private final UsersRepository usersRepository;
    private final AlertsRepository alertsRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final Random random = new Random();

    @Autowired
    public UserNotificationInitializer(UsersRepository usersRepository,
                                       AlertsRepository alertsRepository,
                                       UserNotificationRepository userNotificationRepository) {
        this.usersRepository = usersRepository;
        this.alertsRepository = alertsRepository;
        this.userNotificationRepository = userNotificationRepository;
    }

    @PostConstruct
    @Transactional
    public void initNotifications() {
        if (userNotificationRepository.count() > 0) {
            System.out.println("Notifications already initialized. Skipping generation.");
            return;
        }

        System.out.println("Initializing notifications...");
        List<Users> users = usersRepository.findAll();
        List<Alerts> alerts = alertsRepository.findAll();

        if (alerts.isEmpty()) {
            System.out.println("No alerts found! Please ensure alerts are initialized.");
            return;
        }

        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        for (Users user : users) {
            if (!hasNotificationForDateTime(user, currentDateTime)) {
                UserNotification notification = new UserNotification();
                notification.setUser(user);
                notification.setDataCreate(currentDateTime);

                Alerts randomAlert = alerts.get(random.nextInt(alerts.size()));
                notification.setAlert(randomAlert);

                userNotificationRepository.save(notification);
            }
        }

        System.out.println("Notifications initialized.");
    }

    private boolean hasNotificationForDateTime(Users user, LocalDateTime dateTime) {
        return userNotificationRepository.existsByUserAndDataCreate(user, dateTime);
    }
}
