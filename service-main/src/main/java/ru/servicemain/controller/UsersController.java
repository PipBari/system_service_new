package ru.servicemain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.servicemain.service.UsersService;
import ru.systemapi.controllers.UsersApi;
import ru.systemapi.dto.UsersDTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class UsersController implements UsersApi {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public ResponseEntity<EntityModel<UsersDTO>> getUserById(UUID id) {
        UsersDTO user = usersService.getUserById(id);

        EntityModel<UsersDTO> userModel = EntityModel.of(user,
                linkTo(methodOn(UsersController.class).getUserById(id)).withSelfRel(),
                linkTo(methodOn(UsersController.class).getAllUsers()).withRel("all-users"));

        return ResponseEntity.ok(userModel);
    }

    @Override
    public ResponseEntity<CollectionModel<EntityModel<UsersDTO>>> getAllUsers() {
        List<EntityModel<UsersDTO>> users = usersService.getAllUsers().stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UsersController.class).getUserById(user.getId())).withSelfRel(),
                        linkTo(methodOn(UsersController.class).getAllUsers()).withRel("all-users")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<UsersDTO>> usersCollection = CollectionModel.of(users,
                linkTo(methodOn(UsersController.class).getAllUsers()).withSelfRel());

        return ResponseEntity.ok(usersCollection);
    }

    @Override
    public ResponseEntity<EntityModel<UsersDTO>> createUser(UsersDTO usersDTO) {
        UsersDTO createdUser = usersService.createUser(usersDTO);

        EntityModel<UsersDTO> userModel = EntityModel.of(createdUser,
                linkTo(methodOn(UsersController.class).getUserById(createdUser.getId())).withSelfRel(),
                linkTo(methodOn(UsersController.class).getAllUsers()).withRel("all-users"));

        return ResponseEntity
                .created(linkTo(methodOn(UsersController.class).getUserById(createdUser.getId())).toUri())
                .body(userModel);
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        usersService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
