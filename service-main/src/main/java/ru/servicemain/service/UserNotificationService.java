package ru.servicemain.service;

import ru.systemapi.dto.UserNotificationDTO;

import java.util.List;
import java.util.UUID;

public interface UserNotificationService {
    UserNotificationDTO getNotificationById(UUID id);
    List<UserNotificationDTO> getAllNotifications();
    UserNotificationDTO createNotification(UserNotificationDTO notificationDTO);
    void deleteNotification(UUID id);
    UserNotificationDTO partUpdate(UUID id, UserNotificationDTO notificationDTO);
    String getProcessingStatus(UUID notificationId);
}
