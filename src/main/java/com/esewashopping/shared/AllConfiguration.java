package com.esewashopping.shared;

import com.esewashopping.customer.Customer;
import com.esewashopping.customer.CustomerRepo;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AllConfiguration implements CommandLineRunner {

    private final CustomerRepo customerRepo;

    private final PasswordEncoder passwordEncoder;

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info().title("Esewa-Store")
                        .description("Some custom description of API.")
                        .version("1.0").contact(new Contact().name("Manjesh Rayamajhi")
                                .email( "www.esewa-store.com").url("esewa-store.com"))
                        .license(new License().name("License of API")
                                .url("API license URL")));
    }
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }


    @Override
    public void run(String... args) throws Exception {

        Customer customer=customerRepo.findByRole(Role.ADMIN);
        if (customer==null){
            Customer admin=Customer.builder().
                    fullName("Manjesh Rayamajhi")
                    .email("rayamajhimanjesh41@gmail.com")
                    .password(passwordEncoder.encode("manjesh@"))
                    .contact("9868384322")
                    .address("Chitwan")
                    .role(Role.ADMIN)
                    .status(Status.VERIFIED)
                    .build();
            customerRepo.save(admin);
        }
    }
}
