package ru.servicemain.dao.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.servicemain.dao.entity.Users;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {
    boolean existsByTelephoneNumber(String telephoneNumber);
    Optional<Users> findByTelephoneNumber(String telephoneNumber);
}

