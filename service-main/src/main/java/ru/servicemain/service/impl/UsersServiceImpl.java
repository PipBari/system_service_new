package ru.servicemain.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.servicemain.dao.entity.Users;
import ru.servicemain.dao.rep.UsersRepository;
import ru.servicemain.service.UsersService;
import ru.systemapi.dto.UsersDTO;
import ru.systemapi.exception.ResourceNotFoundException;
import ru.systemapi.exception.BadRequestException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, ModelMapper modelMapper) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UsersDTO getUserById(UUID id) {
        if (id == null) {
            throw new BadRequestException("Invalid ID", "User ID cannot be null");
        }

        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return modelMapper.map(user, UsersDTO.class);
    }

    @Override
    public List<UsersDTO> getAllUsers() {
        List<Users> users = usersRepository.findAll();

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users found in the database");
        }

        return users.stream()
                .map(user -> modelMapper.map(user, UsersDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UsersDTO createUser(UsersDTO usersDTO) {
        if (usersDTO == null) {
            throw new BadRequestException("Invalid user data", "UserDTO cannot be null");
        }

        if (usersDTO.getFirstName() == null || usersDTO.getFirstName().isBlank()) {
            throw new BadRequestException("Invalid user data", "First name cannot be null or blank");
        }

        Users user = modelMapper.map(usersDTO, Users.class);
        Users savedUser = usersRepository.save(user);
        return modelMapper.map(savedUser, UsersDTO.class);
    }

    @Override
    public void deleteUser(UUID id) {
        if (id == null) {
            throw new BadRequestException("Invalid ID", "User ID cannot be null");
        }

        if (!usersRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }

        usersRepository.deleteById(id);
    }
}
