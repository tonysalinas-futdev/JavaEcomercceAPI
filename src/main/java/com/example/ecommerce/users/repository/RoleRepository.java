package com.example.ecommerce.users.repository;

import com.example.ecommerce.users.enums.RoleEnum;
import com.example.ecommerce.users.models.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  public Optional<Role> findByRoleEnum(RoleEnum roleEnum);
}
