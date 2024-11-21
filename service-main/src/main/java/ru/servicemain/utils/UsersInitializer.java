package ru.servicemain.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.servicemain.dao.entity.Users;
import ru.servicemain.dao.rep.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class UsersInitializer {

    private final UsersRepository usersRepository;
    private final Random random = new Random();

    private final String[] firstNames = {
            "Александр", "Дмитрий", "Максим", "Иван", "Андрей",
            "Алексей", "Сергей", "Никита", "Егор", "Константин"
    };

    private final String[] lastNames = {
            "Иванов", "Смирнов", "Кузнецов", "Попов", "Соколов",
            "Лебедев", "Козлов", "Новиков", "Морозов", "Петров"
    };

    @Autowired
    public UsersInitializer(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @PostConstruct
    public void initData() {
        if (usersRepository.count() > 0) {
            System.out.println("Users already initialized. Skipping data generation.");
            return;
        }

        System.out.println("Initializing sample users...");

        List<Users> usersList = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            Users user = new Users();
            user.setFirstName(generateRandomFirstName());
            user.setSecondName(generateRandomLastName());
            user.setTelephoneNumber(generateUniquePhoneNumber());
            usersList.add(user);
        }

        usersRepository.saveAll(usersList);
        System.out.println("Sample users initialized successfully.");
    }

    private String generateRandomFirstName() {
        return firstNames[random.nextInt(firstNames.length)];
    }

    private String generateRandomLastName() {
        return lastNames[random.nextInt(lastNames.length)];
    }

    private String generateUniquePhoneNumber() {
        String phoneNumber;
        do {
            phoneNumber = String.format("+7 (9%02d) %03d-%02d-%02d",
                    random.nextInt(100),
                    random.nextInt(1000),
                    random.nextInt(100),
                    random.nextInt(100));
        } while (usersRepository.existsByTelephoneNumber(phoneNumber));

        return phoneNumber;
    }
}
