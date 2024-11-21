package ru.servicemain.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.servicemain.dao.entity.UserNotification;
import ru.servicemain.dao.rep.UserNotificationRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationListener {

    private final UserNotificationRepository userNotificationRepository;
    private final ConcurrentHashMap<String, String> processingStatusCache = new ConcurrentHashMap<>();

    @Autowired
    public NotificationListener(UserNotificationRepository userNotificationRepository) {
        this.userNotificationRepository = userNotificationRepository;
    }

    @RabbitListener(queues = "alerts.fire")
    public void listenFire(String notificationId) {
        processNotificationWithDelay(notificationId, "Пожар");
    }

    @RabbitListener(queues = "alerts.flood")
    public void listenFlood(String notificationId) {
        processNotificationWithDelay(notificationId, "Наводнение");
    }

    @RabbitListener(queues = "alerts.earthquake")
    public void listenEarthquake(String notificationId) {
        processNotificationWithDelay(notificationId, "Землетрясение");
    }

    @RabbitListener(queues = "alerts.storm")
    public void listenStorm(String notificationId) {
        processNotificationWithDelay(notificationId, "Шторм");
    }

    @RabbitListener(queues = "alerts.wind")
    public void listenWind(String notificationId) {
        processNotificationWithDelay(notificationId, "Сильный ветер");
    }

    private void processNotificationWithDelay(String notificationId, String disasterType) {
        try {
            UUID id = UUID.fromString(notificationId);
            processingStatusCache.put(notificationId, "В процессе");

            Optional<UserNotification> notificationOpt = userNotificationRepository.findById(id);

            if (notificationOpt.isPresent()) {
                UserNotification notification = notificationOpt.get();

                Thread.sleep(2000);
                System.out.println("Обработка уведомления о " + disasterType + " для региона: " +
                        notification.getAlert().getRegion().getRegion());
                processingStatusCache.put(notificationId, "Обработано");
            } else {
                System.out.println("Уведомление с ID " + notificationId + " не найдено в базе данных.");
                processingStatusCache.put(notificationId, "Не найдено");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректный формат ID уведомления: " + notificationId);
            processingStatusCache.put(notificationId, "Некорректный формат ID");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            processingStatusCache.remove(notificationId);
        }
    }

    public String getProcessingStatus(String notificationId) {
        return processingStatusCache.getOrDefault(notificationId, "Нет данных");
    }
}