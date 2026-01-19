package com.example.Ecomercce.users.repository;

import com.example.Ecomercce.users.enums.RoleEnum;
import com.example.Ecomercce.users.models.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  public Optional<Role> findByRoleEnum(RoleEnum roleEnum);
}
