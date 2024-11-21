package ru.servicemain.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.servicemain.dao.entity.Alerts;
import ru.servicemain.dao.entity.Regions;
import ru.servicemain.dao.entity.UserNotification;
import ru.servicemain.dao.entity.Users;
import ru.servicemain.dao.rep.AlertsRepository;
import ru.servicemain.dao.rep.RegionsRepository;
import ru.servicemain.dao.rep.UserNotificationRepository;
import ru.servicemain.dao.rep.UsersRepository;
import ru.servicemain.service.UserNotificationService;
import ru.systemapi.dto.UserNotificationDTO;
import ru.systemapi.exception.ResourceNotFoundException;
import ru.systemapi.exception.BadRequestException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserNotificationServiceImpl implements UserNotificationService {

    private final UserNotificationRepository userNotificationRepository;
    private final UsersRepository usersRepository;
    private final AlertsRepository alertsRepository;
    private final RegionsRepository regionsRepository;
    private final ModelMapper modelMapper;
    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName = "alerts.exchange";

    private static final Map<String, Integer> PRIORITY_MAP = Map.of(
            "fire", 10,
            "flood", 9,
            "earthquake", 8,
            "storm", 5,
            "strong wind", 3
    );

    @Autowired
    public UserNotificationServiceImpl(UserNotificationRepository userNotificationRepository,
                                       UsersRepository usersRepository,
                                       AlertsRepository alertsRepository,
                                       RegionsRepository regionsRepository,
                                       ModelMapper modelMapper,
                                       RabbitTemplate rabbitTemplate) {
        this.userNotificationRepository = userNotificationRepository;
        this.usersRepository = usersRepository;
        this.alertsRepository = alertsRepository;
        this.regionsRepository = regionsRepository;
        this.modelMapper = modelMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public UserNotificationDTO getNotificationById(UUID id) {
        if (id == null) {
            throw new BadRequestException("Invalid ID", "Notification ID cannot be null");
        }

        UserNotification notification = userNotificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + id));
        UserNotificationDTO dto = modelMapper.map(notification, UserNotificationDTO.class);
        mapAdditionalFields(notification, dto);
        return dto;
    }

    @Override
    public List<UserNotificationDTO> getAllNotifications() {
        List<UserNotification> notifications = userNotificationRepository.findAll();

        if (notifications.isEmpty()) {
            throw new ResourceNotFoundException("No notifications found");
        }

        return notifications.stream()
                .map(notification -> {
                    UserNotificationDTO dto = modelMapper.map(notification, UserNotificationDTO.class);
                    mapAdditionalFields(notification, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserNotificationDTO createNotification(UserNotificationDTO notificationDTO) {
        if (notificationDTO == null) {
            throw new BadRequestException("Invalid input", "Notification data cannot be null");
        }

        Users user = usersRepository.findByTelephoneNumber(notificationDTO.getUserTelephoneNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with telephone number: " + notificationDTO.getUserTelephoneNumber()));

        Regions region = regionsRepository.findByRegion(notificationDTO.getRegion())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with name: " + notificationDTO.getRegion()));

        Alerts alert = alertsRepository.findByDisasterAndRegion(notificationDTO.getAlertDisaster(), region)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with disaster: " + notificationDTO.getAlertDisaster() + " and region: " + notificationDTO.getRegion()));

        UserNotification notification = modelMapper.map(notificationDTO, UserNotification.class);
        notification.setUser(user);
        notification.setAlert(alert);
        notification.setDataCreate(LocalDateTime.now());

        UserNotification savedNotification = userNotificationRepository.save(notification);

        String routingKey = "disaster." + alert.getDisaster().toLowerCase();
        int priority = PRIORITY_MAP.getOrDefault(alert.getDisaster(), 1);

        rabbitTemplate.convertAndSend(exchangeName, routingKey, savedNotification.getId().toString(), message -> {
            message.getMessageProperties().setPriority(priority);
            return message;
        });

        UserNotificationDTO savedDto = modelMapper.map(savedNotification, UserNotificationDTO.class);
        mapAdditionalFields(savedNotification, savedDto);
        return savedDto;
    }

    @Override
    public void deleteNotification(UUID id) {
        if (id == null) {
            throw new BadRequestException("Invalid ID", "Notification ID cannot be null");
        }

        if (!userNotificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found with ID: " + id);
        }

        userNotificationRepository.deleteById(id);
    }

    @Override
    public UserNotificationDTO partUpdate(UUID id, UserNotificationDTO notificationDTO) {
        if (id == null) {
            throw new BadRequestException("Invalid ID", "Notification ID cannot be null");
        }

        UserNotification existingNotification = userNotificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + id));

        if (notificationDTO.getAlertMessage() != null) {
            existingNotification.getAlert().setMessage(notificationDTO.getAlertMessage());
        }
        if (notificationDTO.getAlertDisaster() != null) {
            existingNotification.getAlert().setDisaster(notificationDTO.getAlertDisaster());
        }
        if (notificationDTO.getRegion() != null) {
            existingNotification.getAlert().getRegion().setRegion(notificationDTO.getRegion());
        }
        if (notificationDTO.getUserFirstName() != null) {
            existingNotification.getUser().setFirstName(notificationDTO.getUserFirstName());
        }
        if (notificationDTO.getUserLastName() != null) {
            existingNotification.getUser().setSecondName(notificationDTO.getUserLastName());
        }
        if (notificationDTO.getDataCreate() != null) {
            existingNotification.setDataCreate(notificationDTO.getDataCreate());
        }

        UserNotification updatedNotification = userNotificationRepository.save(existingNotification);
        UserNotificationDTO updatedDto = modelMapper.map(updatedNotification, UserNotificationDTO.class);
        mapAdditionalFields(updatedNotification, updatedDto);
        return updatedDto;
    }

    private void mapAdditionalFields(UserNotification notification, UserNotificationDTO dto) {
        dto.setUserFirstName(notification.getUser().getFirstName());
        dto.setUserLastName(notification.getUser().getSecondName());
        dto.setUserTelephoneNumber(notification.getUser().getTelephoneNumber());
        dto.setRegion(notification.getAlert().getRegion().getRegion());
        dto.setAlertMessage(notification.getAlert().getMessage());
        dto.setAlertDisaster(notification.getAlert().getDisaster());
        dto.setDataCreate(notification.getDataCreate());
    }

    public String getProcessingStatus(UUID notificationId) {
        if (notificationId == null) {
            throw new BadRequestException("Invalid ID", "Notification ID cannot be null");
        }

        return userNotificationRepository.existsById(notificationId) ? "Processed" : "Not found";
    }
}
