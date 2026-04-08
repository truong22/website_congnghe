package com.devteria.hello_spring_boot.Respository;

import com.devteria.hello_spring_boot.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByRoleName(String roleName);
}
