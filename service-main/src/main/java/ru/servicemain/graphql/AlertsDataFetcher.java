package ru.servicemain.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import ru.servicemain.service.AlertsService;
import ru.systemapi.dto.AlertsDTO;

import java.util.List;
import java.util.UUID;

@DgsComponent
public class AlertsDataFetcher {

    private final AlertsService alertsService;

    public AlertsDataFetcher(AlertsService alertsService) {
        this.alertsService = alertsService;
    }

    @DgsQuery
    public List<AlertsDTO> allAlerts() {
        return alertsService.getAllAlerts();
    }

    @DgsQuery
    public AlertsDTO alertById(@InputArgument("id") UUID id) {
        return alertsService.getAlertById(id);
    }

    @DgsMutation
    public AlertsDTO createAlert(
            @InputArgument("disaster") String disaster,
            @InputArgument("message") String message,
            @InputArgument("createdDate") String createdDate
    ) {
        AlertsDTO newAlert = new AlertsDTO();
        newAlert.setDisaster(disaster);
        newAlert.setMessage(message);
        newAlert.setCreatedDate(java.sql.Date.valueOf(createdDate));
        return alertsService.createAlert(newAlert);
    }

    @DgsMutation
    public Boolean deleteAlert(@InputArgument("id") UUID id) {
        alertsService.deleteAlert(id);
        return true;
    }
}
