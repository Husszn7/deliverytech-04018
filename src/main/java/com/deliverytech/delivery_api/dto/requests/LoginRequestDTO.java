package com.deliverytech.delivery_api.dto.requests;

import com.deliverytech.delivery_api.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDTO {
    private String email;
    private String senha;
    private Role role;
}
