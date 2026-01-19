package com.example.Ecomercce.users.services;

import com.example.Ecomercce.shared.exceptions.NotFoundException;
import com.example.Ecomercce.users.enums.RoleEnum;
import com.example.Ecomercce.users.models.Role;
import com.example.Ecomercce.users.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {
  private final RoleRepository repo;

  public Role getRoleByEnum(RoleEnum enum1) {
    return repo.findByRoleEnum(enum1).orElseThrow(() -> new NotFoundException("Role not found"));
  }
}
