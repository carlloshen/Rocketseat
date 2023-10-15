package br.com.henrique.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserRepository extends JpaRepository<UserModel, UUID> {
    Optional<UserModel> findByUserName(String userName);
}
