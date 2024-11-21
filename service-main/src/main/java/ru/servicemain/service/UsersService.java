package ru.servicemain.service;

import ru.systemapi.dto.UsersDTO;

import java.util.List;
import java.util.UUID;

public interface UsersService {
    UsersDTO getUserById(UUID id);
    List<UsersDTO> getAllUsers();
    UsersDTO createUser(UsersDTO usersDTO);
    void deleteUser(UUID id);
}
