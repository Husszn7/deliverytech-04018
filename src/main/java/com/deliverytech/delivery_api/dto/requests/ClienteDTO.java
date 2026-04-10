package com.deliverytech.delivery_api.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description="Dados para cadastro/atualização de cliente.")
public class ClienteDTO {

    @Schema(description="Nome do cliente", example="Elaine Soares")
    @NotBlank(message = "Campo nome é obrigatório.")
    private String nome;

    @Schema(description="E-mail do cliente", example="elaine@gmail.com")
    @Email(message = "E-mail inválido.")
    @NotBlank(message = "Campo e-mail é obrigatório.")
    private String email;

    @Schema(description="Telefone/celular do cliente", example="(xx)xxxxx-xxxx")
    @Pattern(regexp="^\\(\\d{2}\\)\\d{4,5}-\\d{4}$",
        message="Formato de telefone inválido. Use (xx)xxxxx-xxxx"
    )
    @NotBlank(message="Campo telefone é obrigatório.")
    private String telefone;

    @Schema(description="Endereço do cliente", example="rua/av teste, 123")
    @Size(min= 5, message="Endereço deve ter no mínimo 5 caracteres")
    private String endereco;
}
