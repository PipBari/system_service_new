package ru.servicemain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.servicemain.service.UserNotificationService;
import ru.systemapi.controllers.UserNotificationApi;
import ru.systemapi.dto.UserNotificationDTO;

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
        UserNotificationDTO notification = userNotificationService.getNotificationById(id);

        EntityModel<UserNotificationDTO> notificationModel = EntityModel.of(notification,
                linkTo(methodOn(UserNotificationController.class).getNotificationById(id)).withSelfRel(),
                linkTo(methodOn(UserNotificationController.class).getAllNotifications()).withRel("all-notifications"));

        return ResponseEntity.ok(notificationModel);
    }

    @Override
    public ResponseEntity<CollectionModel<EntityModel<UserNotificationDTO>>> getAllNotifications() {
        List<EntityModel<UserNotificationDTO>> notifications = userNotificationService.getAllNotifications().stream()
                .map(notification -> EntityModel.of(notification,
                        linkTo(methodOn(UserNotificationController.class).getNotificationById(notification.getId())).withSelfRel(),
                        linkTo(methodOn(UserNotificationController.class).getAllNotifications()).withRel("all-notifications")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<UserNotificationDTO>> notificationsCollection = CollectionModel.of(notifications,
                linkTo(methodOn(UserNotificationController.class).getAllNotifications()).withSelfRel());

        return ResponseEntity.ok(notificationsCollection);
    }

    @Override
    public ResponseEntity<EntityModel<UserNotificationDTO>> createNotification(UserNotificationDTO userNotificationDTO) {
        UserNotificationDTO createdNotification = userNotificationService.createNotification(userNotificationDTO);

        EntityModel<UserNotificationDTO> notificationModel = EntityModel.of(createdNotification,
                linkTo(methodOn(UserNotificationController.class).getNotificationById(createdNotification.getId())).withSelfRel(),
                linkTo(methodOn(UserNotificationController.class).getAllNotifications()).withRel("all-notifications"));

        return ResponseEntity
                .created(linkTo(methodOn(UserNotificationController.class).getNotificationById(createdNotification.getId())).toUri())
                .body(notificationModel);
    }

    @Override
    public ResponseEntity<Void> deleteNotification(UUID id) {
        userNotificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
