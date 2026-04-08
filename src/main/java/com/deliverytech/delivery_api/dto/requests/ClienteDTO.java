package com.deliverytech.delivery_api.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDTO {
    @NotBlank(message = "Campo nome é obrigatório.")
    private String nome;

    @Email(message = "E-mail inválido.")
    @NotBlank(message = "Campo e-mail é obrigatório.")
    private String email;

    @Pattern(regexp="^\\(\\d{2}\\)\\d{4,5}-\\d{4}$",
        message="Formato de telefone inválido. Use (xx)xxxxx-xxxx"
    )
    @NotBlank(message="Campo telefone é obrigatório.")
    private String telefone;

    @Size(min= 5, message="Endereço deve ter no mínimo 5 caracteres")
    private String endereco;
}
