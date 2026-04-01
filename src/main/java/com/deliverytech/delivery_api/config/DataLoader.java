package com.deliverytech.delivery_api.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner iniciarDados(ClienteRepository clienteRepository){
        return args ->{
            System.out.println("=====Inserindo Clientes======");

            Cliente c1 = new Cliente();
            c1.setNome("João Freitas");
            c1.setEmail("joao@gmail.com");
            c1.setTelefone("119999-8888");
            c1.setEndereco("av 1, 111");
            c1.setAtivo(true);

            Cliente c2 = new Cliente();
            c2.setNome("Mariana Freitas");
            c2.setEmail("mariana@gmail.com");
            c2.setTelefone("119999-7777");
            c2.setEndereco("av 2, 222");
            c2.setAtivo(true);


            Cliente c3 = new Cliente();
            c3.setNome("Joanna Silva");
            c3.setEmail("joanna@");
            c3.setTelefone("119999-7777");
            c3.setEndereco("av 3, 333");
            c3.setAtivo(true);


            clienteRepository.saveAll(List.of(c1, c2, c3));

            System.out.println("======Consultando Clientes======");

            System.out.println("> Buscar por email:");
            clienteRepository.findByEmail("joao@gmail.com").ifPresent(System.out::println);

            System.out.println("> Buscar por cliente contendo 'jo':");
            clienteRepository.findByNomeContainingIgnoreCase("jo")
            .forEach(c -> System.out.println(c.getNome()));

            System.out.println("> Verificar se email existe:");
            boolean existe = clienteRepository.existsByEmail("mariana@gmail.com");
            System.out.println("Existe Maria? " + existe);

            System.out.println("> Clientes ativos:");
            clienteRepository.findByAtivoTrue()
                .forEach(c -> System.out.println(c.getNome()));
        };
    }
}

