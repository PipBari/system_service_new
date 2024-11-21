package ru.servicemain.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import ru.servicemain.service.UsersService;
import ru.systemapi.dto.UsersDTO;

import java.util.List;
import java.util.UUID;

@DgsComponent
public class UsersDataFetcher {

    private final UsersService usersService;

    public UsersDataFetcher(UsersService usersService) {
        this.usersService = usersService;
    }

    @DgsQuery
    public List<UsersDTO> allUsers() {
        return usersService.getAllUsers();
    }

    @DgsQuery
    public UsersDTO userById(@InputArgument("id") UUID id) {
        return usersService.getUserById(id);
    }

    @DgsMutation
    public UsersDTO createUser(
            @InputArgument("firstName") String firstName,
            @InputArgument("secondName") String secondName,
            @InputArgument("telephoneNumber") String telephoneNumber
    ) {
        UsersDTO newUser = new UsersDTO();
        newUser.setFirstName(firstName);
        newUser.setSecondName(secondName);
        newUser.setTelephoneNumber(telephoneNumber);
        return usersService.createUser(newUser);
    }

    @DgsMutation
    public Boolean deleteUser(@InputArgument("id") UUID id) {
        usersService.deleteUser(id);
        return true;
    }
}
