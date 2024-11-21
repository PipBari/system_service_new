package ru.servicemain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.servicemain.service.UserNotificationService;
import ru.systemapi.controllers.UserNotificationApi;
import ru.systemapi.dto.UserNotificationDTO;
import ru.systemapi.exception.ResourceNotFoundException;
import ru.systemapi.exception.BadRequestException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class UserNotificationController implements UserNotificationApi {

    private final UserNotificationService userNotificationService;

    @Autowired
    public UserNotificationController(UserNotificationService userNotificationService) {
        this.userNotificationService = userNotificationService;
    }

    @Override
    public ResponseEntity<EntityModel<UserNotificationDTO>> getNotificationById(UUID id) {
        if (id == null) {
            throw new BadRequestException("Invalid ID", "Notification ID cannot be null");
        }

        try {
            UserNotificationDTO notification = userNotificationService.getNotificationById(id);

            EntityModel<UserNotificationDTO> notificationModel = EntityModel.of(notification,
                    linkTo(methodOn(UserNotificationController.class).getNotificationById(id)).withSelfRel(),
                    linkTo(methodOn(UserNotificationController.class).getAllNotifications()).withRel("all-notifications"));

            return ResponseEntity.ok(notificationModel);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Notification not found with ID: " + id);
        } catch (Exception e) {
            throw new BadRequestException("Failed to fetch notification by ID", e.getMessage());
        }
    }

    @Override
    public ResponseEntity<CollectionModel<EntityModel<UserNotificationDTO>>> getAllNotifications() {
        try {
            List<EntityModel<UserNotificationDTO>> notifications = userNotificationService.getAllNotifications().stream()
                    .map(notification -> EntityModel.of(notification,
                            linkTo(methodOn(UserNotificationController.class).getNotificationById(notification.getId())).withSelfRel(),
                            linkTo(methodOn(UserNotificationController.class).getAllNotifications()).withRel("all-notifications")))
                    .collect(Collectors.toList());

            CollectionModel<EntityModel<UserNotificationDTO>> notificationsCollection = CollectionModel.of(notifications,
                    linkTo(methodOn(UserNotificationController.class).getAllNotifications()).withSelfRel());

            return ResponseEntity.ok(notificationsCollection);
        } catch (Exception e) {
            throw new BadRequestException("Failed to fetch all notifications", e.getMessage());
        }
    }

    @Override
    public ResponseEntity<EntityModel<UserNotificationDTO>> createNotification(UserNotificationDTO userNotificationDTO) {
        if (userNotificationDTO == null) {
            throw new BadRequestException("Invalid input", "User notification data cannot be null");
        }

        try {
            UserNotificationDTO createdNotification = userNotificationService.createNotification(userNotificationDTO);

            EntityModel<UserNotificationDTO> notificationModel = EntityModel.of(createdNotification,
                    linkTo(methodOn(UserNotificationController.class).getNotificationById(createdNotification.getId())).withSelfRel(),
                    linkTo(methodOn(UserNotificationController.class).getAllNotifications()).withRel("all-notifications"));

            return ResponseEntity
                    .created(linkTo(methodOn(UserNotificationController.class).getNotificationById(createdNotification.getId())).toUri())
                    .body(notificationModel);
        } catch (Exception e) {
            throw new BadRequestException("Failed to create notification", e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Void> deleteNotification(UUID id) {
        if (id == null) {
            throw new BadRequestException("Invalid ID", "Notification ID cannot be null");
        }

        try {
            userNotificationService.deleteNotification(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Notification not found with ID: " + id);
        } catch (Exception e) {
            throw new BadRequestException("Failed to delete notification", e.getMessage());
        }
    }
}
