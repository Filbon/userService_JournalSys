package com.example.userservice_journalsys.Repository;

import com.example.userservice_journalsys.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);

    boolean existsByUserName(String userName);

    List<User> findByRoleIn(List<String> list);
}
