package ru.servicemain.graphql;


import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import ru.servicemain.service.UserNotificationService;
import ru.servicemain.service.UsersService;
import ru.systemapi.dto.UserNotificationDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@DgsComponent
public class UserNotificationDataFetcher {

    private final UserNotificationService userNotificationService;
    private final UsersService usersService;

    public UserNotificationDataFetcher(UserNotificationService userNotificationService, UsersService usersService) {
        this.userNotificationService = userNotificationService;
        this.usersService = usersService;
    }

    @DgsQuery
    public List<UserNotificationDTO> allNotifications() {
        return userNotificationService.getAllNotifications();
    }

    @DgsQuery
    public UserNotificationDTO notificationById(@InputArgument("id") UUID id) {
        return userNotificationService.getNotificationById(id);
    }

    @DgsMutation
    public UserNotificationDTO createNotification(
            @InputArgument("region") String region,
            @InputArgument("userId") UUID userId,
            @InputArgument("alertMessage") String alertMessage,
            @InputArgument("alertDisaster") String alertDisaster,
            @InputArgument("dataCreate") LocalDateTime dataCreate
    ) {
        UserNotificationDTO newNotification = new UserNotificationDTO();
        newNotification.setRegion(region);

        var userDTO = usersService.getUserById(userId);
        newNotification.setUserFirstName(userDTO.getFirstName());
        newNotification.setUserLastName(userDTO.getSecondName());

        newNotification.setAlertMessage(alertMessage);
        newNotification.setAlertDisaster(alertDisaster);

        newNotification.setDataCreate(dataCreate != null ? dataCreate : LocalDateTime.now());

        return userNotificationService.createNotification(newNotification);
    }

    @DgsMutation
    public Boolean deleteNotification(@InputArgument("id") UUID id) {
        userNotificationService.deleteNotification(id);
        return true;
    }

    @DgsMutation
    public UserNotificationDTO updateNotification(
            @InputArgument("id") UUID id,
            @InputArgument("region") String region,
            @InputArgument("alertMessage") String alertMessage,
            @InputArgument("alertDisaster") String alertDisaster,
            @InputArgument("dataCreate") LocalDateTime dataCreate
    ) {
        UserNotificationDTO updatedNotification = new UserNotificationDTO();
        updatedNotification.setRegion(region);
        updatedNotification.setAlertMessage(alertMessage);
        updatedNotification.setAlertDisaster(alertDisaster);

        updatedNotification.setDataCreate(dataCreate);

        return userNotificationService.partUpdate(id, updatedNotification);
    }
}
