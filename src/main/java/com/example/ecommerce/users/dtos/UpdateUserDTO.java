package com.example.ecommerce.users.dtos;

import com.example.ecommerce.users.enums.RoleEnum;
import jakarta.validation.constraints.Email;
import lombok.Builder;


@Builder
public record UpdateUserDTO(
         String name,

        @Email  String email,

         Boolean isEnabled,

        Boolean credentialsNoExpired,

        Boolean accountNoLocked,

         RoleEnum role
){


}
